package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val id: String,
    val serviceType: String, // RIDE, DELIVERY, GROCERY, HOME_SERVICE, PHARMACY
    val title: String,
    val subtitle: String,
    val status: String, // CONFIRMED, PARTNER_ASSIGNED, EN_ROUTE, IN_PROGRESS, COMPLETED, CANCELLED
    val amount: Double,
    val pickupAddress: String,
    val dropAddress: String,
    val timestamp: Long,
    val otp: String,
    val partnerName: String,
    val partnerPhone: String,
    val partnerRating: Float,
    val partnerVehicle: String,
    val itemsSummary: String = "",
    val paymentMode: String = "SAHAY Wallet"
)

@Entity(tableName = "wallet_transactions")
data class WalletTransactionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val isCredit: Boolean,
    val timestamp: Long,
    val category: String, // WALLET_TOPUP, RIDE_PAYMENT, GROCERY_ORDER, SERVICE_FEE, CASHBACK
    val status: String = "SUCCESS"
)

@Entity(tableName = "saved_addresses")
data class AddressEntity(
    @PrimaryKey val id: String,
    val label: String, // Home, Office, Parents, Gym
    val fullAddress: String,
    val landmark: String,
    val isDefault: Boolean
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val timestamp: Long,
    val type: String, // ORDER, PROMO, WALLET, SYSTEM
    val isRead: Boolean = false,
    val actionRoute: String = ""
)
