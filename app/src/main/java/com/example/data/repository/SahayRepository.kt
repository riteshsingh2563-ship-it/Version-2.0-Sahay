package com.example.data.repository

import com.example.data.local.SahayDatabase
import com.example.data.local.entities.AddressEntity
import com.example.data.local.entities.BookingEntity
import com.example.data.local.entities.NotificationEntity
import com.example.data.local.entities.WalletTransactionEntity
import com.example.data.model.GroceryItem
import com.example.data.model.HomeServiceItem
import com.example.data.model.PharmacyItem
import com.example.data.model.PromoCoupon
import com.example.data.model.RideTier
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class SahayRepository(private val database: SahayDatabase) {

    // Room DB Flows
    val allBookings: Flow<List<BookingEntity>> = database.bookingDao().getAllBookings()
    val activeBookings: Flow<List<BookingEntity>> = database.bookingDao().getActiveBookings()
    val walletTransactions: Flow<List<WalletTransactionEntity>> = database.walletDao().getAllTransactions()
    val walletBalance: Flow<Double?> = database.walletDao().getWalletBalanceFlow()
    val savedAddresses: Flow<List<AddressEntity>> = database.addressDao().getAllAddresses()
    val notifications: Flow<List<NotificationEntity>> = database.notificationDao().getAllNotifications()
    val unreadNotificationsCount: Flow<Int> = database.notificationDao().getUnreadCount()

    fun observeBooking(id: String): Flow<BookingEntity?> = database.bookingDao().observeBookingById(id)

    suspend fun createBooking(
        serviceType: String,
        title: String,
        subtitle: String,
        amount: Double,
        pickupAddress: String,
        dropAddress: String,
        itemsSummary: String,
        paymentMode: String
    ): BookingEntity {
        val otp = (1000..9999).random().toString()
        val bookingId = "BK_${serviceType.take(4)}_${System.currentTimeMillis() % 100000}"

        val partners = listOf(
            Triple("Vikram Malhotra", "+91 98450 12389", "Silver Swift Dzire (KA 05 MN 4920)"),
            Triple("Ananya Sharma", "+91 97312 94812", "Hero Electric Optima (KA 01 EK 8812)"),
            Triple("Suresh Kumar", "+91 99014 38291", "Bajaj Compact Auto (KA 04 AA 9120)"),
            Triple("Ramesh Verma", "+91 98110 59281", "Certified Master Technician (ID: SY-882)")
        )
        val selectedPartner = partners.random()

        val booking = BookingEntity(
            id = bookingId,
            serviceType = serviceType,
            title = title,
            subtitle = subtitle,
            status = "CONFIRMED",
            amount = amount,
            pickupAddress = pickupAddress,
            dropAddress = dropAddress,
            timestamp = System.currentTimeMillis(),
            otp = otp,
            partnerName = selectedPartner.first,
            partnerPhone = selectedPartner.second,
            partnerRating = 4.9f,
            partnerVehicle = selectedPartner.third,
            itemsSummary = itemsSummary,
            paymentMode = paymentMode
        )

        database.bookingDao().insertBooking(booking)

        // Deduct from wallet if wallet is selected
        if (paymentMode.contains("Wallet", ignoreCase = true)) {
            database.walletDao().insertTransaction(
                WalletTransactionEntity(
                    id = "TXN_${UUID.randomUUID().toString().take(8)}",
                    title = "Payment for $title",
                    subtitle = "Order #$bookingId",
                    amount = amount,
                    isCredit = false,
                    timestamp = System.currentTimeMillis(),
                    category = "SERVICE_FEE"
                )
            )
        }

        // Add confirmation notification
        database.notificationDao().insertNotification(
            NotificationEntity(
                id = "NOTIF_${UUID.randomUUID().toString().take(8)}",
                title = "Order Confirmed: $title",
                message = "Your partner ${selectedPartner.first} has been assigned. Share OTP $otp when they arrive.",
                timestamp = System.currentTimeMillis(),
                type = "ORDER",
                isRead = false,
                actionRoute = "booking_detail/$bookingId"
            )
        )

        return booking
    }

    suspend fun updateBookingStatus(id: String, newStatus: String) {
        database.bookingDao().updateBookingStatus(id, newStatus)
    }

    suspend fun topUpWallet(amount: Double, paymentMethod: String) {
        val bonus = if (amount >= 1000.0) amount * 0.05 else 0.0
        database.walletDao().insertTransaction(
            WalletTransactionEntity(
                id = "TXN_TOPUP_${System.currentTimeMillis() % 100000}",
                title = "Wallet Top-up",
                subtitle = "Via $paymentMethod",
                amount = amount,
                isCredit = true,
                timestamp = System.currentTimeMillis(),
                category = "WALLET_TOPUP"
            )
        )
        if (bonus > 0) {
            database.walletDao().insertTransaction(
                WalletTransactionEntity(
                    id = "TXN_CB_${System.currentTimeMillis() % 100000}",
                    title = "5% Cashback Bonus",
                    subtitle = "SAHAY Super Saver Offer",
                    amount = bonus,
                    isCredit = true,
                    timestamp = System.currentTimeMillis() + 100,
                    category = "CASHBACK"
                )
            )
        }
        database.notificationDao().insertNotification(
            NotificationEntity(
                id = "NOTIF_${System.currentTimeMillis() % 100000}",
                title = "₹${amount.toInt()} Added to SAHAY Wallet",
                message = "Your wallet has been topped up successfully via $paymentMethod.",
                timestamp = System.currentTimeMillis(),
                type = "WALLET",
                isRead = false
            )
        )
    }

    suspend fun markNotificationAsRead(id: String) {
        database.notificationDao().markAsRead(id)
    }

    suspend fun markAllNotificationsAsRead() {
        database.notificationDao().markAllAsRead()
    }

    suspend fun addAddress(label: String, fullAddress: String, landmark: String) {
        database.addressDao().insertAddress(
            AddressEntity(
                id = "ADDR_${System.currentTimeMillis() % 100000}",
                label = label,
                fullAddress = fullAddress,
                landmark = landmark,
                isDefault = false
            )
        )
    }

    suspend fun deleteAddress(id: String) {
        database.addressDao().deleteAddress(id)
    }

    // Catalog Data
    fun getRideTiers(): List<RideTier> = listOf(
        RideTier("bike", "SAHAY Bike Taxi", 3, 1, 49.0, "Fastest way to beat city traffic", isFastest = true),
        RideTier("auto", "SAHAY Auto Rickshaw", 4, 3, 79.0, "Affordable doorstep 3-wheeler rides", isPopular = true),
        RideTier("mini", "SAHAY Mini AC", 6, 4, 149.0, "Pocket friendly compact hatchback cars"),
        RideTier("ev", "SAHAY Green Electric", 5, 4, 169.0, "100% Zero emission modern EV cab", isElectric = true),
        RideTier("prime", "SAHAY Prime Sedan", 7, 4, 219.0, "Spacious top-rated sedans with extra legroom"),
        RideTier("suv", "SAHAY Premier XL SUV", 9, 6, 329.0, "6-seater luxury SUV for group travel")
    )

    fun getGroceryItems(): List<GroceryItem> = listOf(
        GroceryItem("g1", "Farm Fresh Alphonso Mangoes", "Fruits & Veggies", "1 kg (4-5 pcs)", 299.0, 399.0, 4.9f, "🥭"),
        GroceryItem("g2", "Organic Hass Avocados", "Fruits & Veggies", "2 pcs (400g)", 189.0, 240.0, 4.8f, "🥑"),
        GroceryItem("g3", "Fresh Tender Coconut", "Fruits & Veggies", "1 pc", 59.0, 70.0, 4.9f, "🥥"),
        GroceryItem("g4", "Pure Cow Milk Pasteurised", "Dairy & Bakery", "1000 ml", 64.0, 68.0, 4.9f, "🥛"),
        GroceryItem("g5", "Artisanal Sourdough Bread", "Dairy & Bakery", "400 g", 95.0, 120.0, 4.7f, "🍞"),
        GroceryItem("g6", "Farm Fresh Greek Yogurt", "Dairy & Bakery", "400 g", 85.0, 105.0, 4.8f, "🥣"),
        GroceryItem("g7", "Roasted Almonds & Sea Salt", "Munchies & Snacks", "200 g", 220.0, 280.0, 4.9f, "🥜"),
        GroceryItem("g8", "Dark Belgian Chocolate Bar", "Munchies & Snacks", "100 g", 145.0, 180.0, 4.8f, "🍫"),
        GroceryItem("g9", "Cold-Pressed Valencia Orange Juice", "Beverages", "1000 ml", 159.0, 199.0, 4.9f, "🍊"),
        GroceryItem("g10", "Pure Organic Green Tea", "Beverages", "25 Bags", 175.0, 225.0, 4.8f, "🍵"),
        GroceryItem("g11", "Premium Basmati Rice", "Staples & Kitchen", "5 kg", 499.0, 650.0, 4.9f, "🍚"),
        GroceryItem("g12", "Organic Extra Virgin Olive Oil", "Staples & Kitchen", "500 ml", 420.0, 550.0, 4.9f, "🫒")
    )

    fun getHomeServices(): List<HomeServiceItem> = listOf(
        HomeServiceItem(
            id = "hs1",
            title = "AC Deep Foam Jet Cleaning",
            category = "AC & Appliances",
            price = 499.0,
            durationMinutes = 60,
            rating = 4.9f,
            reviewsCount = 3840,
            description = "High pressure waterjet foam cleaning of AC filters, coils and outdoor condenser for 2x cooling.",
            inclusions = listOf("Deep pressure jet cleaning", "Filter disinfection", "Free gas leak check", "30-day warranty"),
            emoji = "❄️"
        ),
        HomeServiceItem(
            id = "hs2",
            title = "Master Electrician Inspection & Fix",
            category = "Electrician",
            price = 199.0,
            durationMinutes = 45,
            rating = 4.8f,
            reviewsCount = 2190,
            description = "Certified background-checked electrician for switches, short circuits, MCB, fans, and wiring.",
            inclusions = listOf("Safety voltage diagnosis", "Switch/Socket fix", "Wire insulation", "Upfront rate card"),
            emoji = "⚡"
        ),
        HomeServiceItem(
            id = "hs3",
            title = "Plumber - Tap, Pipe & Drain Repair",
            category = "Plumbing",
            price = 189.0,
            durationMinutes = 45,
            rating = 4.9f,
            reviewsCount = 1850,
            description = "Fix dripping faucets, concealed water pipe leaks, flush tank mechanism, and unclog blocked drains.",
            inclusions = listOf("Complete leak detection", "Fitting replacement", "Drain unblocking", "Clean workspace assurance"),
            emoji = "🔧"
        ),
        HomeServiceItem(
            id = "hs4",
            title = "Full Home Deep Cleaning (2-3 BHK)",
            category = "Cleaning",
            price = 1899.0,
            durationMinutes = 240,
            rating = 4.9f,
            reviewsCount = 4210,
            description = "Complete mechanized floor scrub, kitchen degreasing, bathroom chemical treatment & balcony pressure wash.",
            inclusions = listOf("Industrial vacuuming", "Kitchen tiles degrease", "Bathroom anti-bacterial", "Window track cleaning"),
            emoji = "✨"
        ),
        HomeServiceItem(
            id = "hs5",
            title = "Washing Machine & Refrigerator Repair",
            category = "AC & Appliances",
            price = 299.0,
            durationMinutes = 60,
            rating = 4.8f,
            reviewsCount = 1420,
            description = "Diagnostic check and repair by brand specialist technician with genuine OEM spare parts warranty.",
            inclusions = listOf("Motor diagnosis", "Drain pump fix", "Gas top-up estimate", "90-day parts guarantee"),
            emoji = "🧺"
        ),
        HomeServiceItem(
            id = "hs6",
            title = "Pest Control - Herbal Cockroach & Termite",
            category = "Cleaning",
            price = 799.0,
            durationMinutes = 90,
            rating = 4.9f,
            reviewsCount = 980,
            description = "100% odorless, child-safe gel and spray treatment with 60-day complete eradication warranty.",
            inclusions = listOf("Kitchen gel dots", "Drain opening spray", "Safe for pets & babies", "Free repeat visit if pests persist"),
            emoji = "🛡️"
        )
    )

    fun getPharmacyItems(): List<PharmacyItem> = listOf(
        PharmacyItem("ph1", "Paracetamol 650mg Fast Relief", "Pain & Fever", "15 Tablets", 32.0, 36.0, false, "Cipla Ltd", "💊"),
        PharmacyItem("ph2", "Daily Multivitamin + Zinc & D3", "Vitamins & Wellness", "60 Capsules", 349.0, 499.0, false, "HealthKart", "🌿"),
        PharmacyItem("ph3", "Comprehensive Home First Aid Kit", "First Aid", "24 Items Box", 399.0, 550.0, false, "Dettol Healthcare", "🩹"),
        PharmacyItem("ph4", "Ayurvedic Cough Syrup & Honey", "Cold & Immunity", "100 ml", 98.0, 120.0, false, "Dabur Honitus", "🍯"),
        PharmacyItem("ph5", "Digital Upper Arm BP Monitor", "Health Devices", "1 Unit with cuff", 1490.0, 2100.0, false, "Omron Healthcare", "🩺"),
        PharmacyItem("ph6", "Electrolyte Energy Drink Mix", "Wellness", "5 x 21g Sachets", 75.0, 95.0, false, "Enerzal", "⚡"),
        PharmacyItem("ph7", "Instant Antacid Effervescent", "Digestive", "Pack of 6", 48.0, 60.0, false, "Eno", "🍋"),
        PharmacyItem("ph8", "Moisturizing Sunscreen SPF 50+", "Skincare", "100 ml", 380.0, 499.0, false, "Neutrogena", "🧴")
    )

    fun getAvailableCoupons(): List<PromoCoupon> = listOf(
        PromoCoupon("SAHAYFIRST", 25, 150.0, "25% OFF up to ₹150 on your first booking across any service", 0.0),
        PromoCoupon("SAHAYRIDE", 20, 100.0, "20% OFF on all Cab, Auto & Bike rides today", 50.0),
        PromoCoupon("GROCERY50", 15, 200.0, "Flat 15% OFF on Fresh Vegetables & Grocery orders above ₹499", 499.0),
        PromoCoupon("HOMECLEAN", 30, 400.0, "30% OFF on AC Service & Home Deep Cleaning", 799.0)
    )
}
