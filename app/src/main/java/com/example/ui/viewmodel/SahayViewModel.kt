package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.data.repository.SahayRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CartItem<T>(val item: T, val quantity: Int)

class SahayViewModel(application: Application) : AndroidViewModel(application) {
    private val database = SahayDatabase.getDatabase(application)
    private val repository = SahayRepository(database)

    // Observables from Room
    val allBookings: StateFlow<List<BookingEntity>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeBookings: StateFlow<List<BookingEntity>> = repository.activeBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val walletTransactions: StateFlow<List<WalletTransactionEntity>> = repository.walletTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val walletBalance: StateFlow<Double?> = repository.walletBalance
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1850.0)

    val savedAddresses: StateFlow<List<AddressEntity>> = repository.savedAddresses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationEntity>> = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotificationsCount: StateFlow<Int> = repository.unreadNotificationsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Current selected address
    private val _currentAddress = MutableStateFlow("Flat 402, Skyview Residency, Indiranagar")
    val currentAddress: StateFlow<String> = _currentAddress.asStateFlow()

    // Grocery Cart
    private val _groceryCart = MutableStateFlow<Map<String, CartItem<GroceryItem>>>(emptyMap())
    val groceryCart: StateFlow<Map<String, CartItem<GroceryItem>>> = _groceryCart.asStateFlow()

    // Pharmacy Cart
    private val _pharmacyCart = MutableStateFlow<Map<String, CartItem<PharmacyItem>>>(emptyMap())
    val pharmacyCart: StateFlow<Map<String, CartItem<PharmacyItem>>> = _pharmacyCart.asStateFlow()

    // Active coupon
    private val _appliedCoupon = MutableStateFlow<PromoCoupon?>(null)
    val appliedCoupon: StateFlow<PromoCoupon?> = _appliedCoupon.asStateFlow()

    // User authentication state
    private val _isUserLoggedIn = MutableStateFlow(true)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    private val _userName = MutableStateFlow("Ritesh Singh")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userPhone = MutableStateFlow("+91 98765 43210")
    val userPhone: StateFlow<String> = _userPhone.asStateFlow()

    private val _userEmail = MutableStateFlow("riteshsingh2563@gmail.com")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    // Static Catalog Data Accessors
    val rideTiers: List<RideTier> = repository.getRideTiers()
    val groceryCatalog: List<GroceryItem> = repository.getGroceryItems()
    val homeServicesCatalog: List<HomeServiceItem> = repository.getHomeServices()
    val pharmacyCatalog: List<PharmacyItem> = repository.getPharmacyItems()
    val availableCoupons: List<PromoCoupon> = repository.getAvailableCoupons()

    fun setAddress(address: String) {
        _currentAddress.value = address
    }

    fun applyCoupon(coupon: PromoCoupon?) {
        _appliedCoupon.value = coupon
    }

    fun updateGroceryQuantity(item: GroceryItem, delta: Int) {
        val current = _groceryCart.value.toMutableMap()
        val existing = current[item.id]
        if (existing == null && delta > 0) {
            current[item.id] = CartItem(item, delta)
        } else if (existing != null) {
            val newQty = existing.quantity + delta
            if (newQty <= 0) {
                current.remove(item.id)
            } else {
                current[item.id] = CartItem(item, newQty)
            }
        }
        _groceryCart.value = current
    }

    fun clearGroceryCart() {
        _groceryCart.value = emptyMap()
    }

    fun updatePharmacyQuantity(item: PharmacyItem, delta: Int) {
        val current = _pharmacyCart.value.toMutableMap()
        val existing = current[item.id]
        if (existing == null && delta > 0) {
            current[item.id] = CartItem(item, delta)
        } else if (existing != null) {
            val newQty = existing.quantity + delta
            if (newQty <= 0) {
                current.remove(item.id)
            } else {
                current[item.id] = CartItem(item, newQty)
            }
        }
        _pharmacyCart.value = current
    }

    fun clearPharmacyCart() {
        _pharmacyCart.value = emptyMap()
    }

    fun bookRide(
        tier: RideTier,
        pickup: String,
        drop: String,
        paymentMode: String,
        onSuccess: (BookingEntity) -> Unit
    ) {
        viewModelScope.launch {
            val discount = _appliedCoupon.value?.let { (tier.basePrice * (it.discountPercent / 100.0)).coerceAtMost(it.maxDiscount) } ?: 0.0
            val finalAmount = (tier.basePrice - discount).coerceAtLeast(20.0)

            val booking = repository.createBooking(
                serviceType = "RIDE",
                title = tier.name,
                subtitle = "$pickup ➔ $drop",
                amount = finalAmount,
                pickupAddress = pickup,
                dropAddress = drop,
                itemsSummary = "${tier.name} • ETA ${tier.etaMinutes} mins • AC Ride",
                paymentMode = paymentMode
            )
            onSuccess(booking)
            simulateOrderProgression(booking.id)
        }
    }

    fun bookCourierDelivery(
        packageType: String,
        weightKg: String,
        pickup: String,
        drop: String,
        isExpress: Boolean,
        amount: Double,
        paymentMode: String,
        onSuccess: (BookingEntity) -> Unit
    ) {
        viewModelScope.launch {
            val title = if (isExpress) "SAHAY Flash Courier (25 min)" else "SAHAY Standard Parcel"
            val booking = repository.createBooking(
                serviceType = "DELIVERY",
                title = title,
                subtitle = "$packageType ($weightKg) • $pickup ➔ $drop",
                amount = amount,
                pickupAddress = pickup,
                dropAddress = drop,
                itemsSummary = "Package: $packageType • Weight: $weightKg • Sealed & Insured",
                paymentMode = paymentMode
            )
            onSuccess(booking)
            simulateOrderProgression(booking.id)
        }
    }

    fun checkoutGroceryOrder(
        paymentMode: String,
        onSuccess: (BookingEntity) -> Unit
    ) {
        viewModelScope.launch {
            val cartList = _groceryCart.value.values.toList()
            val subtotal = cartList.sumOf { it.item.price * it.quantity }
            val discount = _appliedCoupon.value?.let { (subtotal * (it.discountPercent / 100.0)).coerceAtMost(it.maxDiscount) } ?: 0.0
            val deliveryFee = if (subtotal > 299.0) 0.0 else 25.0
            val total = subtotal - discount + deliveryFee

            val itemsSummary = cartList.joinToString(", ") { "${it.quantity}x ${it.item.name}" }

            val booking = repository.createBooking(
                serviceType = "GROCERY",
                title = "SAHAY Flash Grocery (15 min)",
                subtitle = "${cartList.size} items ordered",
                amount = total,
                pickupAddress = "SAHAY Dark Store Hub #12",
                dropAddress = _currentAddress.value,
                itemsSummary = itemsSummary,
                paymentMode = paymentMode
            )
            clearGroceryCart()
            onSuccess(booking)
            simulateOrderProgression(booking.id)
        }
    }

    fun bookHomeService(
        service: HomeServiceItem,
        timeSlot: String,
        paymentMode: String,
        onSuccess: (BookingEntity) -> Unit
    ) {
        viewModelScope.launch {
            val discount = _appliedCoupon.value?.let { (service.price * (it.discountPercent / 100.0)).coerceAtMost(it.maxDiscount) } ?: 0.0
            val total = service.price - discount

            val booking = repository.createBooking(
                serviceType = "HOME_SERVICE",
                title = service.title,
                subtitle = "Slot: $timeSlot • ${_currentAddress.value}",
                amount = total,
                pickupAddress = "SAHAY Certified Service Hub",
                dropAddress = _currentAddress.value,
                itemsSummary = "${service.title} • Includes: ${service.inclusions.take(2).joinToString(", ")}",
                paymentMode = paymentMode
            )
            onSuccess(booking)
            simulateOrderProgression(booking.id)
        }
    }

    fun checkoutPharmacyOrder(
        paymentMode: String,
        onSuccess: (BookingEntity) -> Unit
    ) {
        viewModelScope.launch {
            val cartList = _pharmacyCart.value.values.toList()
            val subtotal = cartList.sumOf { it.item.price * it.quantity }
            val discount = _appliedCoupon.value?.let { (subtotal * (it.discountPercent / 100.0)).coerceAtMost(it.maxDiscount) } ?: 0.0
            val total = subtotal - discount

            val itemsSummary = cartList.joinToString(", ") { "${it.quantity}x ${it.item.name}" }

            val booking = repository.createBooking(
                serviceType = "PHARMACY",
                title = "SAHAY Meds Express (20 min)",
                subtitle = "${cartList.size} health items ordered",
                amount = total,
                pickupAddress = "Apollo & MedPlus Certified Hub",
                dropAddress = _currentAddress.value,
                itemsSummary = itemsSummary,
                paymentMode = paymentMode
            )
            clearPharmacyCart()
            onSuccess(booking)
            simulateOrderProgression(booking.id)
        }
    }

    private fun simulateOrderProgression(bookingId: String) {
        viewModelScope.launch {
            delay(10000)
            repository.updateBookingStatus(bookingId, "PARTNER_ASSIGNED")
            delay(15000)
            repository.updateBookingStatus(bookingId, "EN_ROUTE")
            delay(20000)
            repository.updateBookingStatus(bookingId, "IN_PROGRESS")
        }
    }

    fun bookPharmacyOrder(
        itemCount: Int,
        amount: Double,
        hasPrescription: Boolean,
        paymentMode: String,
        onSuccess: (BookingEntity) -> Unit
    ) {
        viewModelScope.launch {
            val title = if (hasPrescription) "SAHAY Prescription Medicines" else "SAHAY OTC Health & Meds"
            val booking = repository.createBooking(
                serviceType = "PHARMACY",
                title = title,
                subtitle = "$itemCount items • 20 mins express delivery",
                amount = amount,
                pickupAddress = "Licensed Partner Pharmacy Hub",
                dropAddress = _currentAddress.value,
                itemsSummary = "Medicines & First Aid Essentials ($itemCount items)",
                paymentMode = paymentMode
            )
            clearPharmacyCart()
            onSuccess(booking)
            simulateOrderProgression(booking.id)
        }
    }

    fun updateAddress(address: String) {
        _currentAddress.value = address
    }

    fun advanceBookingStatus(bookingId: String) {
        viewModelScope.launch {
            val booking = allBookings.value.firstOrNull { it.id == bookingId }
            if (booking != null) {
                val nextStatus = when (booking.status) {
                    "PENDING_CONFIRMATION" -> "ASSIGNED"
                    "ASSIGNED" -> "IN_PROGRESS"
                    "IN_PROGRESS" -> "COMPLETED"
                    else -> "COMPLETED"
                }
                repository.updateBookingStatus(bookingId, nextStatus)
            }
        }
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, "CANCELLED")
        }
    }

    fun topUpWallet(amount: Double, method: String = "UPI / Card") {
        viewModelScope.launch {
            repository.topUpWallet(amount, method)
        }
    }

    fun markNotificationAsRead(id: String) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }

    fun addSavedAddress(label: String, fullAddress: String, landmark: String) {
        viewModelScope.launch {
            repository.addAddress(label, fullAddress, landmark)
        }
    }

    fun deleteAddress(id: String) {
        viewModelScope.launch {
            repository.deleteAddress(id)
        }
    }

    fun updateProfile(name: String, email: String, phone: String) {
        _userName.value = name
        _userEmail.value = email
        _userPhone.value = phone
    }

    fun logout() {
        _isUserLoggedIn.value = false
    }

    fun login(phoneOrEmail: String) {
        _isUserLoggedIn.value = true
    }
}
