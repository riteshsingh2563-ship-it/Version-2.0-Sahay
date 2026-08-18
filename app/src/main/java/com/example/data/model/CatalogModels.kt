package com.example.data.model

import androidx.compose.ui.graphics.vector.ImageVector

enum class ServiceType(val displayName: String, val tagline: String) {
    RIDE("Rides & Cabs", "Instant City Rides, Auto & Intercity"),
    DELIVERY("Package Courier", "Send anything across city in 30 mins"),
    GROCERY("Instant Grocery", "Delivered fresh to door in 15 mins"),
    HOME_SERVICE("Home Services", "Vetted Electricians, Plumbers & Cleaners"),
    PHARMACY("Pharmacy & Health", "Medicines & wellness at your doorstep"),
    FOOD("Food & Dining", "Top culinary picks delivered hot")
}

data class RideTier(
    val id: String,
    val name: String,
    val etaMinutes: Int,
    val capacity: Int,
    val basePrice: Double,
    val description: String,
    val isElectric: Boolean = false,
    val isFastest: Boolean = false,
    val isPopular: Boolean = false
)

data class GroceryItem(
    val id: String,
    val name: String,
    val category: String,
    val weight: String,
    val price: Double,
    val originalPrice: Double,
    val rating: Float = 4.8f,
    val emoji: String,
    val inStock: Boolean = true
)

data class HomeServiceItem(
    val id: String,
    val title: String,
    val category: String,
    val price: Double,
    val durationMinutes: Int,
    val rating: Float = 4.9f,
    val reviewsCount: Int = 1240,
    val description: String,
    val inclusions: List<String>,
    val emoji: String
)

data class PharmacyItem(
    val id: String,
    val name: String,
    val category: String,
    val dosage: String,
    val price: Double,
    val originalPrice: Double,
    val requiresPrescription: Boolean = false,
    val manufacturer: String,
    val emoji: String
)

data class PromoCoupon(
    val code: String,
    val discountPercent: Int,
    val maxDiscount: Double,
    val description: String,
    val minOrder: Double
)
