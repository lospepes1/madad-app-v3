package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MidadColorScheme = darkColorScheme(
    primary = NeonGreenAccent,
    onPrimary = DarkBackground,
    primaryContainer = DarkEmeraldCard,
    onPrimaryContainer = BrightNeonGreen,
    secondary = BrightNeonGreen,
    onSecondary = DarkBackground,
    secondaryContainer = DarkEmeraldCardBorder,
    onSecondaryContainer = TextPrimaryWhite,
    background = DarkBackground,
    onBackground = TextPrimaryWhite,
    surface = DarkSurface,
    onSurface = TextPrimaryWhite,
    surfaceVariant = DarkEmeraldCard,
    onSurfaceVariant = TextSecondaryGray,
    outline = DarkEmeraldCardBorder,
    error = ErrorRed,
    onError = TextPrimaryWhite
)

@Composable
fun MidadTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MidadColorScheme,
        typography = Typography,
        content = content
    )
}

