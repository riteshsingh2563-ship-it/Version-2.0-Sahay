package com.example.ui.screens

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
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SahayScreenTopBar
import com.example.ui.theme.SahayBorderLight
import com.example.ui.theme.SahayChipBg
import com.example.ui.theme.SahayNavy
import com.example.ui.theme.SahayNavyDark
import com.example.ui.theme.SahaySkyBlue
import com.example.ui.theme.SahaySkyBlueDark
import com.example.ui.theme.SahaySkyBlueLight
import com.example.ui.viewmodel.SahayViewModel

data class PackageCategory(
    val id: String,
    val name: String,
    val icon: ImageVector
)

@Composable
fun CourierScreen(
    viewModel: SahayViewModel,
    onBack: () -> Unit,
    onBookingCreated: (String) -> Unit
) {
    val savedAddress by viewModel.currentAddress.collectAsState()
    var pickupAddress by remember { mutableStateOf(savedAddress) }
    var dropAddress by remember { mutableStateOf("Prestige Ozone, Whitefield Main Road") }
    var recipientName by remember { mutableStateOf("Sunita Rao") }
    var recipientPhone by remember { mutableStateOf("+91 99887 76655") }

    var selectedPkgCategory by remember { mutableStateOf("Documents & Files") }
    var weightTier by remember { mutableStateOf("Up to 1 kg") }
    var isExpressDelivery by remember { mutableStateOf(true) }

    val categories = listOf(
        PackageCategory("doc", "Documents & Files", Icons.Default.Description),
        PackageCategory("elec", "Gadgets & Cables", Icons.Default.Devices),
        PackageCategory("box", "Gift & Box", Icons.Default.CardGiftcard),
        PackageCategory("food", "Homemade Food", Icons.Default.Fastfood),
        PackageCategory("med", "Medicines", Icons.Default.MedicalServices)
    )

    val baseFare = when (weightTier) {
        "Up to 1 kg" -> 69.0
        "1 - 5 kg" -> 119.0
        else -> 199.0
    }
    val totalFare = baseFare + if (isExpressDelivery) 30.0 else 0.0

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("courier_screen"),
        color = Color(0xFFF8FAFC)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SahayScreenTopBar(
                title = "Package Courier",
                onBackClick = onBack,
                showBrandLogo = true
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SahayNavy),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(SahaySkyBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalShipping,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Instant Point-to-Point Courier",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Delivered safely with OTP verification & live tracking",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = SahaySkyBlueLight
                                )
                            )
                        }
                    }
                }

                // Locations Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SahayBorderLight)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Pickup & Drop Details",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = SahayNavy
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = pickupAddress,
                            onValueChange = { pickupAddress = it },
                            label = { Text("Sender / Pickup Location") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SahaySkyBlue,
                                unfocusedBorderColor = SahayBorderLight
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = dropAddress,
                            onValueChange = { dropAddress = it },
                            label = { Text("Recipient / Drop Location") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SahaySkyBlue,
                                unfocusedBorderColor = SahayBorderLight
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = recipientName,
                                onValueChange = { recipientName = it },
                                label = { Text("Recipient Name") },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SahaySkyBlue,
                                    unfocusedBorderColor = SahayBorderLight
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = recipientPhone,
                                onValueChange = { recipientPhone = it },
                                label = { Text("Recipient Phone") },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SahaySkyBlue,
                                    unfocusedBorderColor = SahayBorderLight
                                )
                            )
                        }
                    }
                }

                // Package Type Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SahayBorderLight)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "What are you sending?",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = SahayNavy
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        categories.forEach { cat ->
                            val isSelected = selectedPkgCategory == cat.name
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) SahayChipBg else Color.Transparent)
                                    .clickable { selectedPkgCategory = cat.name }
                                    .padding(vertical = 8.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = cat.icon,
                                    contentDescription = null,
                                    tint = if (isSelected) SahayNavy else Color(0xFF64748B),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = cat.name,
                                    fontSize = 13.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) SahayNavy else Color(0xFF334155)
                                )
                            }
                        }
                    }
                }

                // Express Delivery toggle
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
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
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(SahayChipBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = null,
                                    tint = SahayNavy,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "SAHAY Flash Express (25 mins)",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SahayNavy
                                )
                                Text(
                                    text = "Direct non-stop courier (+₹30)",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        Switch(
                            checked = isExpressDelivery,
                            onCheckedChange = { isExpressDelivery = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = SahaySkyBlue
                            )
                        )
                    }
                }
            }

            // Bottom Floating Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Courier Total",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                        Text(
                            text = "₹${"%.0f".format(totalFare)}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = SahayNavy
                            )
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.bookCourierDelivery(
                                packageType = selectedPkgCategory,
                                weightKg = weightTier,
                                pickup = pickupAddress,
                                drop = dropAddress,
                                isExpress = isExpressDelivery,
                                amount = totalFare,
                                paymentMode = "SAHAY Wallet",
                                onSuccess = { booking ->
                                    onBookingCreated(booking.id)
                                }
                            )
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .testTag("confirm_courier_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SahayNavy,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Confirm Delivery",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
