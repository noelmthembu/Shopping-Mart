package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Egg
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.OutdoorGrill
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest

enum class PictureSize(val dp: Dp, val iconSize: Dp, val fontSize: Float) {
    SMALL(46.dp, 22.dp, 18f),
    MEDIUM(64.dp, 30.dp, 26f),
    LARGE(80.dp, 38.dp, 34f)
}

data class ProductVisualTheme(
    val bgGradient: Brush,
    val icon: ImageVector,
    val emoji: String,
    val accentColor: Color,
    val labelText: String
)

object ProductVisuals {
    fun resolveTheme(name: String, category: String, imageType: String): ProductVisualTheme {
        val lower = "$name $category $imageType".lowercase()

        return when {
            // Milk & Dairy
            lower.contains("milk") || lower.contains("dairy") || lower.contains("clover") || lower.contains("ayrshire") -> ProductVisualTheme(
                bgGradient = Brush.linearGradient(listOf(Color(0xFFE1F5FE), Color(0xFFB3E5FC))),
                icon = Icons.Default.Egg,
                emoji = "🥛",
                accentColor = Color(0xFF0288D1),
                labelText = "Dairy"
            )
            // Braai, Meat & Butchery
            lower.contains("boerewors") || lower.contains("braai") || lower.contains("meat") ||
            lower.contains("chops") || lower.contains("mince") || lower.contains("beef") || lower.contains("butchery") -> ProductVisualTheme(
                bgGradient = Brush.linearGradient(listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2))),
                icon = Icons.Default.OutdoorGrill,
                emoji = "🥩",
                accentColor = Color(0xFFD32F2F),
                labelText = "Braai"
            )
            // Chicken & Poultry
            lower.contains("chicken") || lower.contains("rotisserie") || lower.contains("rainbow") || lower.contains("goldi") -> ProductVisualTheme(
                bgGradient = Brush.linearGradient(listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2))),
                icon = Icons.Default.Restaurant,
                emoji = "🍗",
                accentColor = Color(0xFFE65100),
                labelText = "Chicken"
            )
            // Bakery & Bread
            lower.contains("bread") || lower.contains("bakery") || lower.contains("baguette") ||
            lower.contains("croissant") || lower.contains("rusks") || lower.contains("sasko") || lower.contains("albany") -> ProductVisualTheme(
                bgGradient = Brush.linearGradient(listOf(Color(0xFFFFF8E1), Color(0xFFFFECB3))),
                icon = Icons.Default.BakeryDining,
                emoji = "🍞",
                accentColor = Color(0xFFFFA000),
                labelText = "Bakery"
            )
            // Coffee & Tea
            lower.contains("coffee") || lower.contains("tea") || lower.contains("nescafé") ||
            lower.contains("jacobs") || lower.contains("five roses") || lower.contains("rooibos") -> ProductVisualTheme(
                bgGradient = Brush.linearGradient(listOf(Color(0xFFEFEBE9), Color(0xFFD7CCC8))),
                icon = Icons.Default.Coffee,
                emoji = "☕",
                accentColor = Color(0xFF5D4037),
                labelText = "Coffee"
            )
            // Soft Drinks & Beverages
            lower.contains("coca-cola") || lower.contains("coke") || lower.contains("juice") ||
            lower.contains("beverage") || lower.contains("appletiser") || lower.contains("drink") -> ProductVisualTheme(
                bgGradient = Brush.linearGradient(listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2))),
                icon = Icons.Default.LocalDrink,
                emoji = "🥤",
                accentColor = Color(0xFFC2185B),
                labelText = "Drinks"
            )
            // Beer / Liquor
            lower.contains("castle") || lower.contains("beer") || lower.contains("lager") -> ProductVisualTheme(
                bgGradient = Brush.linearGradient(listOf(Color(0xFFFFFDE7), Color(0xFFFFF59D))),
                icon = Icons.Default.LocalDrink,
                emoji = "🍺",
                accentColor = Color(0xFFFBC02D),
                labelText = "Beverage"
            )
            // Butter & Spreads
            lower.contains("butter") || lower.contains("butro") || lower.contains("spread") || lower.contains("cheese") -> ProductVisualTheme(
                bgGradient = Brush.linearGradient(listOf(Color(0xFFFFFDE7), Color(0xFFFFF9C4))),
                icon = Icons.Default.Egg,
                emoji = "🧈",
                accentColor = Color(0xFFFBC02D),
                labelText = "Dairy"
            )
            // Maize Meal & Staples & Rice & Sugar
            lower.contains("maize") || lower.contains("white star") || lower.contains("rice") ||
            lower.contains("tastic") || lower.contains("sugar") || lower.contains("huletts") || lower.contains("flour") || lower.contains("staple") -> ProductVisualTheme(
                bgGradient = Brush.linearGradient(listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))),
                icon = Icons.Default.Restaurant,
                emoji = "🌾",
                accentColor = Color(0xFF388E3C),
                labelText = "Staples"
            )
            // Chips & Snacks
            lower.contains("chips") || lower.contains("simba") || lower.contains("lay's") || lower.contains("snack") -> ProductVisualTheme(
                bgGradient = Brush.linearGradient(listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2))),
                icon = Icons.Default.LocalPizza,
                emoji = "🥔",
                accentColor = Color(0xFFF57C00),
                labelText = "Snacks"
            )
            // Chocolate & Biscuits
            lower.contains("chocolate") || lower.contains("cadbury") || lower.contains("chuckles") ||
            lower.contains("biscuit") || lower.contains("sweet") || lower.contains("romany") -> ProductVisualTheme(
                bgGradient = Brush.linearGradient(listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))),
                icon = Icons.Default.Restaurant,
                emoji = "🍫",
                accentColor = Color(0xFF7B1FA2),
                labelText = "Treats"
            )
            // Fruits & Veg
            lower.contains("avocado") || lower.contains("produce") || lower.contains("spinach") ||
            lower.contains("fruit") || lower.contains("veg") || lower.contains("kiwi") -> ProductVisualTheme(
                bgGradient = Brush.linearGradient(listOf(Color(0xFFE8F8F2), Color(0xFFC8E6C9))),
                icon = Icons.Default.Restaurant,
                emoji = "🥑",
                accentColor = Color(0xFF007A3D),
                labelText = "Fresh"
            )
            // Canned & Condiments
            lower.contains("beans") || lower.contains("koo") || lower.contains("tomato sauce") ||
            lower.contains("all gold") || lower.contains("chutney") || lower.contains("mayonnaise") -> ProductVisualTheme(
                bgGradient = Brush.linearGradient(listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2))),
                icon = Icons.Default.Restaurant,
                emoji = "🥫",
                accentColor = Color(0xFFD32F2F),
                labelText = "Pantry"
            )
            // Household & Detergent
            lower.contains("omo") || lower.contains("sunlight") || lower.contains("washing") ||
            lower.contains("dishwashing") || lower.contains("toilet") || lower.contains("clean") -> ProductVisualTheme(
                bgGradient = Brush.linearGradient(listOf(Color(0xFFE0F7FA), Color(0xFFB2EBF2))),
                icon = Icons.Default.CleaningServices,
                emoji = "🧼",
                accentColor = Color(0xFF0097A7),
                labelText = "Clean"
            )
            // Pharmacy & Healthcare
            lower.contains("vitamin") || lower.contains("panado") || lower.contains("nivea") ||
            lower.contains("diaper") || lower.contains("dettol") || lower.contains("pharmacy") -> ProductVisualTheme(
                bgGradient = Brush.linearGradient(listOf(Color(0xFFEDE7F6), Color(0xFFD1C4E9))),
                icon = Icons.Default.LocalPharmacy,
                emoji = "💊",
                accentColor = Color(0xFF512DA8),
                labelText = "Care"
            )
            else -> ProductVisualTheme(
                bgGradient = Brush.linearGradient(listOf(Color(0xFFECEFF1), Color(0xFFCFD8DC))),
                icon = Icons.Default.ShoppingBag,
                emoji = "🛒",
                accentColor = Color(0xFF455A64),
                labelText = "Grocery"
            )
        }
    }
}

@Composable
fun ItemPicture(
    itemName: String,
    category: String,
    imageType: String = "",
    imageUrl: String = "",
    size: PictureSize = PictureSize.MEDIUM,
    discountBadgeText: String? = null,
    modifier: Modifier = Modifier
) {
    val theme = ProductVisuals.resolveTheme(itemName, category, imageType)

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(theme.bgGradient)
            .border(1.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isNotBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = itemName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        } else {
            // High-fidelity graphic product visual
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(2.dp)
            ) {
                Text(
                    text = theme.emoji,
                    fontSize = size.fontSize.sp
                )
                if (size != PictureSize.SMALL) {
                    Text(
                        text = theme.labelText,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = theme.accentColor,
                        maxLines = 1
                    )
                }
            }
        }

        // Deal Badge overlay
        if (!discountBadgeText.isNullOrBlank()) {
            Surface(
                shape = RoundedCornerShape(topStart = 8.dp, bottomEnd = 8.dp),
                color = Color(0xFFE53935),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-1).dp, y = (-1).dp)
            ) {
                Text(
                    text = discountBadgeText,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
            }
        }
    }
}
