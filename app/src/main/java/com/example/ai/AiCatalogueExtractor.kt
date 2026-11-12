package com.example.ai

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeBackend
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.ImagePart
import com.google.firebase.ai.type.ResponseModality
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

private const val TAG = "AiCatalogueExtractor"

data class ExtractedProduct(
    val name: String,
    val category: String,
    val regularPrice: Double,
    val cardDiscountPrice: Double,
    val unit: String,
    val promoTag: String,
    val imageType: String
)

data class AiFlyerExtractionResult(
    val storeChain: String,
    val flyerTitle: String,
    val products: List<ExtractedProduct>,
    val sourceDescription: String
)

class AiCatalogueExtractor {

    // Define JSON Schema for Gemini structured output
    private val productItemSchema = Schema.obj(
        properties = mapOf(
            "name" to Schema.string(description = "Full brand and product name, e.g. Koo Baked Beans in Tomato Sauce 410g"),
            "category" to Schema.string(description = "Category: Pantry & Staples, Dairy & Eggs, Meat & Butchery, Bakery & Bread, Beverages, Fresh Produce, Household & Cleaning, Snacks & Treats"),
            "regularPrice" to Schema.double(description = "Regular non-discounted retail price in ZAR Rands"),
            "cardDiscountPrice" to Schema.double(description = "Promotional or loyalty card price in ZAR Rands"),
            "unit" to Schema.string(description = "Packaging unit, e.g. 410g Tin, 30's Tray, 2 Litre, 5 kg Bag"),
            "promoTag" to Schema.string(description = "Promotional sticker or headline text, e.g. BUY ME TO ENTER • WIN A CAR, HOT SPECIAL, BUY 2 FOR R28"),
            "imageType" to Schema.string(description = "Product type identifier: koo_beans, rhodes_beans, baramakama_eggs, thokoman, clover_milk, white_star, rice, coffee, chicken, boerewors, chocolate, simba, butter")
        ),
        optionalProperties = listOf("promoTag")
    )

    private val flyerResponseSchema = Schema.obj(
        properties = mapOf(
            "storeChain" to Schema.string(description = "Store brand name, e.g. Shoprite, Checkers, Makro, SPAR, Pick n Pay, Woolworths"),
            "flyerTitle" to Schema.string(description = "Main promotional campaign headline"),
            "products" to Schema.array(
                items = productItemSchema,
                description = "List of distinct promotional products recognized on the flyer"
            )
        )
    )

    private val generationConfig = generationConfig {
        responseMimeType = "application/json"
        responseSchema = flyerResponseSchema
    }

    // Lazy initialization of Gemini 3.8 Flash model via Firebase AI Logic
    private val model by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = generationConfig
        )
    }

    // Gemini Nano Banana generates one product packshot per catalogue item.
    // The generated bitmap is saved locally by ShoppingRepository after each request.
    private val imageModel by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
            modelName = "gemini-nano-banana-2.1",
            generationConfig = generationConfig {
                responseModalities = listOf(ResponseModality.IMAGE)
            }
        )
    }

    suspend fun generateProductImage(item: CatalogueItem): Result<Bitmap> = withContext(Dispatchers.IO) {
        val prompt = """
            Create one premium, photorealistic e-commerce product packshot for this exact South African retail product.

            Product name: ${item.name}
            Product category: ${item.category}
            Product size: ${item.unit}
            Brand and product cues: ${item.imageType}

            Image requirements:
            - Show one product only, centred, upright and fully visible.
            - Use realistic retail packaging appropriate to the product (bottle, jar, box, pouch, tin, tray or bag).
            - Keep the product visually consistent with the named brand and product type.
            - Use a clean pure-white studio background with soft natural shadow.
            - Use professional supermarket catalogue photography and accurate proportions.
            - Do not add price labels, sale stickers, badges, extra products, hands, shelves or props.
            - Do not invent unrelated brand names, slogans, ingredients or claims.
            - No watermark, no borders and no text outside the product packaging.
            - Square 1:1 composition with the entire package inside the frame.
        """.trimIndent()

        var lastError: Exception? = null
        for (attempt in 0..1) {
            try {
                val response = withTimeout(90_000L) {
                    imageModel.generateContent(prompt)
                }
                val bitmap = response.candidates
                    .firstOrNull()
                    ?.content
                    ?.parts
                    ?.filterIsInstance<ImagePart>()
                    ?.firstOrNull()
                    ?.image

                if (bitmap != null) {
                    Log.d(TAG, "Gemini generated product image for ${item.name}")
                    return@withContext Result.success(bitmap)
                }

                lastError = IllegalStateException("Gemini returned no image for ${item.name}.")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                lastError = e
                Log.w(TAG, "Image generation attempt ${attempt + 1} failed for ${item.name}", e)
                if (attempt == 0) delay(1_500L)
            }
        }

        Result.failure(lastError ?: IllegalStateException("Gemini image generation failed for ${item.name}."))
    }

    private fun resolveBrandImageType(name: String, candidateType: String): String {
        val lower = "$name $candidateType".lowercase()
        return when {
            lower.contains("koo") -> "koo_beans"
            lower.contains("rhodes") -> "rhodes_beans"
            lower.contains("baramakama") || lower.contains("egg") -> "baramakama_eggs"
            lower.contains("thokoman") || lower.contains("peanut butter") -> "thokoman"
            lower.contains("clover") || (lower.contains("milk") && !lower.contains("coconut")) -> "clover_milk"
            lower.contains("white star") || lower.contains("maize") || lower.contains("mielie") -> "white_star"
            else -> candidateType
        }
    }

    /**
     * Extracts retail products and deals from a supermarket flyer bitmap using Gemini 3.8 Flash multimodal vision.
     */
    suspend fun extractFromFlyerBitmap(
        bitmap: Bitmap,
        preferredStoreId: String
    ): Result<AiFlyerExtractionResult> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Starting Gemini 3.8 Flash multimodal flyer extraction...")

            val prompt = """
                You are an expert South African supermarket flyer analyzer.
                Analyze the provided supermarket flyer image from the offline catalogue download.
                Detect each promotional product, can, bottle, box, or tray pictured on this flyer page.
                For each item:
                1. Identify the exact brand and title (e.g. 'Koo Baked Beans in Tomato Sauce 410g', 'Rhodes Baked Beans in Tomato Sauce 400g', 'Baramakama Eggs Large 30's', 'Thokoman Peanut Butter Smooth 1.1kg', 'Clover Full Cream Fresh Milk 2L', 'White Star Super Maize Meal 5kg').
                2. Extract the regular price and the big bold red discount price in South African Rands (ZAR).
                3. Extract any promotional banner text (such as 'BUY ME TO ENTER • WIN A CAR', 'HOT DEAL • 100g FREE', 'BUY 2 FOR R28').
                4. Set the exact imageType identifier matching the product:
                   - If it is Koo Baked Beans: use 'koo_beans'
                   - If it is Rhodes Baked Beans: use 'rhodes_beans'
                   - If it is Baramakama Eggs: use 'baramakama_eggs'
                   - If it is Thokoman Peanut Butter: use 'thokoman'
                   - If it is Clover Milk: use 'clover_milk'
                   - If it is White Star Maize: use 'white_star'
                   - Otherwise use a short descriptor like 'rice', 'coffee', 'chicken', 'boerewors', 'coke', 'butter'.
                Return strictly structured JSON matching the schema.
            """.trimIndent()

            val response = withTimeout(60_000L) {
                model.generateContent(
                    content {
                        image(bitmap)
                        text(prompt)
                    }
                )
            }

            val jsonText = response.text
            if (jsonText.isNullOrBlank()) {
                throw IllegalStateException("Gemini returned empty response text.")
            }

            val json = JSONObject(jsonText)
            val storeChain = json.optString("storeChain", "South African Retailer")
            val flyerTitle = json.optString("flyerTitle", "Weekly Supermarket Specials")
            val productsArray = json.optJSONArray("products")

            val products = mutableListOf<ExtractedProduct>()
            if (productsArray != null) {
                for (i in 0 until productsArray.length()) {
                    val p = productsArray.getJSONObject(i)
                    val name = p.getString("name")
                    val candidateType = p.optString("imageType", "pantry")
                    val brandType = resolveBrandImageType(name, candidateType)
                    products.add(
                        ExtractedProduct(
                            name = name,
                            category = p.optString("category", "Pantry & Staples"),
                            regularPrice = p.optDouble("regularPrice", 19.99),
                            cardDiscountPrice = p.optDouble("cardDiscountPrice", 14.99),
                            unit = p.optString("unit", "each"),
                            promoTag = p.optString("promoTag", "SPECIAL DEAL"),
                            imageType = brandType
                        )
                    )
                }
            }

            Log.d(TAG, "Gemini successfully extracted ${products.size} products from flyer.")
            Result.success(
                AiFlyerExtractionResult(
                    storeChain = storeChain,
                    flyerTitle = flyerTitle,
                    products = products,
                    sourceDescription = "Extracted via Gemini 3.8 Flash Vision"
                )
            )
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Gemini inference timed out after 60s", e)
            Result.failure(e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Gemini multimodal flyer analysis failed, returning offline parsed fallback", e)
            Result.failure(e)
        }
    }
}
