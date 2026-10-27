package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LoyaltyCard
import com.example.data.model.Store
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
    var editingCard by remember { mutableStateOf<LoyaltyCard?>(null) }

    val totalPoints = remember(loyaltyCards) { loyaltyCards.sumOf { it.pointsBalance } }
    val linkedCardsCount = remember(loyaltyCards) { loyaltyCards.count { it.isLinked } }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    editingCard = null
                    showAddCardDialog = true
                },
                icon = { Icon(Icons.Default.AddCard, contentDescription = null) },
                text = { Text("Link Actual Card") },
                containerColor = Color(0xFF007A3D),
                contentColor = Color.White,
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
            // Header Points Summary & Link Card Quick Action
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
                            text = "Link your actual plastic or digital store cards to automatically unlock Checkers Xtra Savings, Pick n Pay Smart Shopper, Woolworths WRewards and SPAR Rewards specials in your cart balance.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.9f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                editingCard = null
                                showAddCardDialog = true
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007A3D)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("link_my_actual_card_btn")
                        ) {
                            Icon(Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Link My Actual Card", fontWeight = FontWeight.Bold)
                        }
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
                                text = "Link your actual Checkers Xtra Savings, Pick n Pay Smart Shopper, or Clicks ClubCard to unlock South African till discounts.",
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
                        onDelete = { viewModel.deleteLoyaltyCard(card.id) },
                        onEditOrLinkActualCard = {
                            editingCard = card
                            showAddCardDialog = true
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    if (showAddCardDialog) {
        ActualLoyaltyCardDialog(
            stores = allStores,
            existingCard = editingCard,
            onDismiss = {
                showAddCardDialog = false
                editingCard = null
            },
            onSave = { storeId, storeName, programName, cardholder, cardNumber, barcode, tier ->
                if (editingCard != null) {
                    viewModel.linkActualCard(editingCard!!.id, cardNumber, barcode, cardholder)
                } else {
                    viewModel.addActualLoyaltyCard(storeId, storeName, programName, cardholder, cardNumber, barcode, tier)
                }
                showAddCardDialog = false
                editingCard = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActualLoyaltyCardDialog(
    stores: List<Store>,
    existingCard: LoyaltyCard? = null,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String, String, String) -> Unit
) {
    var selectedStoreIndex by remember {
        val initialIdx = if (existingCard != null) {
            stores.indexOfFirst { it.id == existingCard.storeId }.coerceAtLeast(0)
        } else {
            0
        }
        mutableStateOf(initialIdx)
    }
    var expandedStoreDropdown by remember { mutableStateOf(false) }

    val currentStore = stores.getOrNull(selectedStoreIndex) ?: stores.firstOrNull()

    var programName by remember(currentStore, existingCard) {
        mutableStateOf(existingCard?.programName ?: currentStore?.loyaltyProgramName ?: "Xtra Savings")
    }
    var cardholder by remember(existingCard) {
        mutableStateOf(existingCard?.cardholderName ?: "")
    }
    var cardNumber by remember(existingCard) {
        mutableStateOf(existingCard?.cardNumber ?: "")
    }
    var barcodeNumber by remember(existingCard) {
        mutableStateOf(existingCard?.barcodeNumber ?: "")
    }
    var tier by remember(existingCard) {
        mutableStateOf(existingCard?.membershipTier ?: "VIP Member")
    }

    val tiers = listOf("Standard", "Silver", "Gold", "VIP Member")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (existingCard != null) "Link Actual ${existingCard.programName} Card" else "Link Your Actual Card",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Enter your real loyalty card number and barcode from your physical card or app to activate automatic cart savings.",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                if (stores.isNotEmpty() && existingCard == null) {
                    ExposedDropdownMenuBox(
                        expanded = expandedStoreDropdown,
                        onExpandedChange = { expandedStoreDropdown = !expandedStoreDropdown }
                    ) {
                        OutlinedTextField(
                            value = currentStore?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Retail Store") },
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
                                    text = { Text("${store.name} (${store.loyaltyProgramName})") },
                                    onClick = {
                                        selectedStoreIndex = index
                                        programName = store.loyaltyProgramName
                                        expandedStoreDropdown = false
                                    }
                                )
                            }
                        }
                    }
                } else if (existingCard != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF1F5F9),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = existingCard.storeName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Program: ${existingCard.programName}",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = cardholder,
                    onValueChange = { cardholder = it },
                    label = { Text("Cardholder Name") },
                    placeholder = { Text("e.g. Sipho Dlamini") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("cardholder_input")
                )

                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { input ->
                        cardNumber = input
                        if (barcodeNumber.isBlank() || barcodeNumber == cardNumber.filter { it.isDigit() }.dropLast(1)) {
                            barcodeNumber = input.filter { it.isDigit() }
                        }
                    },
                    label = { Text("Actual Card Number") },
                    placeholder = { Text("e.g. 9820 4410 7731 0029") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("card_number_input")
                )

                OutlinedTextField(
                    value = barcodeNumber,
                    onValueChange = { barcodeNumber = it },
                    label = { Text("Barcode String (for Cashier Scanning)") },
                    placeholder = { Text("e.g. 982044107731") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("barcode_input")
                )

                Text(
                    text = "Membership Level:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF475569)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    tiers.forEach { t ->
                        FilterChip(
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
                    val storeId = existingCard?.storeId ?: currentStore?.id ?: ""
                    val storeName = existingCard?.storeName ?: currentStore?.name ?: ""
                    val finalProg = programName.ifBlank { "Rewards" }
                    val finalHolder = cardholder.ifBlank { "Cardholder" }
                    if (storeId.isNotBlank() && cardNumber.isNotBlank()) {
                        onSave(storeId, storeName, finalProg, finalHolder, cardNumber, barcodeNumber, tier)
                    }
                },
                enabled = cardNumber.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007A3D)),
                modifier = Modifier.testTag("save_card_button")
            ) {
                Text("Link & Save Card")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
