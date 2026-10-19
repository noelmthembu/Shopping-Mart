package com.example.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "stores",
    indices = [
        Index("chain"),
        Index("isDownloaded")
    ]
)
data class Store(
    @PrimaryKey val id: String,
    val name: String,
    val chain: String,
    val category: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val loyaltyProgramName: String,
    val pointsPerRand: Double = 0.5,
    val discountPercentForCardHolders: Int = 15,
    val promoBannerText: String = "",
    val catalogueSource: String = "my-catalogue.co.za",
    val catalogueValidity: String = "Valid 19 Oct - 08 Nov 2026",
    val isDownloaded: Boolean = false,
    val downloadedAt: Long = 0L,
    val totalItemsCount: Int = 0,
    val brandColorHex: Long = 0xFF007A3D
)

@Entity(
    tableName = "catalogue_items",
    indices = [
        Index("storeId"),
        Index("category"),
        Index("isWeeklyDeal")
    ]
)
data class CatalogueItem(
    @PrimaryKey val id: String,
    val storeId: String,
    val name: String,
    val category: String,
    val regularPrice: Double,
    val cardDiscountPrice: Double,
    val unit: String = "each",
    val barcode: String = "",
    val inStock: Boolean = true,
    val promoTag: String = "",
    val isWeeklyDeal: Boolean = false,
    val loyaltyPointsMultiplier: Int = 1,
    val catalogueSource: String = "my-catalogue.co.za",
    val imageType: String = "",
    val imageUrl: String = ""
)

@Entity(
    tableName = "shopping_list_items",
    indices = [
        Index("isPurchased"),
        Index("storeId")
    ]
)
data class ShoppingListItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val storeId: String? = null,
    val storeName: String? = null,
    val name: String,
    val category: String = "General",
    val regularPrice: Double,
    val effectivePrice: Double,
    val quantity: Int = 1,
    val unit: String = "each",
    val isPurchased: Boolean = false,
    val isLoyaltyDiscountApplied: Boolean = false,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val imageType: String = "",
    val imageUrl: String = ""
)

@Entity(tableName = "loyalty_cards")
data class LoyaltyCard(
    @PrimaryKey val id: String,
    val storeId: String,
    val storeName: String,
    val programName: String,
    val cardholderName: String,
    val cardNumber: String,
    val barcodeNumber: String,
    val pointsBalance: Int = 0,
    val membershipTier: String = "VIP",
    val isLinked: Boolean = true,
    val colorHex: Long = 0xFF0A7D55
)

@Entity(tableName = "budget_settings")
data class BudgetSettings(
    @PrimaryKey val id: Int = 1,
    val totalBudget: Double = 1500.00,
    val currencySymbol: String = "R",
    val alertThresholdPercent: Int = 85,
    val taxRatePercent: Double = 15.0
)
