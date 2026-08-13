package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActivityLevel
import com.example.data.model.Goal
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
fun GoalLifestyleScreen(
    userProfile: UserProfile,
    onSaveGoalAndLifestyle: (Goal, ActivityLevel) -> Unit,
    onAnalyzeClick: () -> Unit
) {
    val lang = userProfile.language

    var selectedGoal by remember { mutableStateOf(userProfile.goal) }
    var selectedActivity by remember { mutableStateOf(userProfile.activityLevel) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(20.dp)
            .testTag("goal_lifestyle_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = LanguageManager.goalLifestyleTitle(lang),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrightNeonGreen
                )

                Text(
                    text = "خطوة 2 من 3 - حدد هدفك ونمط حياتك لتخصيص جدولك",
                    fontSize = 13.sp,
                    color = TextSecondaryGray,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                // Main Goal Selection
                Text(
                    text = LanguageManager.mainGoalHeader(lang),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimaryWhite,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Goal.entries.forEach { goal ->
                        val isSelected = selectedGoal == goal
                        val borderColor = if (isSelected) BrightNeonGreen else DarkEmeraldCardBorder
                        val bgColor = if (isSelected) DarkEmeraldCard else DarkEmeraldCard.copy(alpha = 0.4f)

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
                                .clickable { selectedGoal = goal }
                                .testTag("goal_item_${goal.name}"),
                            color = bgColor
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = LanguageManager.goalName(goal, lang),
                                    fontSize = 15.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = TextPrimaryWhite
                                )

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected Goal",
                                        tint = BrightNeonGreen,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Activity Level Selection
                Text(
                    text = LanguageManager.activityHeader(lang),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimaryWhite,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ActivityLevel.entries.forEach { act ->
                        val isSelected = selectedActivity == act
                        val borderColor = if (isSelected) BrightNeonGreen else DarkEmeraldCardBorder
                        val bgColor = if (isSelected) DarkEmeraldCard else DarkEmeraldCard.copy(alpha = 0.4f)

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
                                .clickable { selectedActivity = act }
                                .testTag("activity_item_${act.name}"),
                            color = bgColor
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = LanguageManager.activityName(act, lang),
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = TextPrimaryWhite,
                                    modifier = Modifier.weight(1f)
                                )

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected Activity",
                                        tint = BrightNeonGreen,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                }

            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    onSaveGoalAndLifestyle(selectedGoal, selectedActivity)
                    onAnalyzeClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("analyze_save_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonGreenAccent,
                    contentColor = DarkBackground
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = LanguageManager.btnAnalyzeAndSave(lang),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
