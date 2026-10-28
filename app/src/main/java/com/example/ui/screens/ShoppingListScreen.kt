package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ShoppingListItem
import com.example.ui.AppScreen
import com.example.ui.ShoppingViewModel
import com.example.ui.components.BudgetSummaryCard
import com.example.ui.components.CartItemRow
import com.example.ui.components.LocationSetupCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    viewModel: ShoppingViewModel,
    modifier: Modifier = Modifier
) {
    val items by viewModel.shoppingList.collectAsState()
    val summary by viewModel.budgetSummary.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()
    val downloadedStores by viewModel.downloadedStores.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var filterTab by remember { mutableStateOf("ALL") }

    val filteredItems = remember(items, filterTab) {
        when (filterTab) {
            "TO_BUY" -> items.filter { !it.isPurchased }
            "PURCHASED" -> items.filter { it.isPurchased }
            else -> items
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            if (downloadedStores.isNotEmpty() || userLocation.isConfigured) {
                ExtendedFloatingActionButton(
                    onClick = { showAddDialog = true },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Add Custom Item") },
                    modifier = Modifier.testTag("add_item_fab")
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // STEP 1: If location is NOT configured, prompt user to enter location first!
            if (!userLocation.isConfigured) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    LocationSetupCard(
                        onLocationSelected = { lat, lng, area ->
                            viewModel.setUserLocation(lat, lng, area)
                        },
                        onDetectGps = {
                            viewModel.detectDeviceLocation()
                        }
                    )
                }

                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "How SmartShopper Works:",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "1. Choose a listed location or use GPS\n2. Browse sample store offers and confirm current prices in store\n3. Track your budget and linked loyalty discounts",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            } else if (downloadedStores.isEmpty()) {
                // STEP 2: Location is set, but no stores downloaded yet!
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = userLocation.areaName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF007A3D).copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "STEP 2 OF 3",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF005027),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Download a Store to Start Shopping",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Download the full catalogue for Checkers, Pick n Pay, Woolworths, Shoprite, or Makro to start shopping completely offline.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.9f)
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = { viewModel.navigateTo(AppScreen.STORE_CATALOGUES) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("download_first_store_button")
                            ) {
                                Icon(Icons.Default.CloudDownload, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Browse & Download Store Catalogues", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                // STEP 3: Store(s) downloaded! Shopping & budget tracking active
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    BudgetSummaryCard(
                        summary = summary,
                        onUpdateBudget = { viewModel.updateBudget(it) }
                    )
                }

                // Filter & Done actions
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(
                                selected = filterTab == "ALL",
                                onClick = { filterTab = "ALL" },
                                label = { Text("All (${items.size})") }
                            )
                            FilterChip(
                                selected = filterTab == "TO_BUY",
                                onClick = { filterTab = "TO_BUY" },
                                label = { Text("To Buy (${items.count { !it.isPurchased }})") }
                            )
                            FilterChip(
                                selected = filterTab == "PURCHASED",
                                onClick = { filterTab = "PURCHASED" },
                                label = { Text("Checked (${items.count { it.isPurchased }})") }
                            )
                        }

                        if (items.any { it.isPurchased }) {
                            OutlinedButton(
                                onClick = { viewModel.clearPurchased() },
                                modifier = Modifier.testTag("clear_purchased_button"),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.CleaningServices, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Clear Done", fontSize = 11.sp)
                            }
                        }
                    }
                }

                // Complete Trip Banner if items are purchased
                if (items.any { it.isPurchased }) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Ready to Checkout at Till?",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = "${items.count { it.isPurchased }} items scanned • Earn +${summary.estimatedPointsToEarn} loyalty points",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                    )
                                }
                                Button(
                                    onClick = { viewModel.completeCheckout() },
                                    modifier = Modifier.testTag("complete_trip_button")
                                ) {
                                    Text("Complete Trip", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Shopping List Items or Empty State
                if (filteredItems.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_deals_specials),
                                    contentDescription = "Shopping Catalogue",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(130.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "Your Cart is Empty",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "You have ${downloadedStores.size} store catalogue(s) ready offline. Browse items below to start adding to your cart and tracking your budget balance.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = { viewModel.navigateTo(AppScreen.STORE_CATALOGUES) },
                                    modifier = Modifier.testTag("browse_catalogue_cta")
                                ) {
                                    Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Browse Downloaded Catalogues")
                                }
                            }
                        }
                    }
                } else {
                    items(
                        items = filteredItems,
                        key = { it.id }
                    ) { item ->
                        CartItemRow(
                            item = item,
                            onTogglePurchased = { viewModel.togglePurchased(item.id, item.isPurchased) },
                            onQuantityChange = { viewModel.updateQuantity(item.id, it) },
                            onDelete = { viewModel.deleteItem(item.id) }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    if (showAddDialog) {
        AddCustomItemDialog(
            onDismiss = { showAddDialog = false },
            onAddItem = { name, price, qty, category, store, notes ->
                viewModel.addCustomItem(name, price, qty, category, store, notes)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AddCustomItemDialog(
    onDismiss: () -> Unit,
    onAddItem: (String, Double, Int, String, String?, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf(1) }
    var category by remember { mutableStateOf("Groceries") }
    var storeName by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Custom Shopping Item", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Item Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("custom_item_name_input")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text("Unit Price (R) *") },
                        leadingIcon = { Text("R", fontWeight = FontWeight.Bold) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("custom_item_price_input")
                    )

                    OutlinedTextField(
                        value = quantity.toString(),
                        onValueChange = { quantity = it.toIntOrNull()?.coerceAtLeast(1) ?: 1 },
                        label = { Text("Qty") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.width(80.dp).testTag("custom_item_qty_input")
                    )
                }

                OutlinedTextField(
                    value = storeName,
                    onValueChange = { storeName = it },
                    label = { Text("Store Name (e.g. Checkers, Pick n Pay)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("custom_item_store_input")
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (e.g. braai special)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val price = priceText.toDoubleOrNull()
                    if (name.isNotBlank() && price != null && price >= 0) {
                        onAddItem(name, price, quantity, category, storeName.takeIf { it.isNotBlank() }, notes)
                    }
                },
                enabled = name.isNotBlank() && priceText.toDoubleOrNull() != null,
                modifier = Modifier.testTag("save_custom_item_button")
            ) {
                Text("Add to List")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
