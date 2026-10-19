package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BudgetSettings
import com.example.data.model.CatalogueItem
import com.example.data.model.LoyaltyCard
import com.example.data.model.ShoppingListItem
import com.example.data.model.Store
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {
    @Query("SELECT * FROM stores ORDER BY name ASC")
    fun getAllStores(): Flow<List<Store>>

    @Query("SELECT * FROM stores WHERE id = :storeId")
    suspend fun getStoreById(storeId: String): Store?

    @Query("SELECT * FROM stores WHERE isDownloaded = 1")
    fun getDownloadedStores(): Flow<List<Store>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStores(stores: List<Store>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStore(store: Store)

    @Update
    suspend fun updateStore(store: Store)

    @Query("UPDATE stores SET isDownloaded = :isDownloaded, downloadedAt = :timestamp WHERE id = :storeId")
    suspend fun setStoreDownloaded(storeId: String, isDownloaded: Boolean, timestamp: Long)

    @Query("SELECT COUNT(*) FROM stores")
    suspend fun getCount(): Int
}

@Dao
interface CatalogueItemDao {
    @Query("SELECT * FROM catalogue_items WHERE storeId = :storeId ORDER BY category ASC, name ASC")
    fun getItemsForStore(storeId: String): Flow<List<CatalogueItem>>

    @Query("SELECT * FROM catalogue_items WHERE storeId = :storeId AND (name LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%')")
    fun searchItems(storeId: String, query: String): Flow<List<CatalogueItem>>

    @Query("SELECT * FROM catalogue_items WHERE isWeeklyDeal = 1")
    fun getWeeklyDeals(): Flow<List<CatalogueItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<CatalogueItem>)

    @Query("DELETE FROM catalogue_items WHERE storeId = :storeId")
    suspend fun deleteItemsForStore(storeId: String)

    @Query("SELECT COUNT(*) FROM catalogue_items WHERE storeId = :storeId")
    suspend fun getCountForStore(storeId: String): Int
}

@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM shopping_list_items ORDER BY isPurchased ASC, id DESC")
    fun getAllItems(): Flow<List<ShoppingListItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingListItem): Long

    @Update
    suspend fun updateItem(item: ShoppingListItem)

    @Query("UPDATE shopping_list_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantity(id: Long, quantity: Int)

    @Query("UPDATE shopping_list_items SET isPurchased = :isPurchased WHERE id = :id")
    suspend fun setPurchased(id: Long, isPurchased: Boolean)

    @Query("DELETE FROM shopping_list_items WHERE id = :id")
    suspend fun deleteItem(id: Long)

    @Query("DELETE FROM shopping_list_items WHERE isPurchased = 1")
    suspend fun clearPurchased()

    @Query("DELETE FROM shopping_list_items")
    suspend fun clearAll()
}

@Dao
interface LoyaltyCardDao {
    @Query("SELECT * FROM loyalty_cards ORDER BY storeName ASC")
    fun getAllCards(): Flow<List<LoyaltyCard>>

    @Query("SELECT * FROM loyalty_cards WHERE storeId = :storeId LIMIT 1")
    suspend fun getCardForStore(storeId: String): LoyaltyCard?

    @Query("SELECT * FROM loyalty_cards WHERE storeId = :storeId LIMIT 1")
    fun observeCardForStore(storeId: String): Flow<LoyaltyCard?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: LoyaltyCard)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<LoyaltyCard>)

    @Update
    suspend fun updateCard(card: LoyaltyCard)

    @Query("UPDATE loyalty_cards SET isLinked = :isLinked WHERE id = :cardId")
    suspend fun setCardLinked(cardId: String, isLinked: Boolean)

    @Query("UPDATE loyalty_cards SET pointsBalance = pointsBalance + :earnedPoints WHERE storeId = :storeId")
    suspend fun addPointsToStoreCard(storeId: String, earnedPoints: Int)

    @Query("DELETE FROM loyalty_cards WHERE id = :cardId")
    suspend fun deleteCard(cardId: String)

    @Query("SELECT COUNT(*) FROM loyalty_cards")
    suspend fun getCount(): Int
}

@Dao
interface BudgetSettingsDao {
    @Query("SELECT * FROM budget_settings WHERE id = 1 LIMIT 1")
    fun getSettings(): Flow<BudgetSettings?>

    @Query("SELECT * FROM budget_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettingsSync(): BudgetSettings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: BudgetSettings)
}
