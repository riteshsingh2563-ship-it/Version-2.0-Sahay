package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SahayNavy
import com.example.ui.theme.SahayNavyDark
import com.example.ui.theme.SahaySkyBlue
import com.example.ui.theme.SahaySkyBlueDark
import com.example.ui.theme.SahaySkyBlueLight
import kotlin.math.cos
import kotlin.math.sin

/**
 * High-fidelity official SAHAY Logo Component
 * Rendered faithfully matching the brand identity:
 * - Central modern ribbon 'S' with top location pin
 * - Handshake connection in the heart of 'S'
 * - Surrounding 6 circular service badges (Cab, Bike, Grocery, Home, Parcel, Pharmacy)
 * - Bold SAHAY typography with road/pin cutout in the second 'A'
 * - "— One App. Every Need. —" official tagline
 */
@Composable
fun SahayFullLogo(
    modifier: Modifier = Modifier,
    size: Dp = 220.dp,
    showTagline: Boolean = true,
    animated: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sahay_pulse")
    val pulseScale by if (animated) {
        infiniteTransition.animateFloat(
            initialValue = 0.98f,
            targetValue = 1.02f,
            animationSpec = infiniteRepeatable(
                animation = tween(1800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse_scale"
        )
    } else {
        rememberInfiniteTransition(label = "static").animateFloat(
            initialValue = 1f, targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(1000)),
            label = "static"
        )
    }

    Column(
        modifier = modifier
            .testTag("sahay_full_logo")
            .scale(pulseScale),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Central SAHAY Emblem with 6 Service Orbits
        SahayEmblemWithServices(size = size)

        Spacer(modifier = Modifier.height(14.dp))

        // Brand Typography: SAHAY
        SahayWordmark(fontSize = (size.value * 0.20f).sp)

        if (showTagline) {
            Spacer(modifier = Modifier.height(6.dp))
            SahayTagline(taglineFontSize = (size.value * 0.075f).sp)
        }
    }
}

/**
 * SAHAY Emblem with the 6 service orbits around the 'S' ribbon and location pin
 */
@Composable
fun SahayEmblemWithServices(
    size: Dp = 180.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .testTag("sahay_emblem"),
        contentAlignment = Alignment.Center
    ) {
        // Outer Blue circular boundary ring
        Canvas(modifier = Modifier.size(size * 0.82f)) {
            val strokeWidth = size.toPx() * 0.024f
            drawCircle(
                color = SahaySkyBlue.copy(alpha = 0.15f),
                radius = (size.toPx() * 0.82f) / 2f
            )
            drawCircle(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        SahaySkyBlue,
                        SahaySkyBlueLight,
                        SahayNavy,
                        SahaySkyBlue
                    )
                ),
                radius = (size.toPx() * 0.82f) / 2f - strokeWidth / 2,
                style = Stroke(width = strokeWidth)
            )
        }

        // Central S symbol with Pin & Handshake
        SahayCentralSSymbol(size = size * 0.65f)

        // 6 Surrounding Service Badges (matching official logo layout)
        val badgeSize = size * 0.20f
        val radiusOffset = size * 0.41f

        // 1. Top-Left: Rides / Car
        ServiceBadgeOrbit(
            icon = Icons.Default.DirectionsCar,
            angleDegrees = 205.0,
            radius = radiusOffset,
            badgeSize = badgeSize
        )

        // 2. Mid-Left: Bike Courier / Rapid Delivery
        ServiceBadgeOrbit(
            icon = Icons.Default.PedalBike,
            angleDegrees = 160.0,
            radius = radiusOffset,
            badgeSize = badgeSize
        )

        // 3. Bottom-Left: Grocery Shopping
        ServiceBadgeOrbit(
            icon = Icons.Default.ShoppingBag,
            angleDegrees = 120.0,
            radius = radiusOffset,
            badgeSize = badgeSize
        )

        // 4. Top-Right: Home Repair / Handyman Wrench
        ServiceBadgeOrbit(
            icon = Icons.Default.Build,
            angleDegrees = 335.0,
            radius = radiusOffset,
            badgeSize = badgeSize
        )

        // 5. Mid-Right: Parcel / Courier Box
        ServiceBadgeOrbit(
            icon = Icons.Default.LocalShipping,
            angleDegrees = 20.0,
            radius = radiusOffset,
            badgeSize = badgeSize
        )

        // 6. Bottom-Right: Pharmacy / Healthcare
        ServiceBadgeOrbit(
            icon = Icons.Default.Medication,
            angleDegrees = 65.0,
            radius = radiusOffset,
            badgeSize = badgeSize
        )
    }
}

@Composable
private fun ServiceBadgeOrbit(
    icon: ImageVector,
    angleDegrees: Double,
    radius: Dp,
    badgeSize: Dp
) {
    val angleRad = Math.toRadians(angleDegrees)
    val offsetX = (radius.value * cos(angleRad)).dp
    val offsetY = (radius.value * sin(angleRad)).dp

    Box(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
            .size(badgeSize)
            .shadow(2.dp, CircleShape)
            .clip(CircleShape)
            .background(Color.White)
            .border(1.2.dp, SahaySkyBlue.copy(alpha = 0.35f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = SahayNavy,
            modifier = Modifier.size(badgeSize * 0.58f)
        )
    }
}

/**
 * Central SAHAY 'S' ribbon with top location pin & handshake
 */
@Composable
fun SahayCentralSSymbol(
    size: Dp = 100.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .testTag("sahay_central_symbol"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height

            // Top Pin marker on top of S
            val pinPath = Path().apply {
                val pinTopY = h * 0.05f
                val pinCenterX = w * 0.50f
                val pinRadius = w * 0.16f

                moveTo(pinCenterX, pinTopY + pinRadius * 2.1f)
                cubicTo(
                    pinCenterX - pinRadius * 0.9f, pinTopY + pinRadius * 1.4f,
                    pinCenterX - pinRadius, pinTopY + pinRadius * 0.8f,
                    pinCenterX - pinRadius, pinTopY + pinRadius * 0.6f
                )
                cubicTo(
                    pinCenterX - pinRadius, pinTopY,
                    pinCenterX + pinRadius, pinTopY,
                    pinCenterX + pinRadius, pinTopY + pinRadius * 0.6f
                )
                cubicTo(
                    pinCenterX + pinRadius, pinTopY + pinRadius * 0.8f,
                    pinCenterX + pinRadius * 0.9f, pinTopY + pinRadius * 1.4f,
                    pinCenterX, pinTopY + pinRadius * 2.1f
                )
                close()
            }
            drawPath(
                path = pinPath,
                brush = Brush.verticalGradient(
                    colors = listOf(SahaySkyBlue, SahayNavy),
                    startY = 0f,
                    endY = h * 0.4f
                )
            )
            // Pin inner white dot
            drawCircle(
                color = Color.White,
                radius = w * 0.05f,
                center = Offset(w * 0.50f, h * 0.14f)
            )

            // Flowing 3D 'S' ribbon body
            // Upper S curve
            val sPathTop = Path().apply {
                moveTo(w * 0.48f, h * 0.28f)
                cubicTo(
                    w * 0.78f, h * 0.28f,
                    w * 0.86f, h * 0.44f,
                    w * 0.68f, h * 0.56f
                )
                cubicTo(
                    w * 0.52f, h * 0.64f,
                    w * 0.24f, h * 0.68f,
                    w * 0.24f, h * 0.82f
                )
                cubicTo(
                    w * 0.24f, h * 0.94f,
                    w * 0.44f, h * 0.96f,
                    w * 0.58f, h * 0.96f
                )
                cubicTo(
                    w * 0.76f, h * 0.96f,
                    w * 0.84f, h * 0.86f,
                    w * 0.86f, h * 0.80f
                )
                lineTo(w * 0.74f, h * 0.78f)
                cubicTo(
                    w * 0.72f, h * 0.86f,
                    w * 0.64f, h * 0.88f,
                    w * 0.56f, h * 0.88f
                )
                cubicTo(
                    w * 0.42f, h * 0.88f,
                    w * 0.36f, h * 0.82f,
                    w * 0.36f, h * 0.76f
                )
                cubicTo(
                    w * 0.36f, h * 0.68f,
                    w * 0.54f, h * 0.60f,
                    w * 0.74f, h * 0.52f
                )
                cubicTo(
                    w * 0.94f, h * 0.42f,
                    w * 0.86f, h * 0.22f,
                    w * 0.52f, h * 0.20f
                )
                close()
            }
            drawPath(
                path = sPathTop,
                brush = Brush.linearGradient(
                    colors = listOf(SahaySkyBlueLight, SahaySkyBlue, SahayNavy),
                    start = Offset(0f, 0f),
                    end = Offset(w, h)
                )
            )

            // Inner Handshake Symbol in the center of S
            val handshakeLeft = Path().apply {
                moveTo(w * 0.42f, h * 0.52f)
                lineTo(w * 0.54f, h * 0.62f)
                lineTo(w * 0.50f, h * 0.66f)
                lineTo(w * 0.38f, h * 0.56f)
                close()
            }
            drawPath(path = handshakeLeft, color = SahayNavy)

            val handshakeRight = Path().apply {
                moveTo(w * 0.58f, h * 0.52f)
                lineTo(w * 0.46f, h * 0.62f)
                lineTo(w * 0.50f, h * 0.66f)
                lineTo(w * 0.62f, h * 0.56f)
                close()
            }
            drawPath(path = handshakeRight, color = SahaySkyBlue)

            // Central handshake grip knuckles
            drawCircle(
                color = Color.White,
                radius = w * 0.02f,
                center = Offset(w * 0.50f, h * 0.58f)
            )
        }
    }
}

/**
 * SAHAY Wordmark: Bold navy typography with the signature stylized 'A'
 */
@Composable
fun SahayWordmark(
    modifier: Modifier = Modifier,
    fontSize: androidx.compose.ui.unit.TextUnit = 38.sp,
    textColor: Color = SahayNavy
) {
    Row(
        modifier = modifier.testTag("sahay_wordmark"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "SAH",
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = fontSize,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp,
                color = textColor
            )
        )
        // Stylized 'A' with Road & Pin motif
        Box(
            modifier = Modifier
                .height((fontSize.value * 1.15f).dp)
                .width((fontSize.value * 0.95f).dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size((fontSize.value * 0.95f).dp)) {
                val w = size.width
                val h = size.height

                // Outer A triangle
                val aPath = Path().apply {
                    moveTo(w * 0.50f, h * 0.08f)
                    lineTo(w * 0.96f, h * 0.92f)
                    lineTo(w * 0.72f, h * 0.92f)
                    lineTo(w * 0.58f, h * 0.64f)
                    lineTo(w * 0.42f, h * 0.64f)
                    lineTo(w * 0.28f, h * 0.92f)
                    lineTo(w * 0.04f, h * 0.92f)
                    close()
                }
                drawPath(path = aPath, color = textColor)

                // Inner keyhole / pin & road cutout in 'A'
                drawCircle(
                    color = Color.White,
                    radius = w * 0.09f,
                    center = Offset(w * 0.50f, h * 0.38f)
                )
                val roadPath = Path().apply {
                    moveTo(w * 0.45f, h * 0.44f)
                    lineTo(w * 0.55f, h * 0.44f)
                    lineTo(w * 0.60f, h * 0.92f)
                    lineTo(w * 0.40f, h * 0.92f)
                    close()
                }
                drawPath(path = roadPath, color = Color.White)
                // Road dotted center
                drawLine(
                    color = textColor,
                    start = Offset(w * 0.50f, h * 0.55f),
                    end = Offset(w * 0.50f, h * 0.85f),
                    strokeWidth = w * 0.04f,
                    cap = StrokeCap.Round
                )
            }
        }
        Text(
            text = "Y",
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = fontSize,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp,
                color = textColor
            )
        )
    }
}

/**
 * Official Tagline: "— One App. Every Need. —"
 */
@Composable
fun SahayTagline(
    modifier: Modifier = Modifier,
    taglineFontSize: androidx.compose.ui.unit.TextUnit = 13.sp,
    lineColor: Color = SahaySkyBlue,
    textColor: Color = SahayNavyDark
) {
    Row(
        modifier = modifier.testTag("sahay_tagline"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Left gradient accent bar
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(2.5.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, lineColor)
                    ),
                    RoundedCornerShape(2.dp)
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "One App. Every Need.",
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = taglineFontSize,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                color = textColor
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(8.dp))
        // Right gradient accent bar
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(2.5.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(lineColor, Color.Transparent)
                    ),
                    RoundedCornerShape(2.dp)
                )
        )
    }
}

/**
 * Compact horizontal Brand Header lockup for AppBars, Cards, and Dialogs
 */
@Composable
fun SahayCompactHeader(
    modifier: Modifier = Modifier,
    height: Dp = 38.dp,
    showTagline: Boolean = false
) {
    Row(
        modifier = modifier.testTag("sahay_compact_header"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        // Mini Symbol Badge
        Box(
            modifier = Modifier
                .size(height)
                .shadow(1.dp, CircleShape)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, SahaySkyBlue.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            SahayCentralSSymbol(size = height * 0.78f)
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            SahayWordmark(
                fontSize = (height.value * 0.58f).sp,
                textColor = SahayNavy
            )
            if (showTagline) {
                Text(
                    text = "One App. Every Need.",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = SahaySkyBlueDark,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
    }
}
