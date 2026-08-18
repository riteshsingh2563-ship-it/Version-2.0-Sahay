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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HomeServiceItem
import com.example.ui.components.SahayScreenTopBar
import com.example.ui.theme.SahayBorderLight
import com.example.ui.theme.SahayChipBg
import com.example.ui.theme.SahayNavy
import com.example.ui.theme.SahayNavyDark
import com.example.ui.theme.SahaySkyBlue
import com.example.ui.theme.SahaySkyBlueDark
import com.example.ui.theme.SahaySuccess
import com.example.ui.theme.SahayWarning
import com.example.ui.viewmodel.SahayViewModel

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun HomeServicesScreen(
    viewModel: SahayViewModel,
    onBack: () -> Unit,
    onBookingCreated: (String) -> Unit
) {
    val services = viewModel.homeServicesCatalog
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedServiceForBooking by remember { mutableStateOf<HomeServiceItem?>(null) }
    var selectedSlot by remember { mutableStateOf("Today, 3:00 PM - 4:00 PM") }

    val categories = listOf("All", "AC & Appliances", "Electrician", "Plumbing", "Cleaning")
    val filteredServices = services.filter { selectedCategory == "All" || it.category == selectedCategory }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_services_screen"),
        color = Color(0xFFF8FAFC)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SahayScreenTopBar(
                title = "Home Services & Repairs",
                onBackClick = onBack,
                showBrandLogo = true
            )

            // Trust banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SahayChipBg)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.VerifiedUser,
                    contentDescription = null,
                    tint = SahaySkyBlueDark,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Police-verified experts • 30-Day service warranty • Rate card guarantee",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SahayNavy
                )
            }

            // Categories horizontal bar
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) SahayNavy else Color.White)
                            .border(1.dp, if (isSelected) SahayNavy else SahayBorderLight, RoundedCornerShape(20.dp))
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else SahayNavyDark
                        )
                    }
                }
            }

            // Services List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(filteredServices) { service ->
                    HomeServiceCard(
                        service = service,
                        onBookClick = {
                            selectedServiceForBooking = service
                        }
                    )
                }
            }
        }

        // Slot Selection Bottom Sheet / Modal Dialog
        if (selectedServiceForBooking != null) {
            val service = selectedServiceForBooking!!
            androidx.compose.material3.ModalBottomSheet(
                onDismissRequest = { selectedServiceForBooking = null },
                containerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Schedule ${service.title}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SahayNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Standard Fee: ₹${"%.0f".format(service.price)} • Est Duration: ${service.durationMinutes} mins",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Select Preferred Arrival Slot",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = SahayNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val slots = listOf(
                        "Today, 3:00 PM - 4:00 PM",
                        "Today, 5:00 PM - 6:00 PM",
                        "Tomorrow, 10:00 AM - 11:00 AM",
                        "Tomorrow, 2:00 PM - 3:00 PM"
                    )

                    slots.forEach { slot ->
                        val isSelected = selectedSlot == slot
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) SahayChipBg else Color.Transparent)
                                .border(1.dp, if (isSelected) SahaySkyBlue else SahayBorderLight, RoundedCornerShape(10.dp))
                                .clickable { selectedSlot = slot }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = if (isSelected) SahaySkyBlueDark else Color(0xFF94A3B8),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = slot,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) SahayNavy else Color(0xFF334155)
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = SahaySkyBlueDark,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            val bookedService = service
                            selectedServiceForBooking = null
                            viewModel.bookHomeService(
                                service = bookedService,
                                timeSlot = selectedSlot,
                                paymentMode = "SAHAY Wallet",
                                onSuccess = { booking ->
                                    onBookingCreated(booking.id)
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("confirm_home_service_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SahayNavy,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Confirm Booking (₹${"%.0f".format(service.price)})",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeServiceCard(
    service: HomeServiceItem,
    onBookClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("home_service_card_${service.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SahayBorderLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(SahayChipBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = service.emoji, fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = service.title,
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
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${service.rating} (${service.reviewsCount}+ reviews)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = service.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    color = Color(0xFF475569),
                    lineHeight = 17.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Inclusions chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                service.inclusions.take(2).forEach { inc ->
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFF1F5F9), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(text = "✓ $inc", fontSize = 10.5.sp, color = SahayNavyDark)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "₹${"%.0f".format(service.price)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = SahayNavy
                        )
                    )
                    Text(
                        text = "Standard fixed price",
                        fontSize = 10.sp,
                        color = Color(0xFF94A3B8)
                    )
                }

                Button(
                    onClick = onBookClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SahaySkyBlueDark,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.height(38.dp)
                ) {
                    Text(text = "Book Slot", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}
