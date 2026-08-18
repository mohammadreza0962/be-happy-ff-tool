package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val GamingColorScheme = darkColorScheme(
    primary = AmberFlame,
    onPrimary = TextPrimary,
    primaryContainer = AmberFlameDark,
    onPrimaryContainer = TextPrimary,
    secondary = EmeraldPro,
    onSecondary = TextPrimary,
    secondaryContainer = EmeraldProDark,
    onSecondaryContainer = TextPrimary,
    tertiary = CyanTech,
    onTertiary = TextPrimary,
    background = NavyBackground,
    onBackground = TextPrimary,
    surface = NavySurface,
    onSurface = TextPrimary,
    surfaceVariant = NavySurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = NavyCardBorder,
    error = DangerRed,
    onError = TextPrimary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = GamingColorScheme,
        typography = Typography,
        content = content
    )
}

