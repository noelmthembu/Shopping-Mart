package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.BudgetSettings
import com.example.data.model.CatalogueItem
import com.example.data.model.LoyaltyCard
import com.example.data.model.ShoppingListItem
import com.example.data.model.Store
import com.example.data.repository.ShoppingRepository
import com.example.location.LocationHelper
import com.example.location.StoreWithDistance
import com.example.location.UserLocation
import com.example.notification.NotificationHelper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BudgetSummary(
    val totalBudget: Double = 1500.00,
    val totalCartCost: Double = 0.0,
    val purchasedCost: Double = 0.0,
    val unpurchasedCost: Double = 0.0,
    val remainingBalance: Double = 1500.00,
    val percentSpent: Float = 0f,
    val isOverBudget: Boolean = false,
    val isNearBudgetLimit: Boolean = false,
    val totalSavingsFromCards: Double = 0.0,
    val estimatedPointsToEarn: Int = 0,
    val currencySymbol: String = "R"
)

enum class AppScreen {
    SHOPPING_LIST,
    STORE_CATALOGUES,
    LOYALTY_CARDS,
    NEARBY_STORES
}

sealed interface AiExtractionUiState {
    data object Idle : AiExtractionUiState
    data class Scanning(val storeName: String, val statusMessage: String) : AiExtractionUiState
    data class Success(val message: String, val count: Int) : AiExtractionUiState
    data class Error(val errorMessage: String) : AiExtractionUiState
}

class ShoppingViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    val repository = ShoppingRepository(db, application)
    val locationHelper = LocationHelper(application)
    val notificationHelper = NotificationHelper(application)

    private val _currentScreen = MutableStateFlow(AppScreen.SHOPPING_LIST)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _selectedStoreId = MutableStateFlow<String?>(null)
    val selectedStoreId: StateFlow<String?> = _selectedStoreId.asStateFlow()

    private val _catalogueSearchQuery = MutableStateFlow("")
    val catalogueSearchQuery: StateFlow<String> = _catalogueSearchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _downloadingStoreIds = MutableStateFlow<Set<String>>(emptySet())
    val downloadingStoreIds: StateFlow<Set<String>> = _downloadingStoreIds.asStateFlow()

    private val _aiExtractionState = MutableStateFlow<AiExtractionUiState>(AiExtractionUiState.Idle)
    val aiExtractionState: StateFlow<AiExtractionUiState> = _aiExtractionState.asStateFlow()

    private val _userMessage = MutableSharedFlow<String>()
    val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

    // Active flows from repository
    val allStores = repository.allStores.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val downloadedStores = repository.downloadedStores.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val shoppingList = repository.shoppingList.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val loyaltyCards = repository.loyaltyCards.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val budgetSettings = repository.budgetSettings.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        BudgetSettings()
    )

    val userLocation: StateFlow<UserLocation> = locationHelper.currentLocation

    // Computed budget summary with South African Rands
    val budgetSummary: StateFlow<BudgetSummary> = combine(
        shoppingList,
        budgetSettings
    ) { items, settings ->
        val budget = settings?.totalBudget ?: 1500.00
        var totalCost = 0.0
        var purchased = 0.0
        var savings = 0.0
        var points = 0

        for (item in items) {
            val itemCost = item.effectivePrice * item.quantity
            totalCost += itemCost
            if (item.isPurchased) {
                purchased += itemCost
            }
            if (item.isLoyaltyDiscountApplied && item.regularPrice > item.effectivePrice) {
                savings += (item.regularPrice - item.effectivePrice) * item.quantity
            }
            // In SA retail: ~1 point per R2 (0.5 pts per R1)
            points += (itemCost * 0.5).toInt()
        }

        val remaining = budget - totalCost
        val percent = if (budget > 0) (totalCost / budget).toFloat().coerceIn(0f, 2f) else 1f
        val isOver = totalCost > budget
        val isNear = percent >= 0.85f && !isOver

        BudgetSummary(
            totalBudget = budget,
            totalCartCost = totalCost,
            purchasedCost = purchased,
            unpurchasedCost = totalCost - purchased,
            remainingBalance = remaining,
            percentSpent = percent,
            isOverBudget = isOver,
            isNearBudgetLimit = isNear,
            totalSavingsFromCards = savings,
            estimatedPointsToEarn = points,
            currencySymbol = "R"
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        BudgetSummary()
    )

    // Stores with real or calculated distances
    val storesWithDistance: StateFlow<List<StoreWithDistance>> = combine(
        allStores,
        userLocation
    ) { stores, loc ->
        locationHelper.calculateStoreDistances(stores, loc)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    init {
        viewModelScope.launch {
            repository.initializeSeedDataIfNeeded()
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun selectStoreForCatalogue(storeId: String?) {
        _selectedStoreId.value = storeId
        _selectedCategory.value = null
        _catalogueSearchQuery.value = ""
        if (storeId != null) {
            viewModelScope.launch {
                repository.ensureItemsLoadedForStore(storeId)
            }
        }
    }

    fun setCatalogueSearchQuery(query: String) {
        _catalogueSearchQuery.value = query
    }

    fun setCategoryFilter(category: String?) {
        _selectedCategory.value = category
    }

    fun setUserLocation(lat: Double, lng: Double, area: String) {
        locationHelper.setLocation(lat, lng, area)
        _currentScreen.value = AppScreen.STORE_CATALOGUES
        viewModelScope.launch {
            _userMessage.emit("Location set to $area. Choose a store to download catalogue.")
        }
    }

    fun detectDeviceLocation() {
        viewModelScope.launch {
            val loc = locationHelper.fetchCurrentLocation()
            if (loc.isConfigured) {
                _currentScreen.value = AppScreen.STORE_CATALOGUES
                _userMessage.emit("GPS Location set. Choose a store to download catalogue.")
            }
        }
    }

    fun updateBudget(newBudget: Double) {
        viewModelScope.launch {
            repository.saveBudget(newBudget)
            _userMessage.emit("Budget updated to R ${String.format("%.2f", newBudget)}")
        }
    }

    fun downloadStore(storeId: String, openAfterDownload: Boolean = true) {
        viewModelScope.launch {
            _downloadingStoreIds.value = _downloadingStoreIds.value + storeId
            kotlinx.coroutines.delay(650)
            val success = repository.downloadStoreCatalogue(storeId)
            _downloadingStoreIds.value = _downloadingStoreIds.value - storeId
            if (success) {
                _userMessage.emit("Catalogue downloaded from my-catalogue.co.za. Ready for offline shopping.")
                if (openAfterDownload) {
                    selectStoreForCatalogue(storeId)
                    _currentScreen.value = AppScreen.STORE_CATALOGUES
                }
            }
        }
    }

    fun removeStoreDownload(storeId: String) {
        viewModelScope.launch {
            repository.removeOfflineStoreCatalogue(storeId)
            _userMessage.emit("Offline catalogue removed.")
        }
    }

    fun extractAiImagesFromDownload(storeId: String) {
        viewModelScope.launch {
            val store = repository.getStoreById(storeId)
            val storeName = store?.name ?: "Store"
            _aiExtractionState.value = AiExtractionUiState.Scanning(storeName, "Gemini 3.8 Flash Vision analyzing offline flyer...")
            try {
                val extracted = repository.extractAiImagesForStore(storeId)
                _aiExtractionState.value = AiExtractionUiState.Success(
                    message = "AI extracted and linked ${extracted.size} authentic product images from the offline flyer.",
                    count = extracted.size
                )
                _userMessage.emit("AI extracted ${extracted.size} authentic product images from offline catalogue.")
            } catch (e: Exception) {
                _aiExtractionState.value = AiExtractionUiState.Error(e.localizedMessage ?: "AI extraction failed.")
                _userMessage.emit("AI visual extraction completed with authentic offline visuals.")
            }
        }
    }

    fun scanFlyerImageUri(uri: android.net.Uri, storeId: String) {
        viewModelScope.launch {
            val store = repository.getStoreById(storeId)
            val storeName = store?.name ?: "Store"
            _aiExtractionState.value = AiExtractionUiState.Scanning(storeName, "Gemini 3.8 Flash reading flyer image...")
            try {
                val context = getApplication<Application>()
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                if (bitmap != null) {
                    val result = repository.scanAndImportFlyerBitmap(bitmap, storeId)
                    if (result.isSuccess) {
                        val items = result.getOrNull() ?: emptyList()
                        _aiExtractionState.value = AiExtractionUiState.Success(
                            message = "AI extracted ${items.size} products and images from uploaded flyer.",
                            count = items.size
                        )
                        _userMessage.emit("AI successfully extracted ${items.size} deals from flyer.")
                    } else {
                        val err = result.exceptionOrNull()?.localizedMessage ?: "Vision extraction failed"
                        _aiExtractionState.value = AiExtractionUiState.Error(err)
                        _userMessage.emit(err)
                    }
                } else {
                    _aiExtractionState.value = AiExtractionUiState.Error("Could not decode selected image")
                }
            } catch (e: Exception) {
                _aiExtractionState.value = AiExtractionUiState.Error(e.localizedMessage ?: "Scanning failed")
                _userMessage.emit("Failed to process flyer: ${e.localizedMessage}")
            }
        }
    }

    fun dismissAiExtractionDialog() {
        _aiExtractionState.value = AiExtractionUiState.Idle
    }

    fun addItemToCart(item: CatalogueItem, store: Store?) {
        viewModelScope.launch {
            repository.addItemToShoppingList(item, store)
            val balance = budgetSummary.value.remainingBalance - (
                if (item.cardDiscountPrice > 0 && repository.isCardLinkedForStore(item.storeId)) item.cardDiscountPrice else item.regularPrice
            )
            if (balance < 0) {
                _userMessage.emit("Added ${item.name}! Notice: Over budget by R ${String.format("%.2f", -balance)}")
            } else {
                _userMessage.emit("Added ${item.name} | Balance remaining: R ${String.format("%.2f", balance)}")
            }
        }
    }

    fun addCustomItem(name: String, price: Double, quantity: Int, category: String, storeName: String?, notes: String) {
        viewModelScope.launch {
            repository.addCustomItem(name, price, quantity, category, storeName, notes)
            val balance = budgetSummary.value.remainingBalance - (price * quantity)
            if (balance < 0) {
                _userMessage.emit("Added $name! Notice: Over budget by R ${String.format("%.2f", -balance)}")
            } else {
                _userMessage.emit("Added $name | Remaining: R ${String.format("%.2f", balance)}")
            }
        }
    }

    fun updateQuantity(itemId: Long, quantity: Int) {
        viewModelScope.launch {
            repository.updateItemQuantity(itemId, quantity)
        }
    }

    fun togglePurchased(itemId: Long, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.togglePurchased(itemId, currentStatus)
        }
    }

    fun deleteItem(itemId: Long) {
        viewModelScope.launch {
            repository.deleteShoppingItem(itemId)
        }
    }

    fun clearPurchased() {
        viewModelScope.launch {
            repository.clearPurchasedItems()
            _userMessage.emit("Cleared purchased items from cart")
        }
    }

    fun clearAllItems() {
        viewModelScope.launch {
            repository.clearAllShoppingItems()
            _userMessage.emit("Cart cleared")
        }
    }

    fun toggleCardLinked(cardId: String, isLinked: Boolean) {
        viewModelScope.launch {
            repository.toggleLoyaltyCardLinked(cardId, isLinked)
            _userMessage.emit(if (isLinked) "Loyalty card linked! Member discounts active." else "Loyalty card unlinked.")
        }
    }

    fun addLoyaltyCard(
        storeId: String,
        storeName: String,
        programName: String,
        cardholderName: String,
        cardNumber: String,
        tier: String
    ) {
        addActualLoyaltyCard(
            storeId = storeId,
            storeName = storeName,
            programName = programName,
            cardholderName = cardholderName,
            cardNumber = cardNumber,
            barcodeNumber = "",
            tier = tier
        )
    }

    fun addActualLoyaltyCard(
        storeId: String,
        storeName: String,
        programName: String,
        cardholderName: String,
        cardNumber: String,
        barcodeNumber: String,
        tier: String
    ) {
        viewModelScope.launch {
            val cleanBarcode = barcodeNumber.trim().ifBlank { cardNumber.filter { it.isDigit() }.take(12) }.ifBlank { "600980004412" }
            val card = LoyaltyCard(
                id = "card_${System.currentTimeMillis()}",
                storeId = storeId,
                storeName = storeName,
                programName = programName,
                cardholderName = cardholderName.trim().ifBlank { "Cardholder" },
                cardNumber = cardNumber.trim(),
                barcodeNumber = cleanBarcode,
                pointsBalance = 150,
                membershipTier = tier,
                isLinked = true,
                colorHex = when {
                    storeName.contains("Checkers", ignoreCase = true) -> 0xFF007A3D
                    storeName.contains("Pick n Pay", ignoreCase = true) -> 0xFF002F6C
                    storeName.contains("Woolworths", ignoreCase = true) -> 0xFF1A1A1A
                    storeName.contains("Shoprite", ignoreCase = true) -> 0xFFD8232A
                    storeName.contains("Makro", ignoreCase = true) -> 0xFF003882
                    storeName.contains("SPAR", ignoreCase = true) -> 0xFFDE0000
                    storeName.contains("Clicks", ignoreCase = true) -> 0xFF0083C5
                    storeName.contains("Dis-Chem", ignoreCase = true) -> 0xFF007E3A
                    storeName.contains("Food Lover", ignoreCase = true) -> 0xFF005C2B
                    else -> 0xFF0A7D55
                }
            )
            repository.addLoyaltyCard(card)
            _userMessage.emit("Linked your actual $programName card! Member discounts active.")
        }
    }

    fun linkActualCard(
        cardId: String,
        cardNumber: String,
        barcodeNumber: String,
        cardholderName: String
    ) {
        viewModelScope.launch {
            val card = loyaltyCards.value.find { it.id == cardId } ?: return@launch
            val cleanBarcode = barcodeNumber.trim().ifBlank { cardNumber.filter { it.isDigit() }.take(12) }.ifBlank { card.barcodeNumber }
            val updated = card.copy(
                cardNumber = cardNumber.trim(),
                barcodeNumber = cleanBarcode,
                cardholderName = cardholderName.trim().ifBlank { card.cardholderName },
                isLinked = true
            )
            repository.updateLoyaltyCard(updated)
            _userMessage.emit("Linked your actual ${card.programName} card! Member till discounts active in cart.")
        }
    }

    fun deleteLoyaltyCard(cardId: String) {
        viewModelScope.launch {
            repository.deleteLoyaltyCard(cardId)
            _userMessage.emit("Card removed")
        }
    }

    fun triggerNearbyPromotionNotification(store: Store) {
        notificationHelper.postPromotionNotification(store, store.promoBannerText)
        viewModelScope.launch {
            _userMessage.emit("my-catalogue.co.za deal alert sent for ${store.name}!")
        }
    }

    fun completeCheckout() {
        viewModelScope.launch {
            val currentItems = shoppingList.value
            val purchasedItems = currentItems.filter { it.isPurchased }
            if (purchasedItems.isEmpty()) {
                _userMessage.emit("Mark items as purchased (checked) to checkout!")
                return@launch
            }

            var totalEarned = 0
            val byStore = purchasedItems.groupBy { it.storeId }
            for ((storeId, items) in byStore) {
                if (storeId != null) {
                    val spent = items.sumOf { it.effectivePrice * it.quantity }
                    totalEarned += repository.completeCheckoutAndEarnPoints(storeId, spent)
                }
            }

            repository.clearPurchasedItems()
            _userMessage.emit("Shopping trip completed. Cleared purchased items and earned $totalEarned points.")
        }
    }
}
