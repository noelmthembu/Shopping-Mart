package com.example.data.repository

import com.example.data.model.CatalogueItem
import com.example.data.model.Store

object SampleStoreData {
    val stores = listOf(
        Store(
            id = "shoprite_04",
            name = "Shoprite Mayfair",
            chain = "Shoprite Holdings",
            category = "Discount Supermarket",
            address = "Central Rd, Mayfair, Johannesburg",
            latitude = -26.2050,
            longitude = 28.0180,
            loyaltyProgramName = "Xtra Savings",
            pointsPerRand = 0.5,
            discountPercentForCardHolders = 18,
            promoBannerText = "🇿🇦 Shoprite Mayfair: Lowest Prices on Maize Meal, Sugar & IQF Chicken!",
            catalogueSource = "my-catalogue.co.za",
            catalogueValidity = "Valid 19 Oct - 08 Nov 2026",
            isDownloaded = false,
            totalItemsCount = 12,
            brandColorHex = 0xFFD8232A
        ),
        Store(
            id = "checkers_01",
            name = "Checkers Fordsburg Mall",
            chain = "Shoprite Checkers Group",
            category = "Supermarket & Groceries",
            address = "Main Reef Rd & Mint St, Fordsburg, Johannesburg",
            latitude = -26.2085,
            longitude = 28.0195,
            loyaltyProgramName = "Xtra Savings",
            pointsPerRand = 0.5,
            discountPercentForCardHolders = 20,
            promoBannerText = "🔥 Checkers Xtra Savings: Save R15 on Clover Milk 2L + Buy 2 Coke 2L for R47!",
            catalogueSource = "my-catalogue.co.za",
            catalogueValidity = "Valid 19 Oct - 08 Nov 2026",
            isDownloaded = false,
            totalItemsCount = 18,
            brandColorHex = 0xFF007A3D
        ),
        Store(
            id = "pnp_02",
            name = "Pick n Pay Newtown Junction",
            chain = "Pick n Pay Retailers",
            category = "Supermarket & Hyper",
            address = "Newtown Junction, 100 Carr St, Newtown, Johannesburg",
            latitude = -26.2023,
            longitude = 28.0330,
            loyaltyProgramName = "Smart Shopper",
            pointsPerRand = 1.0,
            discountPercentForCardHolders = 15,
            promoBannerText = "🛒 Smart Shopper Summer Deals: R100 Combos + Double Points on Fresh Produce!",
            catalogueSource = "my-catalogue.co.za",
            catalogueValidity = "Valid 19 Oct - 25 Oct 2026",
            isDownloaded = false,
            totalItemsCount = 15,
            brandColorHex = 0xFF002F6C
        ),
        Store(
            id = "makro_05",
            name = "Makro Crown Mines",
            chain = "Massmart / Makro SA",
            category = "Wholesale & Bulk Groceries",
            address = "Teak Rd & Nasrec Rd, Crown Mines, Johannesburg",
            latitude = -26.2290,
            longitude = 27.9995,
            loyaltyProgramName = "mCard",
            pointsPerRand = 2.0,
            discountPercentForCardHolders = 22,
            promoBannerText = "📦 Makro Bulk Deals: More 4 Less on Pantry Staples, Drinks & Cleaning Essentials!",
            catalogueSource = "my-catalogue.co.za",
            catalogueValidity = "Valid 07 Oct - 02 Nov 2026",
            isDownloaded = false,
            totalItemsCount = 10,
            brandColorHex = 0xFF003882
        ),
        Store(
            id = "spar_06",
            name = "SUPERSPAR Melville",
            chain = "SPAR Group SA",
            category = "Fresh Groceries & Bakery",
            address = "Main Rd & 4th Ave, Melville, Johannesburg",
            latitude = -26.1741,
            longitude = 28.0094,
            loyaltyProgramName = "SPAR Rewards",
            pointsPerRand = 1.0,
            discountPercentForCardHolders = 15,
            promoBannerText = "🥖 SPAR Rewards: Unleash Your Inner Chef with Fresh Butchery & Bakery Deals!",
            catalogueSource = "my-catalogue.co.za",
            catalogueValidity = "Valid 19 Oct - 08 Nov 2026",
            isDownloaded = false,
            totalItemsCount = 8,
            brandColorHex = 0xFF007A3D
        ),
        Store(
            id = "woolworths_03",
            name = "Woolworths Food Rosebank",
            chain = "Woolworths South Africa",
            category = "Premium Food & Organic",
            address = "Rosebank Mall, 50 Bath Ave, Rosebank, Johannesburg",
            latitude = -26.1465,
            longitude = 28.0416,
            loyaltyProgramName = "WRewards",
            pointsPerRand = 1.0,
            discountPercentForCardHolders = 18,
            promoBannerText = "🥑 WRewards Quality Sale: Up to 20% off Free Range Rotisserie Chicken & Deli!",
            catalogueSource = "my-catalogue.co.za",
            catalogueValidity = "Valid 05 Oct - 25 Oct 2026",
            isDownloaded = false,
            totalItemsCount = 14,
            brandColorHex = 0xFF1A1A1A
        ),
        Store(
            id = "clicks_07",
            name = "Clicks Rosebank Mall",
            chain = "Clicks Group",
            category = "Health, Pharmacy & Snacks",
            address = "Rosebank Mall, Lower Level, Rosebank",
            latitude = -26.1455,
            longitude = 28.0420,
            loyaltyProgramName = "ClubCard",
            pointsPerRand = 1.0,
            discountPercentForCardHolders = 20,
            promoBannerText = "💊 Clicks ClubCard: 3 for 2 on Vitamins, Baby & Snacks + Feel Good Rewards!",
            catalogueSource = "my-catalogue.co.za",
            catalogueValidity = "Valid 15 Oct - 05 Nov 2026",
            isDownloaded = false,
            totalItemsCount = 6,
            brandColorHex = 0xFF0083C5
        )
    )

    fun getCatalogueForStore(storeId: String): List<CatalogueItem> {
        return when (storeId) {
            "checkers_01" -> listOf(
                CatalogueItem("chk_01", storeId, "Clover Full Cream Fresh Milk 2L", "Dairy & Eggs", 39.99, 32.99, "2 Litre", "6001234001", true, "Xtra Savings - Save R7.00", true, 2, imageType = "milk", imageUrl = "https://images.unsplash.com/photo-1550583724-b2692b85b150?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("chk_02", storeId, "Grabouw Thick Traditional Boerewors 1kg", "Meat & Butchery", 119.99, 99.99, "1 kg", "6001234002", true, "Save R20 Braai Deal", true, 3, imageType = "boerewors", imageUrl = "https://images.unsplash.com/photo-1607623814075-e51df1bdc82f?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("chk_03", storeId, "Albany Superior Thick Slices White Bread", "Bakery", 18.99, 16.49, "700g", "6001234003", true, "Daily Fresh", false, 1, imageType = "bread", imageUrl = "https://images.unsplash.com/photo-1509440159596-0249088772ff?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("chk_04", storeId, "Nescafé Classic Instant Coffee 200g", "Pantry & Coffee", 129.99, 104.99, "200g jar", "6001234004", true, "Save R25 Xtra Savings", true, 3, imageType = "coffee", imageUrl = "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("chk_05", storeId, "Coca-Cola Original Taste 2L Bottle", "Beverages", 26.99, 23.50, "2 Litre", "6001234005", true, "Buy 2 for R47", true, 1, imageType = "coke", imageUrl = "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("chk_06", storeId, "OMO Auto Washing Powder 3kg", "Household", 159.99, 129.99, "3 kg", "6001234006", true, "Save R30.00", true, 3, imageType = "omo", imageUrl = "https://images.unsplash.com/photo-1585421514284-efb74c2b69ba?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("chk_07", storeId, "Clover Butro Butter Spread Tub 500g", "Dairy & Eggs", 89.99, 79.50, "500g", "6001234007", true, "Buy 2 for R159", true, 2, imageType = "butter", imageUrl = "https://images.unsplash.com/photo-1589985270826-4b7bb135bc9d?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("chk_08", storeId, "Koo Baked Beans in Tomato Sauce 410g", "Pantry Essentials", 17.99, 14.99, "410g tin", "6001234008", true, "Low Price", false, 1, imageType = "beans", imageUrl = "https://images.unsplash.com/photo-1541592106381-b31e9677c0e5?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("chk_09", storeId, "All Gold Tomato Sauce 700ml Glass", "Pantry Essentials", 39.99, 34.99, "700ml", "6001234009", true, "Original Recipe", false, 1, imageType = "sauce", imageUrl = "https://images.unsplash.com/photo-1541592106381-b31e9677c0e5?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("chk_10", storeId, "Mrs Ball's Original Chutney 470g", "Pantry Essentials", 42.99, 36.99, "470g bottle", "6001234010", true, "Traditional Taste", false, 1, imageType = "chutney", imageUrl = "https://images.unsplash.com/photo-1541592106381-b31e9677c0e5?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("chk_11", storeId, "Simba Potato Chips Smoked Beef 120g", "Snacks & Sweets", 19.99, 16.99, "120g packet", "6001234011", true, "Roarrrs Flavour", false, 1, imageType = "simba", imageUrl = "https://images.unsplash.com/photo-1566478989037-eec170784d0b?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("chk_12", storeId, "Cadbury Dairy Milk Chocolate Slab 150g", "Snacks & Sweets", 31.99, 24.99, "150g slab", "6001234012", true, "Buy 3 Save 25%", true, 2, imageType = "chocolate", imageUrl = "https://images.unsplash.com/photo-1549007994-cb92caebd54b?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("chk_13", storeId, "Fresh South African Hass Avocados (4pk)", "Fresh Produce", 39.99, 29.99, "4 pack", "6001234013", true, "Ripe & Ready R10 Off", true, 2, imageType = "avocado", imageUrl = "https://images.unsplash.com/photo-1540420773420-3366772f4999?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("chk_14", storeId, "Tender Pork Loin Chops Braai Pack 1kg", "Meat & Butchery", 94.99, 79.99, "per kg", "6001234014", true, "Butcher's Choice", true, 2, imageType = "chops", imageUrl = "https://images.unsplash.com/photo-1607623814075-e51df1bdc82f?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("chk_15", storeId, "Sunlight 2-in-1 Handwash Powder 2kg", "Household", 69.99, 54.99, "2 kg", "6001234015", true, "Spring Clean Deal", false, 2, imageType = "sunlight", imageUrl = "https://images.unsplash.com/photo-1585421514284-efb74c2b69ba?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("chk_16", storeId, "Five Roses Ceylon Tea Blend 102s", "Pantry & Coffee", 54.99, 44.99, "102 tagless", "6001234016", true, "Best Quality Tea", true, 2, imageType = "tea", imageUrl = "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("chk_17", storeId, "Tastic Parboiled White Rice 2kg", "Pantry Essentials", 46.99, 38.99, "2 kg", "6001234017", true, "Cooks Fluffy", false, 1, imageType = "rice", imageUrl = "https://images.unsplash.com/photo-1586201375761-83865001e31c?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("chk_18", storeId, "Crosse & Blackwell Tangy Mayonnaise 750g", "Pantry Essentials", 44.99, 37.99, "750g jar", "6001234018", true, "Tangy Classic", false, 1, imageType = "mayo", imageUrl = "https://images.unsplash.com/photo-1541592106381-b31e9677c0e5?auto=format&fit=crop&w=600&q=80")
            )

            "pnp_02" -> listOf(
                CatalogueItem("pnp_01", storeId, "PnP Fresh Full Cream Milk 2L", "Dairy & Eggs", 36.99, 31.99, "2 Litre", "6009800001", true, "Smart Price Save R5", true, 2, imageType = "milk", imageUrl = "https://images.unsplash.com/photo-1550583724-b2692b85b150?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("pnp_02", storeId, "PnP Bulk Lean Beef Mince 1kg", "Butchery", 109.99, 89.99, "1 kg", "6009800002", true, "Smart Shopper R20 Off", true, 3, imageType = "mince", imageUrl = "https://images.unsplash.com/photo-1607623814075-e51df1bdc82f?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("pnp_03", storeId, "Sasko Premium Brown Bread 700g", "Bakery", 17.49, 15.49, "700g", "6009800003", true, "Fresh Daily", false, 1, imageType = "bread", imageUrl = "https://images.unsplash.com/photo-1509440159596-0249088772ff?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("pnp_04", storeId, "Jacobs Krönung Instant Coffee 200g", "Coffee & Tea", 154.99, 129.99, "200g jar", "6009800004", true, "Save R25 with Card", true, 3, imageType = "coffee", imageUrl = "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("pnp_05", storeId, "Danone Ultra Mel Vanilla Custard 1L", "Desserts & Dairy", 36.99, 29.99, "1 Litre", "6009800005", true, "Summer Treat", true, 2, imageType = "custard", imageUrl = "https://images.unsplash.com/photo-1550583724-b2692b85b150?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("pnp_06", storeId, "Lay's Potato Chips Salt & Vinegar 120g", "Snacks", 21.99, 17.99, "120g", "6009800006", true, "Smart Shopper Special", false, 1, imageType = "chips", imageUrl = "https://images.unsplash.com/photo-1566478989037-eec170784d0b?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("pnp_07", storeId, "PnP Extra Virgin Olive Oil 1L", "Cooking Essentials", 189.99, 159.99, "1 Litre", "6009800007", true, "Cold Pressed R30 Off", true, 3, imageType = "oil", imageUrl = "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("pnp_08", storeId, "Freshpak Pure Rooibos Tea 80s", "Coffee & Tea", 48.99, 39.99, "80 bags", "6009800008", true, "Naturally Caffeine Free", false, 1, imageType = "rooibos", imageUrl = "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("pnp_09", storeId, "Sunlight Lemon Dishwashing Liquid 750ml", "Household", 34.99, 27.99, "750ml", "6009800009", true, "Real Lemon Power", false, 1, imageType = "sunlight", imageUrl = "https://images.unsplash.com/photo-1585421514284-efb74c2b69ba?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("pnp_10", storeId, "Snowflake Cake Wheat Flour 2.5kg", "Baking Essentials", 44.99, 36.99, "2.5 kg", "6009800010", true, "Too Fresh To Flop", false, 1, imageType = "flour", imageUrl = "https://images.unsplash.com/photo-1586201375761-83865001e31c?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("pnp_11", storeId, "Rainbow Frozen Chicken Braai Pack 2kg", "Frozen Food", 89.99, 76.99, "2 kg", "6009800011", true, "IQF Farmhouse Pack", true, 2, imageType = "chicken", imageUrl = "https://images.unsplash.com/photo-1587593810167-a84920ea0781?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("pnp_12", storeId, "Appletiser 100% Sparkling Apple Juice 1.25L", "Beverages", 28.99, 23.99, "1.25 Litre", "6009800012", true, "Crown Yours", true, 2, imageType = "juice", imageUrl = "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("pnp_13", storeId, "Ouma Buttermilk Rusks Box 500g", "Bakery & Biscuits", 56.99, 46.99, "500g box", "6009800013", true, "Dip 'n Ouma R10 Off", true, 2, imageType = "rusks", imageUrl = "https://images.unsplash.com/photo-1509440159596-0249088772ff?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("pnp_14", storeId, "Colgate Maximum Cavity Protection 100ml", "Toiletries", 26.99, 22.00, "100ml", "6009800014", true, "Buy 2 for R44", false, 1, imageType = "toothpaste", imageUrl = "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("pnp_15", storeId, "Castle Lite 6 x 330ml Non-Returnable Bottles", "Beverages", 79.99, 69.99, "6 pack", "6009800015", true, "Extra Cold Refreshment", true, 2, imageType = "beer", imageUrl = "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?auto=format&fit=crop&w=600&q=80")
            )

            "woolworths_03" -> listOf(
                CatalogueItem("ww_01", storeId, "Woolworths Ayrshire Full Cream Milk 2L", "Dairy & Eggs", 44.99, 38.99, "2 Litre", "6009170001", true, "WRewards Save R6", true, 2, imageType = "milk", imageUrl = "https://images.unsplash.com/photo-1550583724-b2692b85b150?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("ww_02", storeId, "Free Range Whole Rotisserie Chicken", "Prepared Meals", 119.99, 99.99, "1.2 kg", "6009170002", true, "Hot & Crispy R20 Off", true, 3, imageType = "chicken", imageUrl = "https://images.unsplash.com/photo-1587593810167-a84920ea0781?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("ww_03", storeId, "Bulk Farmstyle Beef Boerewors 1kg", "Meat & Poultry", 134.99, 114.99, "1 kg", "6009170003", true, "No Added MSG", true, 3, imageType = "boerewors", imageUrl = "https://images.unsplash.com/photo-1607623814075-e51df1bdc82f?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("ww_04", storeId, "Easy to Cook Lemon & Herb Chicken Flatties", "Meat & Poultry", 114.99, 94.99, "1 kg", "6009170004", true, "WRewards 18% Off", true, 2, imageType = "chicken", imageUrl = "https://images.unsplash.com/photo-1587593810167-a84920ea0781?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("ww_05", storeId, "Hand-Stretched Sourdough Margherita Pizza", "Deli & Bakery", 89.99, 74.99, "420g", "6009170005", true, "Stonebaked Italian", false, 2, imageType = "pizza", imageUrl = "https://images.unsplash.com/photo-1513104890138-7c749659a591?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("ww_06", storeId, "Organic Baby Spinach Leaves 200g", "Fresh Produce", 32.99, 26.99, "200g pillow", "6009170006", true, "Washed & Ready", false, 1, imageType = "salad", imageUrl = "https://images.unsplash.com/photo-1540420773420-3366772f4999?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("ww_07", storeId, "Sweet Gold Kiwi Fruit (4 Pack)", "Fresh Produce", 49.99, 39.99, "4 pack", "6009170007", true, "Super Sweet", true, 2, imageType = "kiwi", imageUrl = "https://images.unsplash.com/photo-1540420773420-3366772f4999?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("ww_08", storeId, "Woolworths 100% Pure Squeezed Orange Juice 1.5L", "Beverages", 59.99, 49.99, "1.5 Litre", "6009170008", true, "Freshly Squeezed", true, 2, imageType = "juice", imageUrl = "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("ww_09", storeId, "All Butter Farmhouse Croissants (4pk)", "Bakery", 49.99, 39.99, "4 pack", "6009170009", true, "Pure Butter Flaky", false, 2, imageType = "croissant", imageUrl = "https://images.unsplash.com/photo-1555507036-ab1f4038808a?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("ww_10", storeId, "Traditional Beef Biltong Slices 200g", "Snacks & Treats", 99.99, 84.99, "200g pack", "6009170010", true, "Coriander Spiced", true, 3, imageType = "biltong", imageUrl = "https://images.unsplash.com/photo-1607623814075-e51df1bdc82f?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("ww_11", storeId, "Chuckles Milk Chocolate Malt Spheres 125g", "Snacks & Treats", 44.99, 36.99, "125g bag", "6009170011", true, "Iconic Woolies Treat", true, 2, imageType = "chocolate", imageUrl = "https://images.unsplash.com/photo-1549007994-cb92caebd54b?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("ww_12", storeId, "Italian Mozzarella Cheese Ball 250g", "Dairy & Eggs", 54.99, 44.99, "250g", "6009170012", true, "Creamy Fresh", false, 1, imageType = "cheese", imageUrl = "https://images.unsplash.com/photo-1452195100486-9cc805987862?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("ww_13", storeId, "Woolworths Organic Rooibos Chai 40s", "Coffee & Tea", 42.99, 34.99, "40 bags", "6009170013", true, "Spiced Organic", false, 1, imageType = "tea", imageUrl = "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("ww_14", storeId, "Dishwashing Tablets All-in-1 30pk", "Household", 129.99, 109.99, "30 tablets", "6009170014", true, "Sparkling Clean", true, 2, imageType = "cleaning", imageUrl = "https://images.unsplash.com/photo-1585421514284-efb74c2b69ba?auto=format&fit=crop&w=600&q=80")
            )

            "shoprite_04" -> listOf(
                CatalogueItem("shp_01", storeId, "White Star Super Maize Meal 5kg", "Staples", 69.99, 58.99, "5 kg bag", "6009000101", true, "The Clever Choice", true, 2, imageType = "maize", imageUrl = "https://images.unsplash.com/photo-1586201375761-83865001e31c?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("shp_02", storeId, "Huletts SunSweet White Sugar 2.5kg", "Staples", 54.99, 46.99, "2.5 kg", "6009000102", true, "Pure Cane Sugar", false, 1, imageType = "sugar", imageUrl = "https://images.unsplash.com/photo-1586201375761-83865001e31c?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("shp_03", storeId, "Top Class Soya Mince Spice Mix 500g", "Pantry", 26.99, 21.99, "500g", "6009000103", true, "Rich Meaty Taste", false, 1, imageType = "mince", imageUrl = "https://images.unsplash.com/photo-1607623814075-e51df1bdc82f?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("shp_04", storeId, "Ritebrand Long Life Full Cream Milk 6x1L", "Dairy", 99.99, 84.99, "6x1L box", "6009000104", true, "Bulk Value Pack", true, 3, imageType = "milk", imageUrl = "https://images.unsplash.com/photo-1550583724-b2692b85b150?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("shp_05", storeId, "Farm Foods Frozen Mixed Veg 1kg", "Frozen Food", 32.99, 26.99, "1 kg", "6009000105", true, "Crisp Choice", false, 1, imageType = "veg", imageUrl = "https://images.unsplash.com/photo-1540420773420-3366772f4999?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("shp_06", storeId, "Sunfoil Pure Sunflower Cooking Oil 2L", "Cooking", 74.99, 62.99, "2 Litre", "6009000106", true, "Triple Refined", true, 2, imageType = "oil", imageUrl = "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("shp_07", storeId, "Lucky Star Pilchards in Tomato Sauce 400g", "Canned Food", 28.99, 23.99, "400g tin", "6009000107", true, "Omega 3 High Protein", false, 1, imageType = "fish", imageUrl = "https://images.unsplash.com/photo-1541592106381-b31e9677c0e5?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("shp_08", storeId, "Golden Cloud Cake Flour 2.5kg", "Baking", 42.99, 35.99, "2.5 kg", "6009000108", true, "Soft & Fine", false, 1, imageType = "flour", imageUrl = "https://images.unsplash.com/photo-1586201375761-83865001e31c?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("shp_09", storeId, "D'Lite Medium Fat Spread Tub 1kg", "Dairy", 39.99, 32.99, "1 kg tub", "6009000109", true, "Smooth & Creamy", false, 1, imageType = "butter", imageUrl = "https://images.unsplash.com/photo-1589985270826-4b7bb135bc9d?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("shp_10", storeId, "MAQ Washing Powder Handwash 2kg", "Cleaning", 64.99, 52.99, "2 kg", "6009000110", true, "Shine Power", false, 2, imageType = "cleaning", imageUrl = "https://images.unsplash.com/photo-1585421514284-efb74c2b69ba?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("shp_11", storeId, "Goldi Mixed Chicken Portions 4kg", "Frozen Food", 159.99, 139.99, "4 kg bag", "6009000111", true, "Family Braai Size", true, 3, imageType = "chicken", imageUrl = "https://images.unsplash.com/photo-1587593810167-a84920ea0781?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("shp_12", storeId, "Fusion Dairy Fruit Blend 1L", "Beverages", 14.99, 11.99, "1 Litre", "6009000112", true, "Refreshing Blend", false, 1, imageType = "juice", imageUrl = "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?auto=format&fit=crop&w=600&q=80")
            )

            "makro_05" -> listOf(
                CatalogueItem("mkr_01", storeId, "Tastic Parboiled Rice 10kg Mega Bag", "Bulk Staples", 219.99, 189.99, "10 kg", "6008800001", true, "mCard Save R30", true, 4, imageType = "rice", imageUrl = "https://images.unsplash.com/photo-1586201375761-83865001e31c?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("mkr_02", storeId, "Coca-Cola Original 24 x 330ml Can Case", "Bulk Drinks", 239.99, 199.99, "24 pack", "6008800002", true, "Party Case Deal", true, 4, imageType = "coke", imageUrl = "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("mkr_03", storeId, "First Choice Long Life Milk 6 x 1L Pack", "Bulk Dairy", 94.99, 79.99, "6x1L", "6008800003", true, "Pantry Stocker", true, 2, imageType = "milk", imageUrl = "https://images.unsplash.com/photo-1550583724-b2692b85b150?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("mkr_04", storeId, "Sunlight Dishwashing Liquid 5L Bulk", "Bulk Cleaning", 179.99, 149.99, "5 Litre", "6008800004", true, "More 4 Less", true, 3, imageType = "sunlight", imageUrl = "https://images.unsplash.com/photo-1585421514284-efb74c2b69ba?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("mkr_05", storeId, "OMO Auto Washing Powder 9kg Mega Pack", "Bulk Cleaning", 389.99, 329.99, "9 kg", "6008800005", true, "Save R60 with mCard", true, 5, imageType = "omo", imageUrl = "https://images.unsplash.com/photo-1585421514284-efb74c2b69ba?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("mkr_06", storeId, "Twin Saver 2-Ply Toilet Paper 18 Rolls", "Bulk Household", 139.99, 114.99, "18 rolls", "6008800006", true, "Ultra Soft & Thick", true, 3, imageType = "toiletpaper", imageUrl = "https://images.unsplash.com/photo-1584551246679-0daf3d275d0f?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("mkr_07", storeId, "Nescafé Gold Instant Coffee Jar 200g", "Coffee & Tea", 169.99, 139.99, "200g jar", "6008800007", true, "Rich Aroma Save R30", true, 3, imageType = "coffee", imageUrl = "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("mkr_08", storeId, "Castle Lager Cans 24 x 330ml Case", "Bulk Drinks", 269.99, 239.99, "24 cans", "6008800008", true, "It All Comes Together", true, 4, imageType = "beer", imageUrl = "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("mkr_09", storeId, "Simba Potato Chips 24 x 36g Box", "Bulk Snacks", 149.99, 124.99, "24 packets", "6008800009", true, "Variety Lunchbox", true, 2, imageType = "simba", imageUrl = "https://images.unsplash.com/photo-1566478989037-eec170784d0b?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("mkr_10", storeId, "Kellogg's Corn Flakes Value Pack 1.2kg", "Cereals", 79.99, 64.99, "1.2 kg", "6008800010", true, "Original & Best", false, 2, imageType = "cereal", imageUrl = "https://images.unsplash.com/photo-1521483451571-c0ebd0023a59?auto=format&fit=crop&w=600&q=80")
            )

            "spar_06" -> listOf(
                CatalogueItem("spr_01", storeId, "SPAR Fresh Full Cream Milk 2L", "Dairy & Eggs", 37.99, 31.99, "2 Litre", "6005500001", true, "SPAR Rewards R6 Off", true, 2, imageType = "milk", imageUrl = "https://images.unsplash.com/photo-1550583724-b2692b85b150?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("spr_02", storeId, "Freshly Baked SPAR Crusty French Baguette", "Bakery", 14.99, 11.99, "each", "6005500002", true, "Baked Hourly", false, 1, imageType = "bread", imageUrl = "https://images.unsplash.com/photo-1509440159596-0249088772ff?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("spr_03", storeId, "SPAR Choice Grade Boerewors 1kg", "Butchery", 109.99, 89.99, "1 kg", "6005500003", true, "Chef's Cut R20 Off", true, 3, imageType = "boerewors", imageUrl = "https://images.unsplash.com/photo-1607623814075-e51df1bdc82f?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("spr_04", storeId, "SPAR Country Fresh Salted Butter 500g", "Dairy & Eggs", 74.99, 62.99, "500g", "6005500004", true, "Pure Cream Butter", false, 2, imageType = "butter", imageUrl = "https://images.unsplash.com/photo-1589985270826-4b7bb135bc9d?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("spr_05", storeId, "Fresh Medium Eggs Tray 30s", "Dairy & Eggs", 84.99, 69.99, "30 tray", "6005500005", true, "Breakfast Value", true, 2, imageType = "eggs", imageUrl = "https://images.unsplash.com/photo-1516448702985-783293103bf3?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("spr_06", storeId, "SPAR Extra Virgin Olive Oil 750ml", "Pantry", 139.99, 119.99, "750ml", "6005500006", true, "Imported Italian", false, 2, imageType = "oil", imageUrl = "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("spr_07", storeId, "Romany Creams Choc Coconut Biscuits 200g", "Biscuits", 34.99, 28.99, "200g", "6005500007", true, "Crunchy Chocolate", false, 1, imageType = "chocolate", imageUrl = "https://images.unsplash.com/photo-1549007994-cb92caebd54b?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("spr_08", storeId, "SPAR Still Natural Spring Water 6x500ml", "Beverages", 39.99, 29.99, "6 pack", "6005500008", true, "Pure Hydration", false, 1, imageType = "water", imageUrl = "https://images.unsplash.com/photo-1548839140-29a749e1cf4d?auto=format&fit=crop&w=600&q=80")
            )

            "clicks_07" -> listOf(
                CatalogueItem("clk_01", storeId, "Clicks Vitamin C 1000mg Effervescent 20s", "Health & Vitamins", 79.99, 59.99, "20 tablets", "6007700001", true, "3 for 2 ClubCard Offer", true, 2, imageType = "vitamins", imageUrl = "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("clk_02", storeId, "Nivea Rich Nourishing Body Lotion 400ml", "Skincare", 84.99, 69.99, "400ml", "6007700002", true, "48h Deep Moisture", true, 2, imageType = "lotion", imageUrl = "https://images.unsplash.com/photo-1608248597359-9943b185d89f?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("clk_03", storeId, "Clicks Made 4 Baby Jumbo Diapers 72s", "Baby Care", 229.99, 189.99, "72 pack", "6007700003", true, "Save R40 ClubCard", true, 4, imageType = "baby", imageUrl = "https://images.unsplash.com/photo-1515488042361-ee00e0ddd4e4?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("clk_04", storeId, "Dettol Antiseptic Liquid 750ml", "First Aid", 94.99, 79.99, "750ml", "6007700004", true, "Germ Protection", false, 2, imageType = "cleaning", imageUrl = "https://images.unsplash.com/photo-1585421514284-efb74c2b69ba?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("clk_05", storeId, "Panado Paracetamol Tablets 24s", "Pharmacy", 34.99, 27.99, "24 tablets", "6007700005", true, "Gentle on Stomach", false, 1, imageType = "medicine", imageUrl = "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?auto=format&fit=crop&w=600&q=80"),
                CatalogueItem("clk_06", storeId, "Jungle Energy Bars 4 x 48g Pack", "Snacks", 42.99, 34.99, "4 pack", "6007700006", true, "Real Energy", false, 1, imageType = "snack", imageUrl = "https://images.unsplash.com/photo-1566478989037-eec170784d0b?auto=format&fit=crop&w=600&q=80")
            )

            else -> emptyList()
        }
    }
}
