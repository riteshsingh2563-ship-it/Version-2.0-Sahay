package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SahayBorderLight
import com.example.ui.theme.SahayChipBg
import com.example.ui.theme.SahayNavy
import com.example.ui.theme.SahayNavyDark
import com.example.ui.theme.SahaySkyBlue
import com.example.ui.theme.SahaySkyBlueDark
import com.example.ui.theme.SahaySkyBlueLight

/**
 * Official Branded Loading Screen with SAHAY symbol pulse animation
 */
@Composable
fun SahayLoadingScreen(
    message: String = "Fetching best SAHAY options for you..."
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loader")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .testTag("sahay_loading_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .scale(pulseScale)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, SahaySkyBlue.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                SahayCentralSSymbol(size = 70.dp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            SahayWordmark(fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            SahayTagline(taglineFontSize = 10.sp)

            Spacer(modifier = Modifier.height(24.dp))

            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                color = SahaySkyBlue,
                strokeWidth = 2.5.dp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF64748B)
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Official Branded Empty State with SAHAY logo mark
 */
@Composable
fun SahayEmptyState(
    modifier: Modifier = Modifier,
    title: String = "No Items Found",
    subtitle: String = "Looks like there's nothing here yet. Explore our one-stop super app services!",
    actionText: String? = "Explore Services",
    onActionClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("sahay_empty_state"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SahayBorderLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .shadow(3.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.5.dp, SahaySkyBlue.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                SahayCentralSSymbol(size = 56.dp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = SahayNavy
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF64748B),
                    lineHeight = 18.sp
                ),
                textAlign = TextAlign.Center
            )

            if (actionText != null && onActionClick != null) {
                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = onActionClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SahayNavy,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.height(44.dp)
                ) {
                    Text(text = actionText, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

/**
 * Top App Bar with SAHAY Brand Header, back button, and actions
 */
@Composable
fun SahayScreenTopBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    showBrandLogo: Boolean = true,
    trailingContent: (@Composable () -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                if (onBackClick != null) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("top_bar_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = SahayNavy
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                if (showBrandLogo) {
                    SahayCompactHeader(height = 30.dp, showTagline = false)
                } else {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SahayNavy
                        )
                    )
                }
            }

            if (trailingContent != null) {
                trailingContent()
            }
        }
    }
}

/**
 * SAHAY Bottom Navigation Bar
 */
@Composable
fun SahayBottomNav(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    unreadNotifications: Int = 0
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 6.dp,
        modifier = Modifier.testTag("sahay_bottom_nav")
    ) {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { onNavigate("home") },
            icon = {
                Icon(
                    imageVector = if (currentRoute == "home") Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SahayNavy,
                selectedTextColor = SahayNavy,
                indicatorColor = SahayChipBg,
                unselectedIconColor = Color(0xFF64748B),
                unselectedTextColor = Color(0xFF64748B)
            ),
            modifier = Modifier.testTag("nav_item_home")
        )

        NavigationBarItem(
            selected = currentRoute == "activity",
            onClick = { onNavigate("activity") },
            icon = {
                Icon(
                    imageVector = if (currentRoute == "activity") Icons.Filled.History else Icons.Outlined.History,
                    contentDescription = "Orders"
                )
            },
            label = { Text("Activity", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SahayNavy,
                selectedTextColor = SahayNavy,
                indicatorColor = SahayChipBg,
                unselectedIconColor = Color(0xFF64748B),
                unselectedTextColor = Color(0xFF64748B)
            ),
            modifier = Modifier.testTag("nav_item_activity")
        )

        NavigationBarItem(
            selected = currentRoute == "wallet",
            onClick = { onNavigate("wallet") },
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = "Wallet"
                )
            },
            label = { Text("Wallet", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SahayNavy,
                selectedTextColor = SahayNavy,
                indicatorColor = SahayChipBg,
                unselectedIconColor = Color(0xFF64748B),
                unselectedTextColor = Color(0xFF64748B)
            ),
            modifier = Modifier.testTag("nav_item_wallet")
        )

        NavigationBarItem(
            selected = currentRoute == "notifications",
            onClick = { onNavigate("notifications") },
            icon = {
                BadgedBox(
                    badge = {
                        if (unreadNotifications > 0) {
                            Badge(containerColor = SahaySkyBlueDark) {
                                Text(unreadNotifications.toString(), color = Color.White)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (currentRoute == "notifications") Icons.Filled.Notifications else Icons.Outlined.Notifications,
                        contentDescription = "Notifications"
                    )
                }
            },
            label = { Text("Alerts", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SahayNavy,
                selectedTextColor = SahayNavy,
                indicatorColor = SahayChipBg,
                unselectedIconColor = Color(0xFF64748B),
                unselectedTextColor = Color(0xFF64748B)
            ),
            modifier = Modifier.testTag("nav_item_notifications")
        )

        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { onNavigate("profile") },
            icon = {
                Icon(
                    imageVector = if (currentRoute == "profile") Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Profile"
                )
            },
            label = { Text("Profile", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SahayNavy,
                selectedTextColor = SahayNavy,
                indicatorColor = SahayChipBg,
                unselectedIconColor = Color(0xFF64748B),
                unselectedTextColor = Color(0xFF64748B)
            ),
            modifier = Modifier.testTag("nav_item_profile")
        )
    }
}
