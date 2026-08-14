package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
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
import com.example.data.model.AnalysisResult
import com.example.data.model.GymLevel
import com.example.data.model.UserProfile
import com.example.localization.LanguageManager
import com.example.ui.components.InstagramFooter
import com.example.ui.components.ThemeToggleButton
import com.example.ui.theme.AppTheme

@Composable
fun AnalysisGymLevelScreen(
    userProfile: UserProfile,
    analysisResult: AnalysisResult?,
    isLoading: Boolean,
    isDarkMode: Boolean = true,
    onToggleDarkMode: () -> Unit = {},
    onGymLevelSelected: (GymLevel) -> Unit,
    onCreatePlanClick: () -> Unit
) {
    val lang = userProfile.language
    val colors = AppTheme.colors
    var selectedGymLevel by remember { mutableStateOf(userProfile.gymLevel) }
    var activeInfoDialog by remember { mutableStateOf<String?>(null) }

    val infiniteTransition = rememberInfiniteTransition(label = "neon_glow")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "neon_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(20.dp)
            .testTag("analysis_gym_level_screen")
    ) {
        if (isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(colors.cardBackgroundOpaque)
                        .border(2.dp, colors.primaryAccent.copy(alpha = alphaAnim), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Analyzing",
                        tint = colors.primaryAccent,
                        modifier = Modifier.size(52.dp)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = LanguageManager.analyzingStatusText(lang),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.primaryAccent,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = colors.primaryAccent,
                    trackColor = colors.cardBorder
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Top Bar: Theme Toggle + Instagram Profile Quick Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ThemeToggleButton(
                            isDark = isDarkMode,
                            onToggle = onToggleDarkMode,
                            language = lang,
                            compact = true
                        )

                        InstagramFooter(compact = true)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = LanguageManager.analysisTitle(lang),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.primaryAccent
                    )

                    Text(
                        text = "خطوة 3 من 3 - نتائج تحليلك من الذكاء الاصطناعي واختيار جدول التمارين",
                        fontSize = 13.sp,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                    )

                    // Results Cards: BMI & TDEE (Clickable with Info Dialogs)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // BMI Card
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(24.dp))
                                .border(1.dp, colors.primaryAccent.copy(alpha = 0.35f), RoundedCornerShape(24.dp))
                                .clickable { activeInfoDialog = "BMI" }
                                .testTag("bmi_card"),
                            color = colors.cardBackgroundOpaque
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Speed,
                                            contentDescription = null,
                                            tint = colors.primaryAccent,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(
                                            text = "BMI",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = colors.textSecondary
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "معلومات عن BMI",
                                        tint = colors.primaryAccent.copy(alpha = 0.85f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "${analysisResult?.bmi ?: "--"}",
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Black,
                                    color = colors.textPrimary
                                )
                                Text(
                                    text = analysisResult?.bmiCategory ?: "وزن متناسق",
                                    fontSize = 12.sp,
                                    color = colors.primaryAccent,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }

                        // TDEE Card
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(24.dp))
                                .border(1.dp, colors.primaryAccent.copy(alpha = 0.35f), RoundedCornerShape(24.dp))
                                .clickable { activeInfoDialog = "TDEE" }
                                .testTag("tdee_card"),
                            color = colors.cardBackgroundOpaque
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocalFireDepartment,
                                            contentDescription = null,
                                            tint = colors.primaryAccent,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(
                                            text = "TDEE",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = colors.textSecondary
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "معلومات عن TDEE",
                                        tint = colors.primaryAccent.copy(alpha = 0.85f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "${analysisResult?.tdee ?: "--"}",
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Black,
                                    color = colors.textPrimary
                                )
                                Text(
                                    text = LanguageManager.caloriesUnit(lang),
                                    fontSize = 12.sp,
                                    color = colors.primaryAccent,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // AI Health Tip Card
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp))
                            .border(1.dp, colors.primaryAccent.copy(alpha = 0.3f), RoundedCornerShape(22.dp)),
                        color = colors.cardBackground
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(colors.primaryAccent.copy(alpha = if (isDarkMode) 0.15f else 0.08f), Color.Transparent)
                                    )
                                )
                                .padding(20.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(colors.primaryAccent)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = LanguageManager.healthTipHeader(lang),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colors.primaryAccent
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = analysisResult?.healthTip ?: "حافظ على شرب الماء والتغذية المتوازنة لتحقيق أفضل نتائج رياضيّة.",
                                    fontSize = 14.sp,
                                    color = colors.textSecondary,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Gym Level Selector
                    Text(
                        text = LanguageManager.gymLevelHeader(lang),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GymLevel.entries.forEach { level ->
                            val isSelected = selectedGymLevel == level
                            val borderColor = if (isSelected) colors.primaryAccent else colors.cardBorder
                            val bgColor = if (isSelected) colors.primaryAccent else colors.cardBackgroundOpaque
                            val textColor = if (isSelected) (if (isDarkMode) Color.Black else Color.White) else colors.textPrimary

                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(54.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .border(1.5.dp, borderColor, RoundedCornerShape(18.dp))
                                    .clickable {
                                        selectedGymLevel = level
                                        onGymLevelSelected(level)
                                    }
                                    .testTag("gym_level_item_${level.name}"),
                                color = bgColor
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = LanguageManager.gymLevelName(level, lang),
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                        color = textColor,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = {
                        onGymLevelSelected(selectedGymLevel)
                        onCreatePlanClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("create_full_plan_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primaryAccent,
                        contentColor = if (isDarkMode) Color.Black else Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = LanguageManager.btnCreateFullPlan(lang),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Permanent Instagram Profile Link
                InstagramFooter()
            }
        }

        // TDEE & BMI Metric Explanation Dialog
        activeInfoDialog?.let { metricType ->
            val isBmi = metricType == "BMI"
            val title = if (isBmi) LanguageManager.bmiDialogTitle(lang) else LanguageManager.tdeeDialogTitle(lang)
            val contentText = if (isBmi) LanguageManager.bmiDialogContent(lang) else LanguageManager.tdeeDialogContent(lang)
            val icon = if (isBmi) Icons.Default.Speed else Icons.Default.LocalFireDepartment

            AlertDialog(
                onDismissRequest = { activeInfoDialog = null },
                shape = RoundedCornerShape(26.dp),
                containerColor = colors.cardBackgroundOpaque,
                modifier = Modifier
                    .border(1.5.dp, colors.primaryAccent.copy(alpha = 0.5f), RoundedCornerShape(26.dp))
                    .testTag("metric_info_dialog"),
                icon = {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(colors.primaryAccent.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = colors.primaryAccent,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                title = {
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Text(
                        text = contentText,
                        fontSize = 15.sp,
                        color = colors.textSecondary,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { activeInfoDialog = null },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .testTag("close_info_dialog_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primaryAccent,
                            contentColor = if (isDarkMode) Color.Black else Color.White
                        )
                    ) {
                        Text(
                            text = LanguageManager.closeBtn(lang),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    }
}
