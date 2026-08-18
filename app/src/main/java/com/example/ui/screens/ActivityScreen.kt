package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.BookingEntity
import com.example.ui.components.SahayEmptyState
import com.example.ui.components.SahayScreenTopBar
import com.example.ui.theme.SahayBorderLight
import com.example.ui.theme.SahayChipBg
import com.example.ui.theme.SahayNavy
import com.example.ui.theme.SahayNavyDark
import com.example.ui.theme.SahaySkyBlue
import com.example.ui.theme.SahaySkyBlueDark
import com.example.ui.theme.SahaySuccess
import com.example.ui.viewmodel.SahayViewModel

@Composable
fun ActivityScreen(
    viewModel: SahayViewModel,
    onNavigateToDetail: (String) -> Unit,
    onExploreServices: () -> Unit
) {
    val activeBookings by viewModel.activeBookings.collectAsState()
    val allBookings by viewModel.allBookings.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Active", "History")

    val completedOrCancelled = allBookings.filter { it.status == "COMPLETED" || it.status == "CANCELLED" }
    val displayList = if (selectedTabIndex == 0) activeBookings else completedOrCancelled

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("activity_screen"),
        color = Color(0xFFF8FAFC)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SahayScreenTopBar(
                title = "Your Activity & Orders",
                showBrandLogo = true
            )

            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = SahayNavy,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = SahaySkyBlueDark,
                        height = 3.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, tabTitle ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = tabTitle,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    )
                }
            }

            if (displayList.isEmpty()) {
                SahayEmptyState(
                    title = if (selectedTabIndex == 0) "No Ongoing Orders" else "No Order History Yet",
                    subtitle = if (selectedTabIndex == 0)
                        "You don't have any ongoing rides or deliveries right now."
                    else
                        "Your completed rides, grocery orders and home service bookings will appear here.",
                    actionText = "Book a Service",
                    onActionClick = onExploreServices,
                    modifier = Modifier.padding(top = 40.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(displayList) { booking ->
                        BookingItemCard(
                            booking = booking,
                            onClick = { onNavigateToDetail(booking.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BookingItemCard(
    booking: BookingEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("activity_item_${booking.id}"),
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
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SahayChipBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (booking.serviceType) {
                                "RIDE" -> Icons.Default.DirectionsCar
                                "COURIER" -> Icons.Default.LocalShipping
                                "GROCERY" -> Icons.Default.ShoppingBag
                                "HOME_SERVICE" -> Icons.Default.Build
                                "PHARMACY" -> Icons.Default.Medication
                                else -> Icons.Default.DirectionsCar
                            },
                            contentDescription = null,
                            tint = SahayNavy,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = booking.title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = SahayNavy
                            )
                        )
                        Text(
                            text = "Partner: ${booking.partnerName}",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when (booking.status) {
                                "COMPLETED" -> SahaySuccess.copy(alpha = 0.15f)
                                "CANCELLED" -> Color(0xFFEF4444).copy(alpha = 0.15f)
                                else -> SahaySkyBlue.copy(alpha = 0.2f)
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = booking.status.replace("_", " "),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (booking.status) {
                            "COMPLETED" -> SahaySuccess
                            "CANCELLED" -> Color(0xFFEF4444)
                            else -> SahayNavy
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "📍 ${booking.pickupAddress}",
                fontSize = 11.5.sp,
                color = Color(0xFF475569),
                maxLines = 1
            )
            if (booking.dropAddress != null) {
                Text(
                    text = "🎯 ${booking.dropAddress}",
                    fontSize = 11.5.sp,
                    color = Color(0xFF475569),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "OTP: ${booking.otp}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SahaySkyBlueDark
                )
                Text(
                    text = "₹${"%.2f".format(booking.amount)}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = SahayNavy
                    )
                )
            }
        }
    }
}
