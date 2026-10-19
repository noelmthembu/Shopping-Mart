package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LoyaltyCard
import com.example.ui.ShoppingViewModel
import com.example.ui.components.LoyaltyCardVisual

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoyaltyCardsScreen(
    viewModel: ShoppingViewModel,
    modifier: Modifier = Modifier
) {
    val loyaltyCards by viewModel.loyaltyCards.collectAsState()
    val allStores by viewModel.allStores.collectAsState()
    var showAddCardDialog by remember { mutableStateOf(false) }

    val totalPoints = remember(loyaltyCards) { loyaltyCards.sumOf { it.pointsBalance } }
    val linkedCardsCount = remember(loyaltyCards) { loyaltyCards.count { it.isLinked } }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddCardDialog = true },
                icon = { Icon(Icons.Default.AddCard, contentDescription = null) },
                text = { Text("Add SA Card") },
                modifier = Modifier.testTag("add_loyalty_card_fab")
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Points Summary
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
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
                            Column {
                                Text(
                                    text = "SA Rewards & Club Wallet",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Text(
                                    text = "$linkedCardsCount active cards saving in cart",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFF57F17),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "$totalPoints Total Pts",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "💡 Swipe at checkout: Tap any card to display its scannable barcode for the cashier. Linking cards automatically applies Checkers Xtra Savings, Pick n Pay Smart Shopper, and Woolworths WRewards member prices to your cart balance!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // Cards List
            if (loyaltyCards.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
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
                                imageVector = Icons.Default.Loyalty,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No Loyalty Cards Added",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Add your Checkers Xtra Savings, Pick n Pay Smart Shopper, or Clicks ClubCard to unlock South African till discounts.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(loyaltyCards, key = { it.id }) { card ->
                    LoyaltyCardVisual(
                        card = card,
                        onToggleLinked = { viewModel.toggleCardLinked(card.id, it) },
                        onDelete = { viewModel.deleteLoyaltyCard(card.id) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    if (showAddCardDialog) {
        AddLoyaltyCardDialog(
            stores = allStores,
            onDismiss = { showAddCardDialog = false },
            onAdd = { storeId, storeName, programName, cardholder, cardNumber, tier ->
                viewModel.addLoyaltyCard(storeId, storeName, programName, cardholder, cardNumber, tier)
                showAddCardDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLoyaltyCardDialog(
    stores: List<com.example.data.model.Store>,
    onDismiss: () -> Unit,
    onAdd: (String, String, String, String, String, String) -> Unit
) {
    var selectedStoreIndex by remember { mutableStateOf(0) }
    var expandedStoreDropdown by remember { mutableStateOf(false) }

    val currentStore = stores.getOrNull(selectedStoreIndex) ?: stores.firstOrNull()
    var programName by remember(currentStore) { mutableStateOf(currentStore?.loyaltyProgramName ?: "Xtra Savings") }
    var cardholder by remember { mutableStateOf("Sipho Dlamini") }
    var cardNumber by remember { mutableStateOf("9820 4410 7731 0029") }
    var tier by remember { mutableStateOf("VIP Member") }

    val tiers = listOf("Standard", "Silver", "Gold", "VIP Member")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add South African Loyalty Card", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (stores.isNotEmpty()) {
                    ExposedDropdownMenuBox(
                        expanded = expandedStoreDropdown,
                        onExpandedChange = { expandedStoreDropdown = !expandedStoreDropdown }
                    ) {
                        OutlinedTextField(
                            value = currentStore?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Store") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStoreDropdown) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedStoreDropdown,
                            onDismissRequest = { expandedStoreDropdown = false }
                        ) {
                            stores.forEachIndexed { index, store ->
                                DropdownMenuItem(
                                    text = { Text(store.name) },
                                    onClick = {
                                        selectedStoreIndex = index
                                        programName = store.loyaltyProgramName
                                        expandedStoreDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = programName,
                    onValueChange = { programName = it },
                    label = { Text("Loyalty Scheme (e.g. Xtra Savings, Smart Shopper)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("program_name_input")
                )

                OutlinedTextField(
                    value = cardholder,
                    onValueChange = { cardholder = it },
                    label = { Text("Cardholder Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("cardholder_input")
                )

                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { cardNumber = it },
                    label = { Text("Card / Barcode Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("card_number_input")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    tiers.forEach { t ->
                        androidx.compose.material3.FilterChip(
                            selected = tier == t,
                            onClick = { tier = t },
                            label = { Text(t, fontSize = 11.sp) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (currentStore != null && cardNumber.isNotBlank()) {
                        onAdd(currentStore.id, currentStore.name, programName, cardholder, cardNumber, tier)
                    }
                },
                enabled = currentStore != null && cardNumber.isNotBlank(),
                modifier = Modifier.testTag("save_card_button")
            ) {
                Text("Save Card")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
