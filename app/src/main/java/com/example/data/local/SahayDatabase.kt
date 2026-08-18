package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.AddressDao
import com.example.data.local.dao.BookingDao
import com.example.data.local.dao.NotificationDao
import com.example.data.local.dao.WalletDao
import com.example.data.local.entities.AddressEntity
import com.example.data.local.entities.BookingEntity
import com.example.data.local.entities.NotificationEntity
import com.example.data.local.entities.WalletTransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        BookingEntity::class,
        WalletTransactionEntity::class,
        AddressEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SahayDatabase : RoomDatabase() {
    abstract fun bookingDao(): BookingDao
    abstract fun walletDao(): WalletDao
    abstract fun addressDao(): AddressDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: SahayDatabase? = null

        fun getDatabase(context: Context): SahayDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SahayDatabase::class.java,
                    "sahay_database"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Seed initial data
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        seedInitialData(database)
                    }
                }
            }

            private suspend fun seedInitialData(db: SahayDatabase) {
                // Initial wallet balance
                db.walletDao().insertTransaction(
                    WalletTransactionEntity(
                        id = "TXN_INIT_01",
                        title = "Welcome Bonus",
                        subtitle = "SAHAY Sign-up Gift Credits",
                        amount = 350.0,
                        isCredit = true,
                        timestamp = System.currentTimeMillis() - 86400000L * 2,
                        category = "CASHBACK"
                    )
                )
                db.walletDao().insertTransaction(
                    WalletTransactionEntity(
                        id = "TXN_INIT_02",
                        title = "Added via UPI",
                        subtitle = "Google Pay / HDFC Bank",
                        amount = 1500.0,
                        isCredit = true,
                        timestamp = System.currentTimeMillis() - 86400000L,
                        category = "WALLET_TOPUP"
                    )
                )

                // Initial addresses
                db.addressDao().insertAddress(
                    AddressEntity(
                        id = "ADDR_1",
                        label = "Home",
                        fullAddress = "Flat 402, Skyview Residency, 100ft Road, Indiranagar",
                        landmark = "Opposite Metro Pillar 84",
                        isDefault = true
                    )
                )
                db.addressDao().insertAddress(
                    AddressEntity(
                        id = "ADDR_2",
                        label = "Office",
                        fullAddress = "Tower B, Level 6, Tech Park, Outer Ring Road",
                        landmark = "Near Gate 3 Main Reception",
                        isDefault = false
                    )
                )

                // Initial notifications
                db.notificationDao().insertNotification(
                    NotificationEntity(
                        id = "NOTIF_1",
                        title = "Welcome to SAHAY! 🎉",
                        message = "Your unified gateway for Rides, Deliveries, Groceries, Home Services & Pharmacy is now active.",
                        timestamp = System.currentTimeMillis() - 3600000L * 5,
                        type = "SYSTEM",
                        isRead = false
                    )
                )
                db.notificationDao().insertNotification(
                    NotificationEntity(
                        id = "NOTIF_2",
                        title = "₹350 SAHAY Cash Credited 💰",
                        message = "Enjoy flat 25% off on your first Cab ride or Instant Grocery order. Use code SAHAYFIRST.",
                        timestamp = System.currentTimeMillis() - 3600000L * 2,
                        type = "WALLET",
                        isRead = false
                    )
                )

                // Sample completed past booking
                db.bookingDao().insertBooking(
                    BookingEntity(
                        id = "BK_RIDE_101",
                        serviceType = "RIDE",
                        title = "Prime Sedan Ride",
                        subtitle = "Indiranagar to Koramangala 5th Block",
                        status = "COMPLETED",
                        amount = 245.0,
                        pickupAddress = "Skyview Residency, Indiranagar",
                        dropAddress = "Forum Mall, Koramangala",
                        timestamp = System.currentTimeMillis() - 86400000L,
                        otp = "4829",
                        partnerName = "Rajesh Kumar",
                        partnerPhone = "+91 98765 43210",
                        partnerRating = 4.9f,
                        partnerVehicle = "White Honda City (KA 03 MN 8821)",
                        itemsSummary = "Prime Sedan • 8.4 km • AC on",
                        paymentMode = "SAHAY Wallet"
                    )
                )
            }
        }
    }
}
