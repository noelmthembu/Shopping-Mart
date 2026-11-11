package com.example.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.R
import java.io.File
import java.io.FileOutputStream

private const val TAG = "OfflineProductImageStore"

object OfflineProductImageStore {

    private fun getImagesDir(context: Context): File {
        val dir = File(context.filesDir, "product_images")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    private fun sanitizeKey(key: String): String {
        return key.lowercase()
            .replace(Regex("[^a-z0-9_]"), "_")
            .replace(Regex("_+"), "_")
            .trim('_')
    }

    /**
     * Saves a product bitmap to persistent internal storage for offline use.
     * Returns the absolute file URI string (file:///...).
     */
    fun saveImageLocally(context: Context, productKey: String, bitmap: Bitmap): String {
        val cleanKey = sanitizeKey(productKey)
        val file = File(getImagesDir(context), "$cleanKey.jpg")
        return try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            Log.d(TAG, "Saved offline product image for $cleanKey to ${file.absolutePath}")
            "file://${file.absolutePath}"
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save offline product image for $cleanKey", e)
            ""
        }
    }

    /**
     * Checks if a persistent offline image file exists for the given product key/name/type.
     * Returns the file URI if found, or null otherwise.
     */
    fun getLocalImageUri(context: Context, productKey: String): String? {
        val cleanKey = sanitizeKey(productKey)
        val file = File(getImagesDir(context), "$cleanKey.jpg")
        return if (file.exists() && file.length() > 0) {
            "file://${file.absolutePath}"
        } else {
            null
        }
    }

    /**
     * Copies pre-packaged high-res drawable resources into persistent internal files directory on app startup
     * so that all products have authentic offline image files stored in internal storage.
     */
    fun ensureBundledImagesSavedLocally(context: Context) {
        val bundledMap = mapOf(
            "product_bio_oil" to R.drawable.product_bio_oil,
            "product_biogen_collagen" to R.drawable.product_biogen_collagen,
            "product_cetaphil_cleanser" to R.drawable.product_cetaphil_cleanser,
            "product_corenza_c" to R.drawable.product_corenza_c,
            "product_albany_bread" to R.drawable.product_albany_bread,
            "product_coke_2l" to R.drawable.product_coke_2l,
            "product_jacobs_gold" to R.drawable.product_jacobs_gold,
            "product_nescafe_classic" to R.drawable.product_nescafe_classic,
            "product_koo_beans" to R.drawable.product_koo_beans,
            "product_rhodes_beans" to R.drawable.product_rhodes_beans,
            "product_clover_milk" to R.drawable.product_clover_milk,
            "product_white_star_maize" to R.drawable.product_white_star_maize,
            "deal_baramakama_eggs" to R.drawable.deal_baramakama_eggs,
            "deal_thokoman" to R.drawable.deal_thokoman
        )

        bundledMap.forEach { (key, resId) ->
            val file = File(getImagesDir(context), "$key.jpg")
            if (!file.exists() || file.length() == 0L) {
                try {
                    val bitmap = BitmapFactory.decodeResource(context.resources, resId)
                    if (bitmap != null) {
                        saveImageLocally(context, key, bitmap)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Could not seed offline image $key", e)
                }
            }
        }
    }
}
