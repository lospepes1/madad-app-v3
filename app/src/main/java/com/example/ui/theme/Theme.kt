package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val isDark: Boolean,
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val cardBackground: Color,
    val cardBackgroundOpaque: Color,
    val cardBorder: Color,
    val primaryAccent: Color,
    val brightAccent: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val glassBorder: Color,
    val error: Color,
    val warning: Color,
    val success: Color,
    val topBarBackground: Color,
    val bottomBarBackground: Color
)

val DarkAppColors = AppColors(
    isDark = true,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkEmeraldCardOpaque,
    cardBackground = DarkEmeraldCard,
    cardBackgroundOpaque = DarkEmeraldCardOpaque,
    cardBorder = DarkEmeraldCardBorder,
    primaryAccent = NeonGreenAccent,
    brightAccent = BrightNeonGreen,
    textPrimary = TextPrimaryWhite,
    textSecondary = TextSecondaryGray,
    textMuted = TextMuted,
    glassBorder = GlassBorderWhite,
    error = ErrorRed,
    warning = WarningYellow,
    success = SuccessGreen,
    topBarBackground = DarkBackground,
    bottomBarBackground = DarkBackground
)

val LightAppColors = AppColors(
    isDark = false,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightEmeraldCardOpaque,
    cardBackground = LightEmeraldCard,
    cardBackgroundOpaque = LightEmeraldCardOpaque,
    cardBorder = LightEmeraldCardBorder,
    primaryAccent = LightPrimaryAccent,
    brightAccent = LightBrightAccent,
    textPrimary = TextPrimaryDark,
    textSecondary = TextSecondaryDark,
    textMuted = TextMutedDark,
    glassBorder = GlassBorderDark,
    error = ErrorRed,
    warning = WarningYellow,
    success = LightPrimaryAccent,
    topBarBackground = LightSurface,
    bottomBarBackground = LightSurface
)

val LocalAppColors = staticCompositionLocalOf { DarkAppColors }

object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current
}

private val MidadDarkColorScheme: ColorScheme = darkColorScheme(
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

private val MidadLightColorScheme: ColorScheme = lightColorScheme(
    primary = LightPrimaryAccent,
    onPrimary = Color.White,
    primaryContainer = LightEmeraldCardOpaque,
    onPrimaryContainer = LightBrightAccent,
    secondary = LightBrightAccent,
    onSecondary = Color.White,
    secondaryContainer = LightEmeraldCardBorder,
    onSecondaryContainer = TextPrimaryDark,
    background = LightBackground,
    onBackground = TextPrimaryDark,
    surface = LightSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = LightEmeraldCardOpaque,
    onSurfaceVariant = TextSecondaryDark,
    outline = LightEmeraldCardBorder,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun MidadTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val appColors = if (darkTheme) DarkAppColors else LightAppColors
    val colorScheme = if (darkTheme) MidadDarkColorScheme else MidadLightColorScheme

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}


