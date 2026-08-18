package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.BookingEntity
import com.example.ui.components.SahayCentralSSymbol
import com.example.ui.components.SahayCompactHeader
import com.example.ui.theme.SahayBorderLight
import com.example.ui.theme.SahayChipBg
import com.example.ui.theme.SahayNavy
import com.example.ui.theme.SahayNavyDark
import com.example.ui.theme.SahayNavyLight
import com.example.ui.theme.SahaySkyBlue
import com.example.ui.theme.SahaySkyBlueDark
import com.example.ui.theme.SahaySkyBlueLight
import com.example.ui.theme.SahaySuccess
import com.example.ui.viewmodel.SahayViewModel

data class ServiceGridItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val badge: String?,
    val icon: ImageVector,
    val route: String,
    val accentColor: Color = SahaySkyBlue
)

@Composable
fun HomeScreen(
    viewModel: SahayViewModel,
    onNavigate: (String) -> Unit
) {
    val currentAddress by viewModel.currentAddress.collectAsState()
    val walletBalance by viewModel.walletBalance.collectAsState()
    val activeBookings by viewModel.activeBookings.collectAsState()
    val unreadNotifications by viewModel.unreadNotificationsCount.collectAsState()
    val coupons = viewModel.availableCoupons

    val services = listOf(
        ServiceGridItem(
            id = "rides",
            title = "Rides & Cabs",
            subtitle = "From ₹49 • Auto/Cab/Bike",
            badge = "Instant",
            icon = Icons.Default.DirectionsCar,
            route = "service_rides"
        ),
        ServiceGridItem(
            id = "courier",
            title = "Package Delivery",
            subtitle = "Send anything across town",
            badge = "30 Mins",
            icon = Icons.Default.LocalShipping,
            route = "service_courier"
        ),
        ServiceGridItem(
            id = "grocery",
            title = "Instant Grocery",
            subtitle = "Fresh produce & pantry",
            badge = "15 Mins",
            icon = Icons.Default.ShoppingBag,
            route = "service_grocery"
        ),
        ServiceGridItem(
            id = "home_services",
            title = "Home Services",
            subtitle = "AC, Electrician, Cleaning",
            badge = "Verified",
            icon = Icons.Default.Build,
            route = "service_home_services"
        ),
        ServiceGridItem(
            id = "pharmacy",
            title = "Pharmacy & Health",
            subtitle = "Medicines & first aid",
            badge = "20 Mins",
            icon = Icons.Default.Medication,
            route = "service_pharmacy"
        ),
        ServiceGridItem(
            id = "food",
            title = "Food & Dining",
            subtitle = "Top rated neighborhood eats",
            badge = "Hot & Fresh",
            icon = Icons.Default.Restaurant,
            route = "service_grocery" // Linked to grocery & pantry
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen"),
        color = Color(0xFFF8FAFC)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // 1. Header Bar with Official SAHAY Branding
            item {
                HomeHeaderSection(
                    currentAddress = currentAddress,
                    unreadCount = unreadNotifications,
                    onAddressClick = { onNavigate("profile") },
                    onNotificationClick = { onNavigate("notifications") }
                )
            }

            // 2. Active Order Banner (If Any)
            if (activeBookings.isNotEmpty()) {
                item {
                    ActiveBookingBanner(
                        booking = activeBookings.first(),
                        onClick = { onNavigate("booking_detail/${activeBookings.first().id}") }
                    )
                }
            }

            // 3. SAHAY Digital Wallet Quick Strip
            item {
                WalletQuickStrip(
                    balance = walletBalance ?: 0.0,
                    onWalletClick = { onNavigate("wallet") },
                    onTopUpClick = { onNavigate("wallet") }
                )
            }

            // 4. Hero SAHAY Super App Services Grid Header
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Super App Services",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SahayNavy
                            )
                        )
                        Text(
                            text = "One App. Every Need.",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = SahaySkyBlueDark
                            )
                        )
                    }
                }
            }

            // 5. 6 Services Grid
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    for (i in services.indices step 2) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ServiceCardItem(
                                item = services[i],
                                modifier = Modifier.weight(1f),
                                onClick = { onNavigate(services[i].route) }
                            )
                            if (i + 1 < services.size) {
                                ServiceCardItem(
                                    item = services[i + 1],
                                    modifier = Modifier.weight(1f),
                                    onClick = { onNavigate(services[i + 1].route) }
                                )
                            }
                        }
                    }
                }
            }

            // 6. Promotional Offers & Coupons
            item {
                Column(modifier = Modifier.padding(top = 18.dp)) {
                    Text(
                        text = "Exclusive SAHAY Offers",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SahayNavy
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(coupons) { coupon ->
                            PromoCouponCard(
                                coupon = coupon,
                                onApply = {
                                    viewModel.applyCoupon(coupon)
                                }
                            )
                        }
                    }
                }
            }

            // 7. Trust & Safety Guarantee Card
            item {
                BrandTrustCard()
            }
        }
    }
}

@Composable
private fun HomeHeaderSection(
    currentAddress: String,
    unreadCount: Int,
    onAddressClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Official SAHAY Header
                SahayCompactHeader(height = 36.dp, showTagline = true)

                // Alerts Button
                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier.testTag("home_notif_btn")
                ) {
                    BadgedBox(
                        badge = {
                            if (unreadCount > 0) {
                                Badge(containerColor = SahaySkyBlueDark) {
                                    Text(unreadCount.toString(), color = Color.White)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = SahayNavy
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Location Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SahayChipBg.copy(alpha = 0.5f))
                    .clickable { onAddressClick() }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = SahaySkyBlueDark,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Delivering / Pickup At",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            color = SahayNavyLight
                        )
                    )
                    Text(
                        text = currentAddress,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = SahayNavy
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = SahayNavy,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun ActiveBookingBanner(
    booking: BookingEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clickable { onClick() }
            .testTag("active_booking_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SahayNavy),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(SahaySkyBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = booking.title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(SahaySuccess, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = booking.status.replace("_", " "),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    Text(
                        text = "Partner: ${booking.partnerName} • OTP: ${booking.otp}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = SahaySkyBlueLight
                        )
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun WalletQuickStrip(
    balance: Double,
    onWalletClick: () -> Unit,
    onTopUpClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("home_wallet_strip"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SahayBorderLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onWalletClick() }
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SahayChipBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = SahayNavy,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "SAHAY Cash Balance",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    )
                    Text(
                        text = "₹${"%.2f".format(balance)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = SahayNavy
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(SahaySkyBlue)
                    .clickable { onTopUpClick() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "+ Add Cash",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    }
}

@Composable
private fun ServiceCardItem(
    item: ServiceGridItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("service_card_${item.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SahayBorderLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SahayChipBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = SahayNavy,
                        modifier = Modifier.size(24.dp)
                    )
                }

                if (item.badge != null) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    listOf(SahayNavy, SahaySkyBlueDark)
                                ),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = item.badge,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = SahayNavy
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun PromoCouponCard(
    coupon: com.example.data.model.PromoCoupon,
    onApply: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .shadow(2.dp, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SahaySkyBlue.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(SahayChipBg.copy(alpha = 0.4f), Color.White)
                    )
                )
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .border(1.dp, SahayNavy, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = coupon.code,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SahayNavy
                    )
                }
                Text(
                    text = "${coupon.discountPercent}% OFF",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = SahaySkyBlueDark
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = coupon.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.5.sp,
                    color = Color(0xFF334155),
                    lineHeight = 16.sp
                ),
                maxLines = 2
            )
        }
    }
}

@Composable
private fun BrandTrustCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SahayChipBg.copy(alpha = 0.6f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, SahayBorderLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .shadow(1.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                SahayCentralSSymbol(size = 36.dp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "The SAHAY Guarantee",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = SahayNavy
                    )
                )
                Text(
                    text = "Standard transparent pricing, zero hidden charges, GPS live tracking & 24x7 customer support.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        color = SahayNavyDark,
                        lineHeight = 15.sp
                    )
                )
            }
        }
    }
}
