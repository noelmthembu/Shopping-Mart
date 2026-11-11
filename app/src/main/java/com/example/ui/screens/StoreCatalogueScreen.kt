package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import com.example.ui.AiExtractionUiState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CatalogueItem
import com.example.data.model.Store
import com.example.data.repository.SampleStoreData
import com.example.location.UserLocation
import com.example.ui.ShoppingViewModel
import com.example.ui.components.CatalogueItemCard
import com.example.ui.components.FlyerDealGridCard
import com.example.ui.components.LocationSetupCard
import com.example.ui.components.StoreBrandBadge
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
            onAddFeaturedDeal = { deal ->
                val store = allStores.find { it.id == deal.storeId }
                viewModel.addItemToCart(deal, store)
            },
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
    onAddFeaturedDeal: (CatalogueItem) -> Unit,
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
        // Location setup / confirmation bar
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
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
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
                                tint = Color(0xFF007A3D),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Stores near: ${userLocation.areaName}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }
                        TextButton(
                            onClick = { showChangeLocation = true },
                            modifier = Modifier.testTag("change_location_button")
                        ) {
                            Text("Change", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF007A3D))
                        }
                    }
                }
            }
        }

        // Hero Header featuring my-catalogue.co.za
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_deals_specials),
                        contentDescription = "Catalogue Header",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        contentScale = ContentScale.Crop,
                        alpha = 0.25f
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
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
                                        text = "SAMPLE CATALOGUE",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Weekly Retail Flyers",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF007A3D)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "South African Supermarket Catalogues",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                                text = "Browse sample catalogue listings for Shoprite, Checkers Hyper, Pick n Pay, SPAR, Makro and Woolworths Food. Confirm prices in store.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF475569)
                        )
                    }
                }
            }
        }

        // HOTTEST WEEKLY FLYER SPECIALS SHOWCASE (Matching user attachment)
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFE31B23)
                        ) {
                            Text(
                                text = "HOT DEALS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sample Flyer Offers",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A)
                        )
                    }
                    Text(
                        text = "SAMPLE OFFERS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF007A3D)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(SampleStoreData.featuredFlyerDeals, key = { it.id }) { deal ->
                        Box(modifier = Modifier.width(185.dp)) {
                            FlyerDealGridCard(
                                item = deal,
                                hasLinkedCard = true,
                                onAddToCart = { onAddFeaturedDeal(deal) }
                            )
                        }
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search stores, categories or retail chains...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF007A3D)) },
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
                    text = { Text("All Stores (${stores.size})", fontWeight = FontWeight.Bold, color = if (selectedTab == 0) Color(0xFF007A3D) else Color(0xFF64748B)) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (selectedTab == 1) Color(0xFF007A3D) else Color(0xFF64748B),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Offline Ready (${downloadedStores.size})",
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == 1) Color(0xFF007A3D) else Color(0xFF64748B)
                            )
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
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
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
                            tint = Color(0xFF007A3D),
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (selectedTab == 1) "No Store Catalogues Downloaded" else "No matching stores found",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (selectedTab == 1)
                                "Switch to 'All Stores' and tap 'Download' on Shoprite, Checkers, or Pick n Pay to download for offline shopping."
                            else
                                "Try searching for another supermarket chain or clear the search query.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B),
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
  
@Composable
fun StoreDetailCatalogueView(
    store: Store,
    hasLinkedCard: Boolean,
    viewModel: ShoppingViewModel,
    onBack: () -> Unit
) {
    val itemsFlow = remember(store.id) { viewModel.repository.getItemsForStore(store.id) }
    val catalogueItems by itemsFlow.collectAsState(initial = emptyList())
    val aiState by viewModel.aiExtractionState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("ALL") }
    var isFlyerGridView by remember { mutableStateOf(true) }
    var showSearch by remember { mutableStateOf(false) }
    var showToolsDialog by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.scanFlyerImageUri(uri, store.id)
        }
    }

    androidx.compose.runtime.LaunchedEffect(store.id) {
        viewModel.repository.ensureItemsLoadedForStore(store.id)
    }

    val allStoreItems = remember(catalogueItems, store.id) {
        if (catalogueItems.isNotEmpty()) catalogueItems else SampleStoreData.getCatalogueForStore(store.id)
    }

    val categories = remember(allStoreItems) {
        listOf("ALL") + allStoreItems.map { it.category }.distinct()
    }

    val filteredItems = remember(allStoreItems, searchQuery, selectedCategory) {
        allStoreItems.filter { item ->
            val matchesCategory = selectedCategory == "ALL" || item.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() ||
                item.name.contains(searchQuery, ignoreCase = true) ||
                item.promoTag.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    if (showToolsDialog) {
        AlertDialog(
            onDismissRequest = { showToolsDialog = false },
            title = {
                Text(
                    text = "Catalogue tools",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Import authentic product photos from a downloaded flyer or scan a new flyer.",
                        color = Color(0xFF64748B),
                        fontSize = 13.sp
                    )

                    when (val state = aiState) {
                        is AiExtractionUiState.Scanning -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = Color(0xFF007A3D)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(state.statusMessage, fontSize = 12.sp, color = Color(0xFF065F46))
                            }
                        }
                        is AiExtractionUiState.Success -> {
                            Text(state.message, fontSize = 12.sp, color = Color(0xFF166534))
                        }
                        is AiExtractionUiState.Error -> {
                            Text(state.errorMessage, fontSize = 12.sp, color = Color(0xFFB91C1C))
                        }
                        AiExtractionUiState.Idle -> Unit
                    }

                    Button(
                        onClick = { viewModel.extractAiImagesFromDownload(store.id) },
                        enabled = aiState !is AiExtractionUiState.Scanning,
                        modifier = Modifier.fillMaxWidth(),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF007A3D)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("AI extract product images", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    androidx.compose.material3.OutlinedButton(
                        onClick = {
                            showToolsDialog = false
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = Color(0xFF007A3D),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Scan flyer photo", color = Color(0xFF007A3D), fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showToolsDialog = false
                        viewModel.dismissAiExtractionDialog()
                    }
                ) {
                    Text("Done", color = Color(0xFF007A3D), fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Compact store context and catalogue actions.
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp, bottom = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("back_to_stores_button")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to stores",
                        tint = Color(0xFF334155),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = store.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${store.category} • ${store.catalogueValidity}",
                        fontSize = 10.sp,
                        color = Color(0xFF64748B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = { showSearch = !showSearch },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = if (showSearch) "Hide search" else "Search products",
                        tint = if (showSearch) Color(0xFF007A3D) else Color(0xFF475569),
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = { showToolsDialog = true },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = "Catalogue tools",
                        tint = Color(0xFF007A3D),
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = { isFlyerGridView = !isFlyerGridView },
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("catalogue_view_toggle")
                ) {
                    Icon(
                        imageVector = if (isFlyerGridView) Icons.Default.ViewList else Icons.Default.GridView,
                        contentDescription = if (isFlyerGridView) "Switch to list view" else "Switch to grid view",
                        tint = Color(0xFF475569),
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }

        if (showSearch) {
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search products in ${store.name}...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF007A3D))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear search")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("item_search_input")
                )
            }
        }

        // Horizontal category pills, matching the compact reference.
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories, key = { it }) { category ->
                    val selected = selectedCategory == category
                    FilterChip(
                        selected = selected,
                        onClick = { selectedCategory = category },
                        label = {
                            Text(
                                text = if (category == "ALL") "All Flyer Items" else category,
                                fontSize = 11.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            1.dp,
                            if (selected) Color(0xFF007A3D) else Color(0xFFCBD5D1)
                        ),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            labelColor = Color(0xFF475569),
                            selectedContainerColor = Color(0xFFF0FDF4),
                            selectedLabelColor = Color(0xFF007A3D)
                        )
                    )
                }
            }
        }

        // Product count and a subtle sample-pricing note.
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Showing ${filteredItems.size} products in ${if (selectedCategory == "ALL") "all categories" else selectedCategory}",
                        fontSize = 10.sp,
                        color = Color(0xFF64748B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${allStoreItems.size} total in store",
                        fontSize = 10.sp,
                        color = Color(0xFF007A3D),
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = "Sample catalogue prices • confirm availability in store",
                    fontSize = 9.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }

        if (filteredItems.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAF9)),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0xFF007A3D),
                            modifier = Modifier.size(34.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No products found",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Try another search or category.",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        } else if (isFlyerGridView) {
            val chunkedItems = filteredItems.chunked(2)
            items(chunkedItems) { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    val item1 = pair[0]
                    Box(modifier = Modifier.weight(1f)) {
                        FlyerDealGridCard(
                            item = item1,
                            hasLinkedCard = hasLinkedCard,
                            onAddToCart = { viewModel.addItemToCart(item1, store) }
                        )
                    }

                    if (pair.size > 1) {
                        val item2 = pair[1]
                        Box(modifier = Modifier.weight(1f)) {
                            FlyerDealGridCard(
                                item = item2,
                                hasLinkedCard = hasLinkedCard,
                                onAddToCart = { viewModel.addItemToCart(item2, store) }
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        } else {
            items(filteredItems, key = { it.id }) { item ->
                CatalogueItemCard(
                    item = item,
                    hasLinkedCard = hasLinkedCard,
                    onAddToCart = { viewModel.addItemToCart(item, store) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

