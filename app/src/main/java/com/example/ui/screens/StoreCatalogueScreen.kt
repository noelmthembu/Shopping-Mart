package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CatalogueItem
import com.example.data.model.Store
import com.example.location.UserLocation
import com.example.ui.ShoppingViewModel
import com.example.ui.components.CatalogueItemCard
import com.example.ui.components.LocationSetupCard
import com.example.ui.components.StoreCard

@Composable
fun StoreCatalogueScreen(
    viewModel: ShoppingViewModel,
    modifier: Modifier = Modifier
) {
    val selectedStoreId by viewModel.selectedStoreId.collectAsState()
    val allStores by viewModel.allStores.collectAsState()
    val downloadedStores by viewModel.downloadedStores.collectAsState()
    val downloadingIds by viewModel.downloadingStoreIds.collectAsState()
    val storesWithDistance by viewModel.storesWithDistance.collectAsState()
    val loyaltyCards by viewModel.loyaltyCards.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()

    if (selectedStoreId != null) {
        val store = allStores.find { it.id == selectedStoreId }
        val hasLinkedCard = loyaltyCards.any { it.storeId == selectedStoreId && it.isLinked }

        BackHandler {
            viewModel.selectStoreForCatalogue(null)
        }

        if (store != null) {
            StoreDetailCatalogueView(
                store = store,
                hasLinkedCard = hasLinkedCard,
                viewModel = viewModel,
                onBack = { viewModel.selectStoreForCatalogue(null) }
            )
        }
    } else {
        StoreDirectoryView(
            stores = allStores,
            downloadedStores = downloadedStores,
            storesWithDistance = storesWithDistance,
            downloadingIds = downloadingIds,
            userLocation = userLocation,
            onLocationSelected = { lat, lng, area -> viewModel.setUserLocation(lat, lng, area) },
            onDetectGps = { viewModel.detectDeviceLocation() },
            onSelectStore = { viewModel.selectStoreForCatalogue(it) },
            onDownloadStore = { viewModel.downloadStore(it, openAfterDownload = true) },
            onRemoveDownload = { viewModel.removeStoreDownload(it) },
            onTriggerDealNotification = { viewModel.triggerNearbyPromotionNotification(it) },
            modifier = modifier
        )
    }
}

@Composable
fun StoreDirectoryView(
    stores: List<Store>,
    downloadedStores: List<Store>,
    storesWithDistance: List<com.example.location.StoreWithDistance>,
    downloadingIds: Set<String>,
    userLocation: UserLocation,
    onLocationSelected: (Double, Double, String) -> Unit,
    onDetectGps: () -> Unit,
    onSelectStore: (String) -> Unit,
    onDownloadStore: (String) -> Unit,
    onRemoveDownload: (String) -> Unit,
    onTriggerDealNotification: (Store) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    var showChangeLocation by remember { mutableStateOf(false) }

    val displayedStores = remember(stores, downloadedStores, selectedTab, searchQuery) {
        val baseList = if (selectedTab == 1) downloadedStores else stores
        if (searchQuery.isBlank()) {
            baseList
        } else {
            baseList.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.category.contains(searchQuery, ignoreCase = true) ||
                it.loyaltyProgramName.contains(searchQuery, ignoreCase = true) ||
                it.address.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Step 1: If location is not set or user clicked to change location
        if (!userLocation.isConfigured || showChangeLocation) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                LocationSetupCard(
                    onLocationSelected = { lat, lng, area ->
                        onLocationSelected(lat, lng, area)
                        showChangeLocation = false
                    },
                    onDetectGps = {
                        onDetectGps()
                        showChangeLocation = false
                    }
                )
            }
        } else {
            // Location confirmation bar
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Stores near: ${userLocation.areaName}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        TextButton(
                            onClick = { showChangeLocation = true },
                            modifier = Modifier.testTag("change_location_button")
                        ) {
                            Text("Change", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Hero Header featuring my-catalogue.co.za
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_deals_specials),
                        contentDescription = "Catalogue Header",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        contentScale = ContentScale.Crop,
                        alpha = 0.45f
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF007A3D)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Language,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "my-catalogue.co.za",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Step 2: Download Store",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Download Stores for Offline Shopping",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Download complete catalogues from Checkers, Pick n Pay, Woolworths, or Makro to start adding items to your cart offline.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search stores, categories or chains...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("store_search_input")
            )
        }

        // Tabs: All Stores vs Offline Ready
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("All Stores (${stores.size})", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF0A7D55),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Offline Ready (${downloadedStores.size})", fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        }

        // Stores list
        if (displayedStores.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (selectedTab == 1) "No Store Catalogues Downloaded" else "No matching stores found",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (selectedTab == 1)
                                "Switch to 'All Stores' and tap 'Download' on Checkers, Pick n Pay, or Woolworths to download and start shopping."
                            else
                                "Try searching for another supermarket or clear the search query.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(displayedStores, key = { it.id }) { store ->
                val distanceInfo = storesWithDistance.find { it.store.id == store.id }
                StoreCard(
                    store = store,
                    distanceFormatted = if (userLocation.isConfigured) distanceInfo?.distanceFormatted else null,
                    isDownloading = downloadingIds.contains(store.id),
                    onDownload = { onDownloadStore(store.id) },
                    onRemoveDownload = { onRemoveDownload(store.id) },
                    onOpenCatalogue = { onSelectStore(store.id) },
                    onTriggerDealAlert = { onTriggerDealNotification(store) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun StoreDetailCatalogueView(
    store: Store,
    hasLinkedCard: Boolean,
    viewModel: ShoppingViewModel,
    onBack: () -> Unit
) {
    val itemsFlow = remember(store.id) { viewModel.repository.getItemsForStore(store.id) }
    val catalogueItems by itemsFlow.collectAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("ALL") }

    val categories = remember(catalogueItems) {
        listOf("ALL") + catalogueItems.map { it.category }.distinct()
    }

    val filteredItems = remember(catalogueItems, searchQuery, selectedCategory) {
        catalogueItems.filter { item ->
            val matchesCategory = selectedCategory == "ALL" || item.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() || item.name.contains(searchQuery, ignoreCase = true) || item.promoTag.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Store Header with Back button
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("back_to_stores_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = store.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${store.category} • ${store.address}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // my-catalogue.co.za Validity Tag
        item {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF0FDF4),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "my-catalogue.co.za specials",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF166534)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Color(0xFF166534),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = store.catalogueValidity,
                            fontSize = 11.sp,
                            color = Color(0xFF166534)
                        )
                    }
                }
            }
        }

        // Offline status & Loyalty banner
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (hasLinkedCard) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CardMembership,
                        contentDescription = null,
                        tint = if (hasLinkedCard) Color(0xFF2E7D32) else Color(0xFFE65100),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (hasLinkedCard)
                                "${store.loyaltyProgramName} Linked • Member Club Discounts Active in Cart!"
                            else
                                "Link your ${store.loyaltyProgramName} in Loyalty tab to save up to ${store.discountPercentForCardHolders}% at the till!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (hasLinkedCard) Color(0xFF1B5E20) else Color(0xFFBF360C)
                        )
                    }
                }
            }
        }

        // Search Bar for Catalogue Items
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search items in ${store.name}...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("item_search_input")
            )
        }

        // Category filter chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(if (category == "ALL") "All Items" else category) }
                    )
                }
            }
        }

        // List of items in store
        if (filteredItems.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No items found in catalogue",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "If not downloaded yet, go back and tap Download.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredItems, key = { it.id }) { item ->
                CatalogueItemCard(
                    item = item,
                    hasLinkedCard = hasLinkedCard,
                    onAddToCart = {
                        viewModel.addItemToCart(item, store)
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
