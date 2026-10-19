package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.ShoppingViewModel
import com.example.ui.components.LocationDiscoveryScreen
import com.example.ui.screens.LoyaltyCardsScreen
import com.example.ui.screens.NearbyStoresScreen
import com.example.ui.screens.ShoppingListScreen
import com.example.ui.screens.StoreCatalogueScreen
import com.example.ui.theme.SmartShopperTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    private val viewModel: ShoppingViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Handle intent from promotion notification
        val targetStoreId = intent?.getStringExtra("TARGET_STORE_ID")
        if (!targetStoreId.isNullOrBlank()) {
            viewModel.selectStoreForCatalogue(targetStoreId)
            viewModel.navigateTo(AppScreen.STORE_CATALOGUES)
        }

        setContent {
            SmartShopperTheme {
                val currentScreen by viewModel.currentScreen.collectAsState()
                val budgetSummary by viewModel.budgetSummary.collectAsState()
                val shoppingList by viewModel.shoppingList.collectAsState()
                val downloadedStores by viewModel.downloadedStores.collectAsState()
                val userLocation by viewModel.userLocation.collectAsState()

                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(Unit) {
                    viewModel.userMessage.collectLatest { message ->
                        snackbarHostState.showSnackbar(message)
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF007A3D)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ShoppingCart,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(17.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "SmartShopper",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 18.sp
                                        )
                                        if (userLocation.isConfigured) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    Icons.Default.LocationOn,
                                                    contentDescription = null,
                                                    tint = Color(0xFF007A3D),
                                                    modifier = Modifier.size(10.dp)
                                                )
                                                Spacer(modifier = Modifier.width(2.dp))
                                                Text(
                                                    text = userLocation.areaName,
                                                    fontSize = 10.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    maxLines = 1
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                            actions = {
                                if (userLocation.isConfigured) {
                                    // Live Budget Remaining Balance Pill
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (budgetSummary.isOverBudget) {
                                            Color(0xFFFFEBEE)
                                        } else {
                                            MaterialTheme.colorScheme.primaryContainer
                                        },
                                        modifier = Modifier.padding(end = 12.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.AccountBalanceWallet,
                                                contentDescription = null,
                                                tint = if (budgetSummary.isOverBudget) Color(0xFFD32F2F) else Color(0xFF007A3D),
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = String.format("R %.2f", budgetSummary.remainingBalance),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = if (budgetSummary.isOverBudget) Color(0xFFD32F2F) else MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }
                                } else {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFFE8F5E9),
                                        modifier = Modifier.padding(end = 12.dp)
                                    ) {
                                        Text(
                                            text = "South Africa 🇿🇦",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF007A3D),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    },
                    bottomBar = {
                        if (userLocation.isConfigured) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                tonalElevation = 6.dp,
                                modifier = Modifier.testTag("bottom_nav_bar")
                            ) {
                                // 1. Store Catalogues
                                NavigationBarItem(
                                    selected = currentScreen == AppScreen.STORE_CATALOGUES,
                                    onClick = { viewModel.navigateTo(AppScreen.STORE_CATALOGUES) },
                                    icon = {
                                        BadgedBox(badge = {
                                            if (downloadedStores.isNotEmpty()) {
                                                Badge(containerColor = Color(0xFF007A3D)) {
                                                    Text("${downloadedStores.size}")
                                                }
                                            }
                                        }) {
                                            Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Catalogues")
                                        }
                                    },
                                    label = { Text("Catalogues") },
                                    modifier = Modifier.testTag("nav_catalogues")
                                )

                                // 2. Shopping List & Budget
                                NavigationBarItem(
                                    selected = currentScreen == AppScreen.SHOPPING_LIST,
                                    onClick = { viewModel.navigateTo(AppScreen.SHOPPING_LIST) },
                                    icon = {
                                        BadgedBox(badge = {
                                            if (shoppingList.isNotEmpty()) {
                                                Badge { Text("${shoppingList.size}") }
                                            }
                                        }) {
                                            Icon(Icons.Default.ShoppingCart, contentDescription = "Shopping List")
                                        }
                                    },
                                    label = { Text("Cart & Budget") },
                                    modifier = Modifier.testTag("nav_shopping_list")
                                )

                                // 3. Loyalty Cards
                                NavigationBarItem(
                                    selected = currentScreen == AppScreen.LOYALTY_CARDS,
                                    onClick = { viewModel.navigateTo(AppScreen.LOYALTY_CARDS) },
                                    icon = {
                                        Icon(Icons.Default.Loyalty, contentDescription = "Loyalty Cards")
                                    },
                                    label = { Text("Loyalty") },
                                    modifier = Modifier.testTag("nav_loyalty")
                                )

                                // 4. Nearby Stores
                                NavigationBarItem(
                                    selected = currentScreen == AppScreen.NEARBY_STORES,
                                    onClick = { viewModel.navigateTo(AppScreen.NEARBY_STORES) },
                                    icon = {
                                        Icon(Icons.Default.NearMe, contentDescription = "Nearby")
                                    },
                                    label = { Text("Nearby") },
                                    modifier = Modifier.testTag("nav_nearby")
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        if (!userLocation.isConfigured) {
                            // The app CANNOT start with data already there.
                            // The user MUST enter the location, then download the store and start shopping!
                            LocationDiscoveryScreen(
                                onLocationSelected = { lat, lng, area ->
                                    viewModel.setUserLocation(lat, lng, area)
                                },
                                onDetectGps = {
                                    viewModel.detectDeviceLocation()
                                }
                            )
                        } else {
                            when (currentScreen) {
                                AppScreen.STORE_CATALOGUES -> StoreCatalogueScreen(viewModel = viewModel)
                                AppScreen.SHOPPING_LIST -> ShoppingListScreen(viewModel = viewModel)
                                AppScreen.LOYALTY_CARDS -> LoyaltyCardsScreen(viewModel = viewModel)
                                AppScreen.NEARBY_STORES -> NearbyStoresScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
