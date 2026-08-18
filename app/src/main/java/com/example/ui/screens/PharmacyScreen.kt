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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.UploadFile
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
import com.example.ui.components.SahayScreenTopBar
import com.example.ui.theme.SahayBorderLight
import com.example.ui.theme.SahayChipBg
import com.example.ui.theme.SahayNavy
import com.example.ui.theme.SahayNavyDark
import com.example.ui.theme.SahaySkyBlue
import com.example.ui.theme.SahaySkyBlueDark
import com.example.ui.theme.SahaySkyBlueLight
import com.example.ui.theme.SahaySuccess
import com.example.ui.viewmodel.SahayViewModel

data class PharmaProduct(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val prescriptionRequired: Boolean,
    val emoji: String
)

@Composable
fun PharmacyScreen(
    viewModel: SahayViewModel,
    onBack: () -> Unit,
    onBookingCreated: (String) -> Unit
) {
    val items = listOf(
        PharmaProduct("p1", "Paracetamol 650mg (Strip of 15)", "Fever & body ache relief", 38.0, false, "💊"),
        PharmaProduct("p2", "Vitamin C + Zinc Chewable (60 tabs)", "Immunity booster tablets", 195.0, false, "🍊"),
        PharmaProduct("p3", "First Aid Antiseptic Liquid (250ml)", "Wound cleansing & healing", 110.0, false, "🩹"),
        PharmaProduct("p4", "Digital Infrared Forehead Thermometer", "Fast 1-second temperature read", 649.0, false, "🌡️"),
        PharmaProduct("p5", "Electrolyte Hydration Sachet (Pack of 5)", "Rapid energy & hydration", 85.0, false, "⚡"),
        PharmaProduct("p6", "Prescription Antibiotics & Chronic Care", "Dispensed only with valid prescription", 299.0, true, "📋")
    )

    var selectedProducts by remember { mutableStateOf(setOf<String>()) }
    var prescriptionUploaded by remember { mutableStateOf(false) }

    val totalAmount = items.filter { selectedProducts.contains(it.id) }.sumOf { it.price }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("pharmacy_screen"),
        color = Color(0xFFF8FAFC)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SahayScreenTopBar(
                title = "SAHAY Pharmacy Express",
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
                // Header Banner
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SahayNavy)
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
                                    .clip(CircleShape)
                                    .background(SahaySkyBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalPharmacy,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "100% Genuine Pharmacy Deliveries",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                                Text(
                                    text = "Verified licensed pharmacists • 20 mins delivery",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = SahaySkyBlueLight
                                    )
                                )
                            }
                        }
                    }
                }

                // Quick Prescription Upload Box
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                prescriptionUploaded = !prescriptionUploaded
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SahayChipBg.copy(alpha = 0.5f)),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, SahaySkyBlue)
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
                                    imageVector = if (prescriptionUploaded) Icons.Default.Check else Icons.Default.UploadFile,
                                    contentDescription = null,
                                    tint = if (prescriptionUploaded) SahaySuccess else SahaySkyBlueDark,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (prescriptionUploaded) "Prescription Attached: Rx_doc_2026.pdf" else "Have a Doctor's Prescription?",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SahayNavy
                                    )
                                    Text(
                                        text = if (prescriptionUploaded) "Tap to change or remove" else "Tap to upload photo or PDF for pharmacist review",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }
                        }
                    }
                }

                // Essential Meds List
                items(items) { item ->
                    val isChecked = selectedProducts.contains(item.id)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedProducts = if (isChecked) selectedProducts - item.id else selectedProducts + item.id
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isChecked) 2.dp else 1.dp,
                            color = if (isChecked) SahaySkyBlue else SahayBorderLight
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
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(SahayChipBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = item.emoji, fontSize = 22.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = SahayNavy
                                        )
                                    )
                                    Text(
                                        text = item.description,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp,
                                            color = Color(0xFF64748B)
                                        )
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "₹${"%.0f".format(item.price)}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = SahayNavy
                                    )
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isChecked) SahayNavy else SahayChipBg)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = if (isChecked) "Added ✓" else "+ ADD",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isChecked) Color.White else SahayNavy
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // Checkout Bar
            if (selectedProducts.isNotEmpty() || prescriptionUploaded) {
                val finalPay = if (totalAmount > 0) totalAmount else 99.0
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
                                text = "${selectedProducts.size} Items ${if (prescriptionUploaded) "+ Rx" else ""}",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = "₹${"%.0f".format(finalPay)}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = SahayNavy
                                )
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.bookPharmacyOrder(
                                    itemCount = selectedProducts.size.coerceAtLeast(1),
                                    amount = finalPay,
                                    hasPrescription = prescriptionUploaded,
                                    paymentMode = "SAHAY Wallet",
                                    onSuccess = { booking ->
                                        onBookingCreated(booking.id)
                                    }
                                )
                            },
                            modifier = Modifier
                                .height(48.dp)
                                .testTag("confirm_pharmacy_btn"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SahayNavy,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Deliver Medicines",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
