package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.CatalogueItem
import com.example.data.model.Store

@Composable
fun StoreBrandBadge(store: Store, modifier: Modifier = Modifier) {
    val logoResId = when {
        store.name.contains("Checkers", ignoreCase = true) || store.chain.contains("Checkers", ignoreCase = true) -> R.drawable.logo_checkers
        store.name.contains("Pick n Pay", ignoreCase = true) || store.chain.contains("Pick n Pay", ignoreCase = true) -> R.drawable.logo_pnp
        store.name.contains("Shoprite", ignoreCase = true) || store.chain.contains("Shoprite", ignoreCase = true) -> R.drawable.logo_shoprite
        store.name.contains("Makro", ignoreCase = true) || store.chain.contains("Makro", ignoreCase = true) -> R.drawable.logo_makro
        store.name.contains("SPAR", ignoreCase = true) || store.chain.contains("SPAR", ignoreCase = true) -> R.drawable.logo_spar
        store.name.contains("Woolworths", ignoreCase = true) || store.chain.contains("Woolworths", ignoreCase = true) -> R.drawable.logo_woolworths
        store.name.contains("Food Lover", ignoreCase = true) || store.chain.contains("Food Lover", ignoreCase = true) -> R.drawable.logo_foodlovers
        store.name.contains("Dis-Chem", ignoreCase = true) || store.chain.contains("Dis-Chem", ignoreCase = true) -> R.drawable.logo_dischem
        store.name.contains("Clicks", ignoreCase = true) || store.chain.contains("Clicks", ignoreCase = true) -> R.drawable.logo_clicks
        store.name.contains("Game", ignoreCase = true) || store.chain.contains("Game", ignoreCase = true) -> R.drawable.logo_game
        store.name.contains("PEP", ignoreCase = true) || store.chain.contains("PEP", ignoreCase = true) -> R.drawable.logo_pep
        store.name.contains("Builders", ignoreCase = true) || store.chain.contains("Builders", ignoreCase = true) -> R.drawable.logo_builders
        else -> null
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        shadowElevation = 1.dp,
        modifier = modifier.size(width = 74.dp, height = 50.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            if (logoResId != null) {
                Image(
                    painter = painterResource(id = logoResId),
                    contentDescription = store.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(
                    text = store.name.take(8),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = Color(0xFF007A3D)
                )
            }
        }
    }
}

@Composable
fun StoreCard(
    store: Store,
    distanceFormatted: String?,
    isDownloading: Boolean,
    onDownload: () -> Unit,
    onRemoveDownload: () -> Unit,
    onOpenCatalogue: () -> Unit,
    onTriggerDealAlert: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("store_card_${store.id}")
            .clickable {
                onOpenCatalogue()
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row: Brand Logo Badge, Name, Category & Distance
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StoreBrandBadge(store = store)

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = store.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        if (!distanceFormatted.isNullOrBlank()) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF1F5F9)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = "Distance",
                                        tint = Color(0xFF007A3D),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = distanceFormatted,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF334155)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${store.category} • ${store.address}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Catalogue Validity & Deal Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(0.5.dp, Color(0xFFE2E8F0))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Color(0xFF475569),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = store.catalogueValidity,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (store.catalogueValidity.contains("Expired", ignoreCase = true)) Color(0xFFB91C1C) else Color(0xFF475569)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFE8F5E9)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CardMembership,
                            contentDescription = null,
                            tint = Color(0xFF1B5E20),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${store.loyaltyProgramName} (-${store.discountPercentForCardHolders}%)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                    }
                }
            }

            if (store.promoBannerText.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = store.promoBannerText,
                    fontSize = 11.sp,
                    color = Color(0xFFD8232A),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Row: Download Status & Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (store.isDownloaded) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Offline ready",
                            tint = Color(0xFF007A3D),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Offline Ready (${store.totalItemsCount} items)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF007A3D)
                        )
                    }
                } else {
                    Text(
                        text = "Catalogue not downloaded yet",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    if (onTriggerDealAlert != null) {
                        IconButton(
                            onClick = onTriggerDealAlert,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = "Promotion alert",
                                tint = Color(0xFF007A3D),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    if (store.isDownloaded) {
                        IconButton(
                            onClick = onRemoveDownload,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete download",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Button(
                            onClick = onOpenCatalogue,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF007A3D)
                            ),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("open_catalogue_${store.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Open & Shop", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    } else {
                        Button(
                            onClick = onDownload,
                            enabled = !isDownloading,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF007A3D)
                            ),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("download_catalogue_${store.id}")
                        ) {
                            if (isDownloading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(14.dp),
                                    strokeWidth = 2.dp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Downloading...", fontSize = 12.sp)
                            } else {
                                Icon(
                                    imageVector = Icons.Default.CloudDownload,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Download Catalogue", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Prominent South African Supermarket Flyer Price Tag
 * Renders the iconic red rounded rectangular price tag with large bold white numbers.
 */
@Composable
fun RedFlyerPriceTag(
    regularPrice: Double,
    discountPrice: Double,
    modifier: Modifier = Modifier
) {
    val isDiscounted = discountPrice < regularPrice
    val displayPrice = if (isDiscounted) discountPrice else regularPrice
    val priceStr = String.format("%.2f", displayPrice)
    val parts = priceStr.split(".")
    val integerPart = parts.getOrNull(0) ?: priceStr
    val decimalPart = parts.getOrNull(1) ?: "00"

    Column(modifier = modifier, horizontalAlignment = Alignment.Start) {
        if (isDiscounted) {
            Text(
                text = String.format("R %.2f", regularPrice),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8),
                textDecoration = TextDecoration.LineThrough,
                fontSize = 11.sp
            )
        }

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFE31B23), // Bold South African retail red
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = integerPart,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = decimalPart,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }
        }
    }
}

/**
 * Authentic 2-Column Flyer Grid Tile (matching image_3a6978c1.png)
 */
@Composable
fun FlyerDealGridCard(
    item: CatalogueItem,
    hasLinkedCard: Boolean,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEggsDeal = item.imageType == "baramakama_eggs" || item.name.contains("Eggs Large 30", ignoreCase = true)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("flyer_grid_item_${item.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Product Visual Container with Promotional Megaphone Sticker
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8FAFC)),
                contentAlignment = Alignment.Center
            ) {
                val specificDrawable = ProductVisuals.getProductDrawableRes(item.name, item.imageType)
                if (specificDrawable != null) {
                    Image(
                        painter = painterResource(id = specificDrawable),
                        contentDescription = item.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    )
                } else if (item.imageUrl.isNotBlank() && !item.imageUrl.contains("images.unsplash.com")) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = item.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    ItemPicture(
                        itemName = item.name,
                        category = item.category,
                        imageType = item.imageType,
                        imageUrl = item.imageUrl,
                        size = PictureSize.LARGE
                    )
                }

                // Promotional Megaphone / Sticker Badge
                if (!isEggsDeal && item.promoTag.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(topStart = 8.dp, bottomEnd = 8.dp),
                        color = Color(0xFFE31B23),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = item.promoTag,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Product Title & Unit
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp,
                fontSize = 13.sp
            )

            Text(
                text = item.unit,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF64748B),
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Price Tag and Add Button Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                RedFlyerPriceTag(
                    regularPrice = item.regularPrice,
                    discountPrice = item.cardDiscountPrice
                )

                Button(
                    onClick = onAddToCart,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF007A3D)
                    ),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("grid_add_${item.id}")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("Add", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Standard Catalogue Item Row (List View) with South African Flyer Styling
 */
@Composable
fun CatalogueItemCard(
    item: CatalogueItem,
    hasLinkedCard: Boolean,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEggsDeal = item.imageType == "baramakama_eggs" || item.name.contains("Eggs Large 30", ignoreCase = true)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("catalogue_item_${item.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Picture with flyer styling
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                val specificDrawable = ProductVisuals.getProductDrawableRes(item.name, item.imageType)
                if (specificDrawable != null) {
                    Image(
                        painter = painterResource(id = specificDrawable),
                        contentDescription = item.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    )
                } else if (item.imageUrl.isNotBlank() && !item.imageUrl.contains("images.unsplash.com")) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = item.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    ItemPicture(
                        itemName = item.name,
                        category = item.category,
                        imageType = item.imageType,
                        imageUrl = item.imageUrl,
                        size = PictureSize.MEDIUM
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${item.category} • ${item.unit}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF64748B)
                )

                if (item.promoTag.isNotBlank()) {
                    Text(
                        text = item.promoTag,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD8232A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                RedFlyerPriceTag(
                    regularPrice = item.regularPrice,
                    discountPrice = item.cardDiscountPrice
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Add button
            Button(
                onClick = onAddToCart,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF007A3D)
                ),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("add_to_cart_${item.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.AddShoppingCart,
                    contentDescription = "Add to cart",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Add",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
