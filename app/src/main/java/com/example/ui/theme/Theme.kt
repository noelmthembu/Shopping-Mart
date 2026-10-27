package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF007A3D),
    secondary = Color(0xFF002F6C),
    tertiary = Color(0xFFD8232A),
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFF334155),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFF8FAFC)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF007A3D), // South African Retail Green
    secondary = Color(0xFF002F6C), // Pick n Pay Blue
    tertiary = Color(0xFFD8232A), // Shoprite Red
    background = Color(0xFFF3F5F7), // Crisp bright flyer background
    surface = Color.White,
    surfaceVariant = Color(0xFFEEF2F6),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1E293B),
    onSurface = Color(0xFF0F172A),
    primaryContainer = Color(0xFFE8F5E9),
    onPrimaryContainer = Color(0xFF1B5E20),
    secondaryContainer = Color(0xFFE0F2FE),
    onSecondaryContainer = Color(0xFF0369A1),
    tertiaryContainer = Color(0xFFFFEBEE),
    onTertiaryContainer = Color(0xFFC62828)
)

@Composable
fun SmartShopperTheme(
    darkTheme: Boolean = false, // Default to bright supermarket flyer theme
    dynamicColor: Boolean = false, // Keep authentic South African retail colors
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
