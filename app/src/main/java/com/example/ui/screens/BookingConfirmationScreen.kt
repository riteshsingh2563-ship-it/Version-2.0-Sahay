package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.BookingEntity
import com.example.ui.components.SahayCentralSSymbol
import com.example.ui.components.SahayScreenTopBar
import com.example.ui.theme.SahayBorderLight
import com.example.ui.theme.SahayChipBg
import com.example.ui.theme.SahayNavy
import com.example.ui.theme.SahayNavyDark
import com.example.ui.theme.SahaySkyBlue
import com.example.ui.theme.SahaySkyBlueDark
import com.example.ui.theme.SahaySkyBlueLight
import com.example.ui.theme.SahaySuccess
import com.example.ui.theme.SahayWarning
import com.example.ui.viewmodel.SahayViewModel

@Composable
fun BookingConfirmationScreen(
    bookingId: String,
    viewModel: SahayViewModel,
    onBackToHome: () -> Unit
) {
    val activeBookings by viewModel.activeBookings.collectAsState()
    val allBookings by viewModel.allBookings.collectAsState()
    val booking = activeBookings.firstOrNull { it.id == bookingId }
        ?: allBookings.firstOrNull { it.id == bookingId }

    var showSupportToast by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("booking_confirmation_screen"),
        color = Color(0xFFF8FAFC)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SahayScreenTopBar(
                title = "Order Tracking",
                onBackClick = onBackToHome,
                showBrandLogo = true,
                trailingContent = {
                    IconButton(onClick = { showSupportToast = true }) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Safety Support",
                            tint = SahayNavy
                        )
                    }
                }
            )

            if (booking == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Booking details not found.")
                }
                return@Column
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Official Status Header Card
                StatusSummaryCard(booking = booking)

                // OTP Security Card (Large highlight)
                if (booking.status != "COMPLETED" && booking.status != "CANCELLED") {
                    OTPHighlightCard(otp = booking.otp)
                }

                // Partner / Driver Profile Card
                PartnerProfileCard(booking = booking)

                // Trip / Order Details Card
                OrderDetailsCard(booking = booking)

                // Safety & 24x7 Helpline Card
                SafetyHelplineCard(
                    onCallSupport = { showSupportToast = true }
                )

                // Actions: Advance Status (For demo testing) / Cancel / Return Home
                if (booking.status != "COMPLETED" && booking.status != "CANCELLED") {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SahayBorderLight)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "⚡ Interactive Simulation Controls",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SahayNavy
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.advanceBookingStatus(booking.id) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SahaySkyBlueDark,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text("Advance Status ➔", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = { viewModel.cancelBooking(booking.id) },
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Cancel", fontSize = 12.sp, color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onBackToHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("booking_back_home_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SahayNavy,
                        contentColor = Color.White
                    )
                ) {
                    Text("Back to Home Dashboard", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun StatusSummaryCard(booking: BookingEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SahayBorderLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(
                        when (booking.status) {
                            "COMPLETED" -> SahaySuccess
                            "CANCELLED" -> Color(0xFFEF4444)
                            else -> SahayNavy
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (booking.status) {
                        "COMPLETED" -> Icons.Default.Check
                        "CANCELLED" -> Icons.Default.Close
                        else -> Icons.Default.DirectionsCar
                    },
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = when (booking.status) {
                    "PENDING_CONFIRMATION" -> "Finding Nearest SAHAY Partner..."
                    "ASSIGNED" -> "Partner Assigned & En Route"
                    "IN_PROGRESS" -> "Service / Ride In Progress"
                    "COMPLETED" -> "Order Completed Successfully"
                    "CANCELLED" -> "Order Cancelled"
                    else -> booking.status
                },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = SahayNavy
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Order ID: ${booking.id} • ${booking.serviceType.replace("_", " ")}",
                fontSize = 11.5.sp,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Step timeline
            val steps = listOf("Confirmed", "Partner On Way", "Started", "Finished")
            val currentStepIndex = when (booking.status) {
                "PENDING_CONFIRMATION" -> 0
                "ASSIGNED" -> 1
                "IN_PROGRESS" -> 2
                "COMPLETED" -> 3
                else -> 0
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                steps.forEachIndexed { index, step ->
                    val isPastOrCurrent = index <= currentStepIndex && booking.status != "CANCELLED"
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(if (isPastOrCurrent) SahaySkyBlue else Color(0xFFCBD5E1)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isPastOrCurrent) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = step,
                            fontSize = 9.sp,
                            fontWeight = if (index == currentStepIndex) FontWeight.Bold else FontWeight.Normal,
                            color = if (index == currentStepIndex) SahayNavy else Color(0xFF94A3B8),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OTPHighlightCard(otp: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SahayChipBg.copy(alpha = 0.6f)),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, SahaySkyBlue)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "START SERVICE OTP",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = SahaySkyBlueLight,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Share this 4-digit PIN with partner upon arrival",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(SahayNavy)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = otp,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 4.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun PartnerProfileCard(booking: BookingEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SahayBorderLight)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(SahayChipBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👨🏽‍✈️", fontSize = 26.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = booking.partnerName,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = SahayNavy
                            )
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = SahayWarning,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${booking.partnerRating} ★ • ${booking.partnerVehicle}",
                                fontSize = 11.5.sp,
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Row {
                    IconButton(
                        onClick = { /* Simulated Call */ },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SahayChipBg)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = SahayNavy,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { /* Simulated Chat */ },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SahayChipBg)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "Chat",
                            tint = SahayNavy,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderDetailsCard(booking: BookingEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SahayBorderLight)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Order & Route Details",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = SahayNavy
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(10.dp)
                        .background(SahaySuccess, CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "Pickup Point", fontSize = 10.5.sp, color = Color(0xFF94A3B8))
                    Text(
                        text = booking.pickupAddress,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SahayNavy
                    )
                }
            }

            if (booking.dropAddress != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .size(10.dp)
                            .background(SahayNavy, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "Destination", fontSize = 10.5.sp, color = Color(0xFF94A3B8))
                        Text(
                            text = booking.dropAddress,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SahayNavy
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = SahayBorderLight)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Payment Mode", fontSize = 12.sp, color = Color(0xFF64748B))
                Text(text = booking.paymentMode, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SahayNavy)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Total Paid / Payable", fontSize = 12.sp, color = Color(0xFF64748B))
                Text(
                    text = "₹${"%.2f".format(booking.amount)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = SahayNavy
                )
            }
        }
    }
}

@Composable
private fun SafetyHelplineCard(
    onCallSupport: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SahayChipBg.copy(alpha = 0.4f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, SahayBorderLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = SahaySkyBlueDark,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "SAHAY 24x7 Safety Shield",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = SahayNavy
                    )
                    Text(
                        text = "Real-time emergency SOS & ride sharing",
                        fontSize = 10.5.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(SahayNavy)
                    .clickable { onCallSupport() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(text = "SOS Helpline", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
