package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.localization.LanguageManager
import com.example.ui.theme.AppTheme

@Composable
fun ThemeToggleButton(
    isDark: Boolean,
    onToggle: () -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val colors = AppTheme.colors
    val label = LanguageManager.toggleThemeLabel(language, isDark)

    val buttonBg by animateColorAsState(
        targetValue = if (isDark) Color(0xF00A1F13) else Color(0xFFE2EFE7),
        animationSpec = tween(300),
        label = "themeBg"
    )

    val iconBg by animateColorAsState(
        targetValue = if (isDark) Color(0xFF1E3A2B) else Color(0xFFCCE8D6),
        animationSpec = tween(300),
        label = "iconBg"
    )

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = 1.2.dp,
                color = colors.cardBorder,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onToggle() }
            .testTag("theme_toggle_btn"),
        color = buttonBg,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = if (compact) 8.dp else 14.dp,
                vertical = if (compact) 4.dp else 7.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(if (compact) 24.dp else 28.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                if (isDark) {
                    Icon(
                        imageVector = Icons.Default.LightMode,
                        contentDescription = label,
                        tint = Color(0xFFFFD54F),
                        modifier = Modifier.size(if (compact) 15.dp else 18.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.DarkMode,
                        contentDescription = label,
                        tint = Color(0xFF0F172A),
                        modifier = Modifier.size(if (compact) 15.dp else 18.dp)
                    )
                }
            }

            if (!compact) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isDark) LanguageManager.lightModeLabel(language) else LanguageManager.darkModeLabel(language),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary
                )
            }
        }
    }
}
