package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AnalysisResult
import com.example.data.model.AppLanguage
import com.example.data.model.Exercise
import com.example.data.model.Meal
import com.example.data.model.NutritionPlan
import com.example.data.model.UserProfile
import com.example.data.model.WorkoutPlan
import com.example.localization.LanguageManager
import com.example.ui.components.ExerciseTutorialBottomSheet
import com.example.ui.components.ExerciseTutorialDetail
import com.example.ui.components.ExerciseTutorialHelper
import com.example.ui.components.InstagramIconButton
import com.example.ui.components.ThemeToggleButton
import com.example.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    userProfile: UserProfile,
    analysisResult: AnalysisResult?,
    nutritionPlan: NutritionPlan?,
    workoutPlan: WorkoutPlan?,
    activeTab: Int,
    waterGlasses: Int,
    isDarkMode: Boolean = true,
    onToggleDarkMode: () -> Unit = {},
    onTabSelected: (Int) -> Unit,
    onToggleExercise: (dayName: String, exerciseId: String) -> Unit,
    onAddWater: () -> Unit,
    onRemoveWater: () -> Unit,
    onLanguageChanged: (AppLanguage) -> Unit,
    onEditProfileClick: () -> Unit,
    onRefreshPlanClick: () -> Unit
) {
    val lang = userProfile.language
    val colors = AppTheme.colors
    val context = LocalContext.current
    var showLanguageMenu by remember { mutableStateOf(false) }
    var activeTutorial by remember { mutableStateOf<ExerciseTutorialDetail?>(null) }

    val days = workoutPlan?.days ?: emptyList()
    var selectedDayIndex by remember { mutableIntStateOf(0) }
    val currentDay = days.getOrNull(selectedDayIndex.coerceIn(0, (days.size - 1).coerceAtLeast(0)))

    Scaffold(
        modifier = Modifier.testTag("dashboard_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(colors.cardBackgroundOpaque)
                                .border(1.dp, colors.primaryAccent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = "Logo",
                                tint = colors.primaryAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "مِداد",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = colors.primaryAccent
                        )
                    }
                },
                actions = {
                    // Theme Switcher Button
                    ThemeToggleButton(
                        isDark = isDarkMode,
                        onToggle = onToggleDarkMode,
                        language = lang,
                        compact = true
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    // Instagram profile quick link badge (top only)
                    InstagramIconButton(
                        modifier = Modifier.padding(end = 4.dp)
                    )

                    // Language switcher button
                    Box {
                        IconButton(
                            onClick = { showLanguageMenu = true },
                            modifier = Modifier.testTag("btn_language_switch")
                        ) {
                            Text(text = lang.flag, fontSize = 20.sp)
                        }

                        DropdownMenu(
                            expanded = showLanguageMenu,
                            onDismissRequest = { showLanguageMenu = false },
                            modifier = Modifier.background(colors.cardBackgroundOpaque)
                        ) {
                            AppLanguage.entries.forEach { appLang ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "${appLang.flag}  ${appLang.displayName}",
                                            color = colors.textPrimary
                                        )
                                    },
                                    onClick = {
                                        onLanguageChanged(appLang)
                                        showLanguageMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Edit Profile button
                    IconButton(
                        onClick = onEditProfileClick,
                        modifier = Modifier.testTag("btn_edit_profile")
                    ) {
                        Icon(
                            imageVector = Icons.Default.EditNote,
                            contentDescription = LanguageManager.editProfile(lang),
                            tint = colors.textSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.background,
                    titleContentColor = colors.textPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = colors.surface,
                tonalElevation = 4.dp,
                windowInsets = WindowInsets.navigationBars
            ) {
                NavigationBarItem(
                        selected = activeTab == 0,
                        onClick = { onTabSelected(0) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = "Exercises"
                            )
                        },
                        label = {
                            Text(
                                text = LanguageManager.tabExercises(lang),
                                fontSize = 13.sp,
                                fontWeight = if (activeTab == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = if (isDarkMode) Color.Black else Color.White,
                            selectedTextColor = colors.primaryAccent,
                            indicatorColor = colors.primaryAccent,
                            unselectedIconColor = colors.textSecondary,
                            unselectedTextColor = colors.textSecondary
                        ),
                        modifier = Modifier.testTag("tab_exercises")
                    )

                    NavigationBarItem(
                        selected = activeTab == 1,
                        onClick = { onTabSelected(1) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.RestaurantMenu,
                                contentDescription = "Nutrition"
                            )
                        },
                        label = {
                            Text(
                                text = LanguageManager.tabNutrition(lang),
                                fontSize = 13.sp,
                                fontWeight = if (activeTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = if (isDarkMode) Color.Black else Color.White,
                            selectedTextColor = colors.primaryAccent,
                            indicatorColor = colors.primaryAccent,
                            unselectedIconColor = colors.textSecondary,
                            unselectedTextColor = colors.textSecondary
                        ),
                        modifier = Modifier.testTag("tab_nutrition")
                    )

                    NavigationBarItem(
                        selected = activeTab == 2,
                        onClick = { onTabSelected(2) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Alarm,
                                contentDescription = "Gym Alarm"
                            )
                        },
                        label = {
                            Text(
                                text = LanguageManager.tabGymAlarm(lang),
                                fontSize = 13.sp,
                                fontWeight = if (activeTab == 2) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = if (isDarkMode) Color.Black else Color.White,
                            selectedTextColor = colors.primaryAccent,
                            indicatorColor = colors.primaryAccent,
                            unselectedIconColor = colors.textSecondary,
                            unselectedTextColor = colors.textSecondary
                        ),
                        modifier = Modifier.testTag("tab_gym_alarm")
                    )
                }
        },
        containerColor = colors.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Summary Banner (Shown on Exercises & Nutrition Plan tabs)
            if (activeTab != 2) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .border(1.dp, colors.cardBorder, RoundedCornerShape(22.dp)),
                    color = colors.cardBackgroundOpaque
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TDEE: ${analysisResult?.tdee ?: "--"} kcal | BMI: ${analysisResult?.bmi ?: "--"}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                            Text(
                                text = LanguageManager.goalName(userProfile.goal, lang),
                                fontSize = 12.sp,
                                color = colors.primaryAccent,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .border(1.dp, colors.primaryAccent.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                                .clickable { onRefreshPlanClick() },
                            color = colors.primaryAccent.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh",
                                    tint = colors.primaryAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = LanguageManager.refreshPlan(lang),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textPrimary
                                )
                            }
                        }
                    }
                }
            }


            // Tab 0: Exercises
            if (activeTab == 0) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Days Carousel
                    if (days.isNotEmpty()) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(days.size) { index ->
                                val day = days[index]
                                val isSelected = selectedDayIndex == index
                                val chipBg = if (isSelected) colors.primaryAccent else colors.cardBackgroundOpaque
                                val chipText = if (isSelected) (if (isDarkMode) Color.Black else Color.White) else colors.textPrimary

                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .border(
                                            1.dp,
                                            if (isSelected) colors.primaryAccent else colors.cardBorder,
                                            RoundedCornerShape(20.dp)
                                        )
                                        .clickable { selectedDayIndex = index }
                                        .testTag("day_chip_$index"),
                                    color = chipBg
                                ) {
                                    Text(
                                        text = day.dayName,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = chipText,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Current Day Workout Details
                    if (currentDay != null) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Text(
                                    text = currentDay.title,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.primaryAccent,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }

                            if (currentDay.isRestDay) {
                                item {
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(20.dp))
                                            .border(1.dp, colors.cardBorder, RoundedCornerShape(20.dp)),
                                        color = colors.cardBackgroundOpaque
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(24.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Bedtime,
                                                contentDescription = "Rest Day",
                                                tint = colors.primaryAccent,
                                                modifier = Modifier.size(56.dp)
                                            )
                                            Spacer(modifier = Modifier.height(12.dp))
                                            Text(
                                                text = LanguageManager.restDayTitle(lang),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = colors.textPrimary
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = LanguageManager.restDayDescription(lang),
                                                fontSize = 13.sp,
                                                color = colors.textSecondary,
                                                lineHeight = 19.sp
                                            )
                                        }
                                    }
                                }
                            } else {
                                items(currentDay.exercises) { exercise ->
                                    ExerciseCard(
                                        exercise = exercise,
                                        lang = lang,
                                        onToggle = {
                                            onToggleExercise(currentDay.dayName, exercise.id)
                                        },
                                        onOpenTutorial = {
                                            activeTutorial = ExerciseTutorialHelper.getTutorial(
                                                exerciseName = exercise.name,
                                                muscleTarget = exercise.muscleTarget,
                                                setsAndReps = "${exercise.sets} ${LanguageManager.setsLabel(lang)} × ${exercise.reps} ${LanguageManager.repsLabel(lang)}",
                                                explicitVideoId = exercise.videoId,
                                                lang = lang
                                            )
                                        }
                                    )
                                }
                            }

                            item {
                                YouTubeGymSection(
                                    lang = lang,
                                    onSelectExercise = { tutorial ->
                                        activeTutorial = tutorial
                                    }
                                )
                            }

                            item { Spacer(modifier = Modifier.height(16.dp)) }
                        }
                    }
                }
            } else {
                // Tab 1: Nutrition
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        // Daily Macros Card
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .border(1.dp, colors.primaryAccent.copy(alpha = 0.4f), RoundedCornerShape(18.dp)),
                            color = colors.cardBackgroundOpaque
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = LanguageManager.dailyNutritionGoals(lang),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.primaryAccent
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    MacroBadge(LanguageManager.macroProtein(lang), "${nutritionPlan?.targetProteinGrams ?: 150}g")
                                    MacroBadge(LanguageManager.macroCarbs(lang), "${nutritionPlan?.targetCarbsGrams ?: 220}g")
                                    MacroBadge(LanguageManager.macroFats(lang), "${nutritionPlan?.targetFatGrams ?: 60}g")
                                }
                            }
                        }
                    }

                    // Water Tracker Section
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .border(1.dp, colors.cardBorder, RoundedCornerShape(18.dp)),
                            color = colors.cardBackgroundOpaque
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = LanguageManager.waterTrackerTitle(lang),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colors.textPrimary
                                    )
                                    Text(
                                        text = LanguageManager.waterGlassesStatus(waterGlasses, lang),
                                        fontSize = 13.sp,
                                        color = colors.primaryAccent,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = onRemoveWater,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(colors.cardBorder)
                                            .testTag("btn_water_minus")
                                    ) {
                                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Minus", tint = colors.textPrimary)
                                    }

                                    Text(
                                        text = "$waterGlasses",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colors.textPrimary,
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    )

                                    IconButton(
                                        onClick = onAddWater,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(colors.primaryAccent)
                                            .testTag("btn_water_plus")
                                    ) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = if (isDarkMode) Color.Black else Color.White)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = LanguageManager.tunisianIngredientsHeader(lang),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }

                    // Meals list
                    val meals = nutritionPlan?.meals ?: emptyList()
                    items(meals) { meal ->
                        MealCard(meal = meal)
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }

            // Tab 2: Gym Alarm & Workout Reminder
            if (activeTab == 2) {
                GymAlarmScreen(
                    language = lang,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    // In-App YouTube Player & Form Guide BottomSheet
    activeTutorial?.let { tutorial ->
        ExerciseTutorialBottomSheet(
            tutorial = tutorial,
            lang = lang,
            onDismiss = { activeTutorial = null }
        )
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    lang: AppLanguage,
    onToggle: () -> Unit,
    onOpenTutorial: () -> Unit
) {
    val colors = AppTheme.colors
    val isDone = exercise.isCompleted

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                1.dp,
                if (isDone) colors.primaryAccent.copy(alpha = 0.6f) else colors.cardBorder,
                RoundedCornerShape(20.dp)
            )
            .clickable { onToggle() }
            .testTag("exercise_card_${exercise.id}"),
        color = if (isDone) colors.cardBackgroundOpaque else colors.cardBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isDone,
                    onCheckedChange = { onToggle() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = colors.primaryAccent,
                        checkmarkColor = colors.background,
                        uncheckedColor = colors.textSecondary
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDone) colors.textSecondary else colors.textPrimary,
                        textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${exercise.muscleTarget} • ${exercise.sets} ${LanguageManager.setsLabel(lang)} × ${exercise.reps} ${LanguageManager.repsLabel(lang)}",
                        fontSize = 11.sp,
                        color = if (isDone) colors.textSecondary else colors.primaryAccent
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onOpenTutorial() }
                    .testTag("exercise_tutorial_btn_${exercise.id}"),
                color = Color(0x25FF0000)
            ) {
                Row(
                    modifier = Modifier
                        .border(1.dp, Color(0xFFFF3333).copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "YouTube Tutorial",
                        tint = Color(0xFFFF3333),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "YouTube",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF4444)
                    )
                }
            }
        }
    }
}

@Composable
fun YouTubeGymSection(
    lang: AppLanguage,
    onSelectExercise: (ExerciseTutorialDetail) -> Unit
) {
    val colors = AppTheme.colors
    val youtubeExercises = LanguageManager.youtubeExercisesList(lang)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayCircle,
                contentDescription = "YouTube Gym Exercises",
                tint = Color(0xFFFF3333),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = LanguageManager.youtubeSuggestionsTitle(lang),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(youtubeExercises) { item ->
                Surface(
                    modifier = Modifier
                        .width(220.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, Color(0xFFFF3333).copy(alpha = 0.35f), RoundedCornerShape(18.dp))
                        .clickable {
                            onSelectExercise(
                                ExerciseTutorialHelper.getTutorial(
                                    exerciseName = item.title,
                                    muscleTarget = item.subtitle,
                                    setsAndReps = "",
                                    explicitVideoId = item.videoId,
                                    lang = lang
                                )
                            )
                        },
                    color = colors.cardBackgroundOpaque
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color(0xFFFF3333),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = item.subtitle,
                            fontSize = 11.sp,
                            color = colors.textSecondary,
                            maxLines = 2,
                            lineHeight = 15.sp,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.SmartDisplay,
                                contentDescription = null,
                                tint = Color(0xFFFF4444),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = LanguageManager.youtubeWatchBtn(lang),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF4444)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MealCard(meal: Meal) {
    val colors = AppTheme.colors
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, colors.cardBorder, RoundedCornerShape(22.dp)),
        color = colors.cardBackgroundOpaque
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = meal.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.primaryAccent
                )

                Text(
                    text = "${meal.totalCalories} kcal",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            meal.ingredients.forEach { ing ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "• ${ing.name}",
                        fontSize = 13.sp,
                        color = colors.textPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = ing.gramsOrQty,
                        fontSize = 12.sp,
                        color = colors.textSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


@Composable
fun MacroBadge(title: String, amount: String) {
    val colors = AppTheme.colors
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, fontSize = 11.sp, color = colors.textSecondary)
        Text(text = amount, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
    }
}
