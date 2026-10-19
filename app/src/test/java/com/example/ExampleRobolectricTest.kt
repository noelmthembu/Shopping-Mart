package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.ShoppingListItem
import com.example.location.LocationHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("SmartShopper", appName)
    }

    @Test
    fun `location starts unconfigured then gets configured`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val locationHelper = LocationHelper(context)
        
        // Starts not configured
        assertFalse(locationHelper.currentLocation.value.isConfigured)

        // User enters location (e.g. Sandton City)
        locationHelper.setLocation(-26.1076, 28.0567, "Sandton City, Johannesburg")
        assertTrue(locationHelper.currentLocation.value.isConfigured)
        assertEquals("Sandton City, Johannesburg", locationHelper.currentLocation.value.areaName)
    }

    @Test
    fun `south african budget remaining balance calculation with Xtra Savings`() {
        val budgetRands = 1500.00
        val items = listOf(
            ShoppingListItem(
                name = "Clover Full Cream Fresh Milk 2L",
                regularPrice = 39.99,
                effectivePrice = 32.99, // R7.00 Xtra Savings discount
                quantity = 2,
                isLoyaltyDiscountApplied = true
            ),
            ShoppingListItem(
                name = "Grabouw Traditional Boerewors 1kg",
                regularPrice = 119.99,
                effectivePrice = 99.99, // R20.00 Braai discount
                quantity = 1,
                isLoyaltyDiscountApplied = true
            ),
            ShoppingListItem(
                name = "Albany Superior White Bread",
                regularPrice = 18.99,
                effectivePrice = 16.49,
                quantity = 1,
                isLoyaltyDiscountApplied = true
            )
        )

        // 32.99*2 + 99.99 + 16.49 = 65.98 + 99.99 + 16.49 = 182.46
        val totalCost = items.sumOf { it.effectivePrice * it.quantity }
        val remaining = budgetRands - totalCost // 1500.00 - 182.46 = 1317.54
        val totalSavings = items.sumOf { (it.regularPrice - it.effectivePrice) * it.quantity } // 7*2 + 20 + 2.50 = 36.50

        assertEquals(182.46, totalCost, 0.01)
        assertEquals(1317.54, remaining, 0.01)
        assertEquals(36.50, totalSavings, 0.01)
        assertTrue(remaining > 0)
    }
}
