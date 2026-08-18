package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SahayFullLogo
import com.example.ui.theme.SahayNavy
import com.example.ui.theme.SahaySkyBlue
import com.example.ui.theme.SahaySkyBlueLight
import kotlinx.coroutines.delay

/**
 * Premium White Splash Screen with:
 * - Centered official SAHAY logo
 * - "One App. Every Need." tagline
 * - Very subtle blue ambient glowing animation
 * - Minimal, clean layout
 */
@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val scale = remember { Animatable(0.85f) }
    val alpha = remember { Animatable(0f) }
    val ringScale = remember { Animatable(0.6f) }
    val ringAlpha = remember { Animatable(0.6f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
        )
    }

    LaunchedEffect(Unit) {
        ringScale.animateTo(
            targetValue = 1.35f,
            animationSpec = tween(durationMillis = 1800, easing = FastOutSlowInEasing)
        )
    }

    LaunchedEffect(Unit) {
        ringAlpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 1800, easing = FastOutSlowInEasing)
        )
    }

    LaunchedEffect(Unit) {
        delay(2200)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Subtle ambient blue pulse ring in background
        Box(
            modifier = Modifier
                .size(320.dp)
                .scale(ringScale.value)
                .alpha(ringAlpha.value)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            SahaySkyBlue.copy(alpha = 0.25f),
                            SahaySkyBlueLight.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Centered SAHAY Logo & Tagline
        Column(
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SahayFullLogo(
                size = 230.dp,
                showTagline = true,
                animated = true
            )
        }

        // Bottom subtle brand footer
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp)
                .alpha(alpha.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Trusted by Millions • Unified Super-App",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = SahayNavy.copy(alpha = 0.55f),
                letterSpacing = 0.5.sp
            )
        }
    }
}
