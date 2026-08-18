package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = SahayNavy,
    onPrimary = Color.White,
    primaryContainer = SahayChipBg,
    onPrimaryContainer = SahayNavyDark,
    secondary = SahaySkyBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F2FE),
    onSecondaryContainer = Color(0xFF0369A1),
    tertiary = SahayAccentCyan,
    onTertiary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = SahayTextPrimary,
    surface = Color.White,
    onSurface = SahayTextPrimary,
    surfaceVariant = Color(0xFFF0F4F9),
    onSurfaceVariant = SahayTextSecondary,
    outline = SahayBorderLight,
    outlineVariant = Color(0xFFCBD5E1),
    error = SahayError,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = SahaySkyBlueLight,
    onPrimary = SahayNavyDark,
    primaryContainer = SahayNavyLight,
    onPrimaryContainer = Color(0xFFE0F2FE),
    secondary = SahaySkyBlue,
    onSecondary = SahayNavyDark,
    secondaryContainer = Color(0xFF075985),
    onSecondaryContainer = Color(0xFFBAE6FD),
    tertiary = SahayAccentCyan,
    onTertiary = Color.Black,
    background = Color(0xFF0A0F1D),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF111827),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF334155),
    outlineVariant = Color(0xFF1E293B),
    error = Color(0xFFF87171),
    onError = Color.Black
)

@Composable
fun SahayTheme(
    darkTheme: Boolean = false, // Keep clean white theme default as requested
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = Color.Transparent.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
