package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.BudgetSettings
import com.example.data.model.CatalogueItem
import com.example.data.model.LoyaltyCard
import com.example.data.model.ShoppingListItem
import com.example.data.model.Store

@Database(
    entities = [
        Store::class,
        CatalogueItem::class,
        ShoppingListItem::class,
        LoyaltyCard::class,
        BudgetSettings::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun storeDao(): StoreDao
    abstract fun catalogueItemDao(): CatalogueItemDao
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun loyaltyCardDao(): LoyaltyCardDao
    abstract fun budgetSettingsDao(): BudgetSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                try {
                    context.applicationContext.deleteDatabase("smart_shopper.db")
                    context.applicationContext.deleteDatabase("smart_shopper_za.db")
                    context.applicationContext.deleteDatabase("smart_shopper_za_v3.db")
                } catch (_: Exception) {
                }

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_shopper_za_v4.db"
                )
                .fallbackToDestructiveMigration(true)
                .fallbackToDestructiveMigrationOnDowngrade(true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
