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

private val DarkColorScheme =
  darkColorScheme(
    primary = Color(0xFF669DF6), // Accessible lighter blue for dark mode
    onPrimary = Color(0xFF0F172A),
    primaryContainer = Color(0xFF1557B0),
    onPrimaryContainer = Color(0xFFD2E3FC),
    secondary = Color(0xFFFA903E), // Accessible lighter orange for dark mode
    onSecondary = Color(0xFF4A1E00),
    secondaryContainer = Color(0xFFB06000),
    onSecondaryContainer = Color(0xFFFFDDC1),
    tertiary = Color(0xFF81C784),
    onTertiary = Color(0xFF0C3810),
    tertiaryContainer = Color(0xFF13521F),
    onTertiaryContainer = Color(0xFFE6F4EA),
    background = Color(0xFF0F172A), // Deep Slate 900 for dark minimalist depth
    onBackground = Color(0xFFF1F5F9), // Slate 100
    surface = Color(0xFF1E293B), // Slate 800
    onSurface = Color(0xFFF8FAFC), // Slate 50
    surfaceVariant = Color(0xFF334155), // Slate 700
    onSurfaceVariant = Color(0xFF94A3B8), // Slate 400
    outline = Color(0xFF475569), // Slate 600
    outlineVariant = Color(0xFF334155) // Slate 700
  )

private val LightColorScheme =
  lightColorScheme(
    primary = RepublicBlue, // 0xFF1A73E8 - Clean minimalist blue
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8F0FE), // Subtle soft blue container
    onPrimaryContainer = RepublicBlue,
    secondary = RepublicOrange, // 0xFFF27D26 - Brand accent orange
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFF1E4), // Soft peach/orange container
    onSecondaryContainer = Color(0xFFC45100),
    tertiary = RepublicTeal, // 0xFF1E8E3E - Brand teal
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE6F4EA), // Soft mint container
    onTertiaryContainer = RepublicTeal,
    background = LightSlate, // 0xFFF7F9FC - Very clean slate white background
    onBackground = Color(0xFF1E293B), // Slate 900 for crisp text contrast
    surface = Color.White, // Standard clean surfaces
    onSurface = Color(0xFF1E293B), // Slate 900 text
    surfaceVariant = Color(0xFFF1F5F9), // Slate 100
    onSurfaceVariant = Color(0xFF64748B), // Slate 500
    outline = Color(0xFFCBD5E1), // Slate 300 subtle borders
    outlineVariant = Color(0xFFE2E8F0) // Slate 200 light dividers
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is disabled by default to keep the precise Clean Minimalism color identity
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
