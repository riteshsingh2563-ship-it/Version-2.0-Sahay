package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RideTier
import com.example.ui.components.SahayCompactHeader
import com.example.ui.components.SahayScreenTopBar
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

@Composable
fun RideBookingScreen(
    viewModel: SahayViewModel,
    onBack: () -> Unit,
    onBookingCreated: (String) -> Unit
) {
    val savedAddress by viewModel.currentAddress.collectAsState()
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()
    val walletBalance by viewModel.walletBalance.collectAsState()

    var pickupLocation by remember { mutableStateOf(savedAddress) }
    var dropLocation by remember { mutableStateOf("Indiranagar 100ft Road Metro Station") }
    var selectedTierId by remember { mutableStateOf("mini") }
    var paymentMethod by remember { mutableStateOf("SAHAY Cash (₹${"%.0f".format(walletBalance ?: 0.0)})") }

    val rideTiers = viewModel.rideTiers
    val selectedTier = rideTiers.firstOrNull { it.id == selectedTierId } ?: rideTiers.first()

    val discount = appliedCoupon?.let { (selectedTier.basePrice * (it.discountPercent / 100.0)).coerceAtMost(it.maxDiscount) } ?: 0.0
    val finalFare = (selectedTier.basePrice - discount).coerceAtLeast(20.0)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("ride_booking_screen"),
        color = Color(0xFFF8FAFC)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SahayScreenTopBar(
                title = "Book a SAHAY Ride",
                onBackClick = onBack,
                showBrandLogo = true
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Interactive Vector Route Map Simulation
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    SimulatedMapCard(pickup = pickupLocation, drop = dropLocation)
                }

                // Pickup & Drop address selector
                item {
                    AddressPickupDropCard(
                        pickup = pickupLocation,
                        onPickupChange = { pickupLocation = it },
                        drop = dropLocation,
                        onDropChange = { dropLocation = it }
                    )
                }

                // Ride options header
                item {
                    Text(
                        text = "Choose Your Ride Option",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SahayNavy
                        ),
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                // Ride Options list
                items(rideTiers) { tier ->
                    val isSelected = tier.id == selectedTierId
                    RideTierCard(
                        tier = tier,
                        isSelected = isSelected,
                        onClick = { selectedTierId = tier.id }
                    )
                }

                // Payment mode picker
                item {
                    PaymentSelectionCard(
                        selectedPayment = paymentMethod,
                        onSelectPayment = { paymentMethod = it }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // Bottom Floating Booking CTA Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total Estimated Fare",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "₹${"%.0f".format(finalFare)}",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Black,
                                        color = SahayNavy
                                    )
                                )
                                if (discount > 0) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "₹${"%.0f".format(selectedTier.basePrice)}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFF94A3B8),
                                            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${appliedCoupon?.code} Applied",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SahaySuccess
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = {
                                viewModel.bookRide(
                                    tier = selectedTier,
                                    pickup = pickupLocation,
                                    drop = dropLocation,
                                    paymentMode = paymentMethod,
                                    onSuccess = { booking ->
                                        onBookingCreated(booking.id)
                                    }
                                )
                            },
                            modifier = Modifier
                                .height(50.dp)
                                .testTag("confirm_ride_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SahayNavy,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Book ${selectedTier.name.replace("SAHAY ", "")}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SimulatedMapCard(pickup: String, drop: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE2E8F0)),
        border = androidx.compose.foundation.BorderStroke(1.dp, SahayBorderLight)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Road grid background
                drawLine(
                    color = Color.White,
                    start = Offset(0f, h * 0.35f),
                    end = Offset(w, h * 0.35f),
                    strokeWidth = 14f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(0f, h * 0.7f),
                    end = Offset(w, h * 0.7f),
                    strokeWidth = 10f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(w * 0.45f, 0f),
                    end = Offset(w * 0.45f, h),
                    strokeWidth = 12f
                )

                // Simulated active route path (SAHAY Sky Blue)
                val routePath = androidx.compose.ui.graphics.Path().apply {
                    moveTo(w * 0.18f, h * 0.65f)
                    lineTo(w * 0.45f, h * 0.65f)
                    lineTo(w * 0.45f, h * 0.35f)
                    lineTo(w * 0.82f, h * 0.35f)
                }
                drawPath(
                    path = routePath,
                    color = SahaySkyBlue,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 8f,
                        pathEffect = PathEffect.cornerPathEffect(12f)
                    )
                )

                // Pickup Dot (Green)
                drawCircle(color = SahaySuccess, radius = 9f, center = Offset(w * 0.18f, h * 0.65f))
                drawCircle(color = Color.White, radius = 4f, center = Offset(w * 0.18f, h * 0.65f))

                // Drop Pin (Sahay Navy)
                drawCircle(color = SahayNavy, radius = 9f, center = Offset(w * 0.82f, h * 0.35f))
                drawCircle(color = Color.White, radius = 4f, center = Offset(w * 0.82f, h * 0.35f))
            }

            // Overlay Badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(Color.White.copy(alpha = 0.92f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "📍 Est. Distance: 6.8 km (18 mins)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SahayNavy
                )
            }
        }
    }
}

@Composable
private fun AddressPickupDropCard(
    pickup: String,
    onPickupChange: (String) -> Unit,
    drop: String,
    onDropChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SahayBorderLight)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Pickup
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(SahaySuccess, CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                OutlinedTextField(
                    value = pickup,
                    onValueChange = onPickupChange,
                    label = { Text("Pickup Location", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ride_pickup_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SahaySkyBlue,
                        unfocusedBorderColor = SahayBorderLight
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Drop
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(SahayNavy, CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                OutlinedTextField(
                    value = drop,
                    onValueChange = onDropChange,
                    label = { Text("Drop Location", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ride_drop_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SahaySkyBlue,
                        unfocusedBorderColor = SahayBorderLight
                    )
                )
            }
        }
    }
}

@Composable
private fun RideTierCard(
    tier: RideTier,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("ride_tier_${tier.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SahayChipBg.copy(alpha = 0.5f) else Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) SahaySkyBlue else SahayBorderLight
        )
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
                        .size(46.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) SahaySkyBlue else SahayChipBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when {
                            tier.isElectric -> Icons.Default.ElectricCar
                            tier.id == "bike" -> Icons.Default.TwoWheeler
                            tier.id == "auto" -> Icons.Default.LocalTaxi
                            else -> Icons.Default.DirectionsCar
                        },
                        contentDescription = tier.name,
                        tint = if (isSelected) Color.White else SahayNavy,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = tier.name,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = SahayNavy
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${tier.etaMinutes} min away",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SahaySkyBlueDark
                        )
                    }

                    Text(
                        text = tier.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${"%.0f".format(tier.basePrice)}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = SahayNavy
                    )
                )
                Text(
                    text = "${tier.capacity} Seats",
                    fontSize = 10.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }
    }
}

@Composable
private fun PaymentSelectionCard(
    selectedPayment: String,
    onSelectPayment: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SahayBorderLight)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Payment Method",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = SahayNavy
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            val options = listOf(
                "SAHAY Wallet Cash",
                "UPI (Google Pay / PhonePe / Paytm)",
                "Credit / Debit Card",
                "Cash on Delivery"
            )

            options.forEach { option ->
                val isSelected = selectedPayment.startsWith(option.take(12))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onSelectPayment(option) }
                        .padding(vertical = 8.dp, horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = if (isSelected) SahaySkyBlue else Color(0xFF94A3B8),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = option,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) SahayNavy else Color(0xFF334155)
                        )
                    }
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = SahaySkyBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
