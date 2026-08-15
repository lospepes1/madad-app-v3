package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.localization.LanguageManager
import com.example.ui.components.InstagramIconButton
import com.example.ui.components.ThemeToggleButton
import com.example.ui.theme.AppTheme

@Composable
fun WelcomeLanguageScreen(
    selectedLanguage: AppLanguage,
    isDarkMode: Boolean = true,
    onToggleDarkMode: () -> Unit = {},
    onLanguageSelected: (AppLanguage) -> Unit,
    onStartClick: () -> Unit
) {
    val colors = AppTheme.colors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(24.dp)
            .testTag("welcome_language_screen")
    ) {
        // Decorative glowing background circle
        Box(
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-40).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(colors.primaryAccent.copy(alpha = if (isDarkMode) 0.25f else 0.12f), Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar: Theme Toggle + Instagram Profile Quick Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ThemeToggleButton(
                    isDark = isDarkMode,
                    onToggle = onToggleDarkMode,
                    language = selectedLanguage,
                    compact = true
                )

                InstagramIconButton()
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Logo & Title Block
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(colors.cardBackgroundOpaque)
                        .border(2.dp, colors.primaryAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = "Midad Logo",
                        tint = colors.primaryAccent,
                        modifier = Modifier.size(42.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "مِداد",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    color = colors.primaryAccent,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = LanguageManager.welcomeTitle(selectedLanguage),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = LanguageManager.welcomeSlogan(selectedLanguage),
                    fontSize = 13.sp,
                    color = colors.textSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Language Selector Cards
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = LanguageManager.selectLanguageHeader(selectedLanguage),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textSecondary,
                    modifier = Modifier.padding(bottom = 2.dp)
                )

                AppLanguage.entries.forEach { lang ->
                    val isSelected = selectedLanguage == lang
                    val borderColor = if (isSelected) colors.primaryAccent else colors.cardBorder
                    val bgColor = if (isSelected) colors.cardBackgroundOpaque else colors.cardBackground

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
                            .clickable { onLanguageSelected(lang) }
                            .testTag("lang_card_${lang.code}"),
                        color = bgColor
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 13.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(text = lang.flag, fontSize = 22.sp)
                                Text(
                                    text = lang.displayName,
                                    fontSize = 16.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = colors.textPrimary
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = colors.primaryAccent,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Button
            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("start_now_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primaryAccent,
                    contentColor = if (isDarkMode) Color.Black else Color.White
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = LanguageManager.btnStartNow(selectedLanguage),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
