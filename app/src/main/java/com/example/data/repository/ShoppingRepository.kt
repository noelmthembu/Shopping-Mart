package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.BudgetSettings
import com.example.data.model.CatalogueItem
import com.example.data.model.LoyaltyCard
import com.example.data.model.ShoppingListItem
import com.example.data.model.Store
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class ShoppingRepository(private val db: AppDatabase) {
    private val storeDao = db.storeDao()
    private val catalogueItemDao = db.catalogueItemDao()
    private val shoppingListDao = db.shoppingListDao()
    private val loyaltyCardDao = db.loyaltyCardDao()
    private val budgetSettingsDao = db.budgetSettingsDao()

    val allStores: Flow<List<Store>> = storeDao.getAllStores()
    val downloadedStores: Flow<List<Store>> = storeDao.getDownloadedStores()
    val shoppingList: Flow<List<ShoppingListItem>> = shoppingListDao.getAllItems()
    val loyaltyCards: Flow<List<LoyaltyCard>> = loyaltyCardDao.getAllCards()
    val budgetSettings: Flow<BudgetSettings?> = budgetSettingsDao.getSettings()

    fun getItemsForStore(storeId: String): Flow<List<CatalogueItem>> =
        catalogueItemDao.getItemsForStore(storeId)

    fun searchItems(storeId: String, query: String): Flow<List<CatalogueItem>> =
        catalogueItemDao.searchItems(storeId, query)

    fun getWeeklyDeals(): Flow<List<CatalogueItem>> =
        catalogueItemDao.getWeeklyDeals()

    suspend fun getStoreById(storeId: String): Store? =
        storeDao.getStoreById(storeId)

    suspend fun isCardLinkedForStore(storeId: String): Boolean {
        val card = loyaltyCardDao.getCardForStore(storeId)
        return card != null && card.isLinked
    }

    suspend fun getCardForStore(storeId: String): LoyaltyCard? =
        loyaltyCardDao.getCardForStore(storeId)

    suspend fun initializeSeedDataIfNeeded() = withContext(Dispatchers.IO) {
        val storeCount = storeDao.getCount()
        if (storeCount == 0) {
            val initialStores = SampleStoreData.stores
            storeDao.insertStores(initialStores)

            // Seed initial South African loyalty cards
            val initialCards = listOf(
                LoyaltyCard(
                    id = "card_checkers",
                    storeId = "checkers_01",
                    storeName = "Checkers Hyper Sandton",
                    programName = "Xtra Savings",
                    cardholderName = "Sipho Dlamini",
                    cardNumber = "9820 4410 7731 0029",
                    barcodeNumber = "982044107731",
                    pointsBalance = 620,
                    membershipTier = "VIP Member",
                    isLinked = true,
                    colorHex = 0xFF007A3D // Checkers Green
                ),
                LoyaltyCard(
                    id = "card_pnp",
                    storeId = "pnp_02",
                    storeName = "Pick n Pay Rosebank",
                    programName = "Smart Shopper",
                    cardholderName = "Sipho Dlamini",
                    cardNumber = "4401 9832 1190 5543",
                    barcodeNumber = "440198321190",
                    pointsBalance = 350,
                    membershipTier = "Gold",
                    isLinked = true,
                    colorHex = 0xFF002F6C // PnP Blue
                ),
                LoyaltyCard(
                    id = "card_woolies",
                    storeId = "woolworths_03",
                    storeName = "Woolworths Food Hyde Park",
                    programName = "WRewards",
                    cardholderName = "Sipho Dlamini",
                    cardNumber = "8890 1204 3392 4110",
                    barcodeNumber = "889012043392",
                    pointsBalance = 1120,
                    membershipTier = "Green Tier",
                    isLinked = false,
                    colorHex = 0xFF1A1A1A // Woolies Black
                ),
                LoyaltyCard(
                    id = "card_clicks",
                    storeId = "clicks_07",
                    storeName = "Clicks Sandton City",
                    programName = "ClubCard",
                    cardholderName = "Sipho Dlamini",
                    cardNumber = "6007 1928 3341 9901",
                    barcodeNumber = "600719283341",
                    pointsBalance = 480,
                    membershipTier = "Platinum",
                    isLinked = true,
                    colorHex = 0xFF0083C5 // Clicks Blue
                )
            )
            loyaltyCardDao.insertCards(initialCards)

            // Initialize default South African budget in Rands (R1,500.00)
            budgetSettingsDao.saveSettings(
                BudgetSettings(
                    id = 1,
                    totalBudget = 1500.00,
                    currencySymbol = "R",
                    alertThresholdPercent = 85,
                    taxRatePercent = 15.0
                )
            )
            // No catalogue items pre-cached and NO shopping list items pre-added.
            // The user must first enter location, download a store catalogue, and start shopping!
        }
    }

    suspend fun downloadStoreCatalogue(storeId: String): Boolean = withContext(Dispatchers.IO) {
        val store = storeDao.getStoreById(storeId) ?: return@withContext false
        val items = SampleStoreData.getCatalogueForStore(storeId)
        catalogueItemDao.deleteItemsForStore(storeId)
        catalogueItemDao.insertItems(items)
        storeDao.setStoreDownloaded(storeId, true, System.currentTimeMillis())
        storeDao.updateStore(store.copy(isDownloaded = true, downloadedAt = System.currentTimeMillis(), totalItemsCount = items.size))
        true
    }

    suspend fun removeOfflineStoreCatalogue(storeId: String) = withContext(Dispatchers.IO) {
        catalogueItemDao.deleteItemsForStore(storeId)
        val store = storeDao.getStoreById(storeId)
        if (store != null) {
            storeDao.updateStore(store.copy(isDownloaded = false, downloadedAt = 0L))
        }
    }

    suspend fun addItemToShoppingList(
        catalogueItem: CatalogueItem,
        store: Store?
    ) = withContext(Dispatchers.IO) {
        val hasCard = if (store != null) isCardLinkedForStore(store.id) else false
        val effectivePrice = if (hasCard) catalogueItem.cardDiscountPrice else catalogueItem.regularPrice
        val item = ShoppingListItem(
            storeId = store?.id,
            storeName = store?.name,
            name = catalogueItem.name,
            category = catalogueItem.category,
            regularPrice = catalogueItem.regularPrice,
            effectivePrice = effectivePrice,
            quantity = 1,
            unit = catalogueItem.unit,
            isLoyaltyDiscountApplied = hasCard && catalogueItem.cardDiscountPrice < catalogueItem.regularPrice,
            notes = catalogueItem.promoTag,
            imageType = catalogueItem.imageType,
            imageUrl = catalogueItem.imageUrl
        )
        shoppingListDao.insertItem(item)
    }

    suspend fun addCustomItem(
        name: String,
        price: Double,
        quantity: Int,
        category: String,
        storeName: String?,
        notes: String
    ) = withContext(Dispatchers.IO) {
        val item = ShoppingListItem(
            storeId = null,
            storeName = storeName.takeIf { !it.isNullOrBlank() },
            name = name.trim(),
            category = category.ifBlank { "General" },
            regularPrice = price,
            effectivePrice = price,
            quantity = quantity.coerceAtLeast(1),
            unit = "each",
            isLoyaltyDiscountApplied = false,
            notes = notes.trim(),
            imageType = name.lowercase(),
            imageUrl = ""
        )
        shoppingListDao.insertItem(item)
    }

    suspend fun updateItemQuantity(itemId: Long, quantity: Int) = withContext(Dispatchers.IO) {
        if (quantity <= 0) {
            shoppingListDao.deleteItem(itemId)
        } else {
            shoppingListDao.updateQuantity(itemId, quantity)
        }
    }

    suspend fun togglePurchased(itemId: Long, currentStatus: Boolean) = withContext(Dispatchers.IO) {
        shoppingListDao.setPurchased(itemId, !currentStatus)
    }

    suspend fun deleteShoppingItem(itemId: Long) = withContext(Dispatchers.IO) {
        shoppingListDao.deleteItem(itemId)
    }

    suspend fun clearPurchasedItems() = withContext(Dispatchers.IO) {
        shoppingListDao.clearPurchased()
    }

    suspend fun clearAllShoppingItems() = withContext(Dispatchers.IO) {
        shoppingListDao.clearAll()
    }

    suspend fun saveBudget(totalBudget: Double) = withContext(Dispatchers.IO) {
        val current = budgetSettingsDao.getSettingsSync() ?: BudgetSettings()
        budgetSettingsDao.saveSettings(current.copy(totalBudget = totalBudget))
    }

    suspend fun toggleLoyaltyCardLinked(cardId: String, isLinked: Boolean) = withContext(Dispatchers.IO) {
        loyaltyCardDao.setCardLinked(cardId, isLinked)
        val card = loyaltyCards.firstOrNull()?.find { it.id == cardId } ?: return@withContext
        refreshShoppingListPricesForStore(card.storeId, isLinked)
    }

    private suspend fun refreshShoppingListPricesForStore(storeId: String, isLinked: Boolean) {
        val items = shoppingListDao.getAllItems().firstOrNull() ?: return
        for (item in items) {
            if (item.storeId == storeId) {
                val catalogueItem = catalogueItemDao.getItemsForStore(storeId).firstOrNull()?.find { it.name == item.name }
                if (catalogueItem != null) {
                    val newPrice = if (isLinked) catalogueItem.cardDiscountPrice else catalogueItem.regularPrice
                    val updated = item.copy(
                        effectivePrice = newPrice,
                        isLoyaltyDiscountApplied = isLinked && catalogueItem.cardDiscountPrice < catalogueItem.regularPrice
                    )
                    shoppingListDao.updateItem(updated)
                }
            }
        }
    }

    suspend fun addLoyaltyCard(card: LoyaltyCard) = withContext(Dispatchers.IO) {
        loyaltyCardDao.insertCard(card)
        if (card.isLinked) {
            refreshShoppingListPricesForStore(card.storeId, true)
        }
    }

    suspend fun deleteLoyaltyCard(cardId: String) = withContext(Dispatchers.IO) {
        loyaltyCardDao.deleteCard(cardId)
    }

    suspend fun completeCheckoutAndEarnPoints(storeId: String, totalSpent: Double): Int = withContext(Dispatchers.IO) {
        val store = storeDao.getStoreById(storeId) ?: return@withContext 0
        val pointsMultiplier = store.pointsPerRand
        val earned = (totalSpent * pointsMultiplier).toInt()
        if (earned > 0) {
            loyaltyCardDao.addPointsToStoreCard(storeId, earned)
        }
        earned
    }
}
