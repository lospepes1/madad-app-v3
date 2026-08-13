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
import androidx.compose.material.icons.filled.FitnessCenter
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
import com.example.ui.theme.BrightNeonGreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkEmeraldCardBorder
import com.example.ui.theme.NeonGreenAccent
import com.example.ui.theme.TextPrimaryWhite
import com.example.ui.theme.TextSecondaryGray

@Composable
fun AnalysisGymLevelScreen(
    userProfile: UserProfile,
    analysisResult: AnalysisResult?,
    isLoading: Boolean,
    onGymLevelSelected: (GymLevel) -> Unit,
    onCreatePlanClick: () -> Unit
) {
    val lang = userProfile.language
    var selectedGymLevel by remember { mutableStateOf(userProfile.gymLevel) }

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
            .background(DarkBackground)
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
                        .background(DarkEmeraldCard)
                        .border(2.dp, BrightNeonGreen.copy(alpha = alphaAnim), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Analyzing",
                        tint = BrightNeonGreen,
                        modifier = Modifier.size(52.dp)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = LanguageManager.analyzingStatusText(lang),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrightNeonGreen,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = BrightNeonGreen,
                    trackColor = DarkEmeraldCardBorder
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
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = LanguageManager.analysisTitle(lang),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrightNeonGreen
                    )

                    Text(
                        text = "خطوة 3 من 3 - نتائج تحليلك من الذكاء الاصطناعي واختيار جدول التمارين",
                        fontSize = 13.sp,
                        color = TextSecondaryGray,
                        modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                    )

                    // Results Cards: BMI & TDEE
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // BMI Card
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(24.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(24.dp)),
                            color = Color(0x990D2818)
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Speed,
                                        contentDescription = null,
                                        tint = BrightNeonGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "BMI",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextSecondaryGray
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "${analysisResult?.bmi ?: "--"}",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimaryWhite
                                )
                                Text(
                                    text = analysisResult?.bmiCategory ?: "وزن متناسق",
                                    fontSize = 12.sp,
                                    color = BrightNeonGreen,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }

                        // TDEE Card
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(24.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(24.dp)),
                            color = Color(0x990D2818)
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocalFireDepartment,
                                        contentDescription = null,
                                        tint = BrightNeonGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "TDEE",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextSecondaryGray
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "${analysisResult?.tdee ?: "--"}",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimaryWhite
                                )
                                Text(
                                    text = LanguageManager.caloriesUnit(lang),
                                    fontSize = 12.sp,
                                    color = BrightNeonGreen,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // AI Health Tip Card with Frosted Glass Gradient
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp))
                            .border(1.dp, NeonGreenAccent.copy(alpha = 0.3f), RoundedCornerShape(22.dp)),
                        color = Color(0x660D2818)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(NeonGreenAccent.copy(alpha = 0.15f), Color.Transparent)
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
                                            .background(BrightNeonGreen)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = LanguageManager.healthTipHeader(lang),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BrightNeonGreen
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = analysisResult?.healthTip ?: "حافظ على شرب الماء والتغذية المتوازنة لتحقيق أفضل نتائج رياضيّة.",
                                    fontSize = 14.sp,
                                    color = TextSecondaryGray,
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
                        color = TextPrimaryWhite,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GymLevel.entries.forEach { level ->
                            val isSelected = selectedGymLevel == level
                            val borderColor = if (isSelected) BrightNeonGreen else Color.White.copy(alpha = 0.1f)
                            val bgColor = if (isSelected) NeonGreenAccent else Color(0x990D2818)
                            val textColor = if (isSelected) DarkBackground else TextPrimaryWhite

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
                        containerColor = NeonGreenAccent,
                        contentColor = DarkBackground
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = LanguageManager.btnCreateFullPlan(lang),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
