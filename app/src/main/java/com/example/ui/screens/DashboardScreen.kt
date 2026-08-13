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
import com.example.ui.theme.BrightNeonGreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkEmeraldCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.NeonGreenAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryWhite
import com.example.ui.theme.TextSecondaryGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    userProfile: UserProfile,
    analysisResult: AnalysisResult?,
    nutritionPlan: NutritionPlan?,
    workoutPlan: WorkoutPlan?,
    activeTab: Int,
    waterGlasses: Int,
    onTabSelected: (Int) -> Unit,
    onToggleExercise: (dayName: String, exerciseId: String) -> Unit,
    onAddWater: () -> Unit,
    onRemoveWater: () -> Unit,
    onLanguageChanged: (AppLanguage) -> Unit,
    onEditProfileClick: () -> Unit,
    onRefreshPlanClick: () -> Unit
) {
    val lang = userProfile.language
    var showLanguageMenu by remember { mutableStateOf(false) }

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
                                .background(DarkEmeraldCard)
                                .border(1.dp, BrightNeonGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = "Logo",
                                tint = BrightNeonGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "مِداد",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = BrightNeonGreen
                        )
                    }
                },
                actions = {
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
                            modifier = Modifier.background(DarkEmeraldCard)
                        ) {
                            AppLanguage.entries.forEach { appLang ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "${appLang.flag}  ${appLang.displayName}",
                                            color = TextPrimaryWhite
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
                            tint = TextSecondaryGray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextPrimaryWhite
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                tonalElevation = 8.dp,
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
                        selectedIconColor = DarkBackground,
                        selectedTextColor = BrightNeonGreen,
                        indicatorColor = BrightNeonGreen,
                        unselectedIconColor = TextSecondaryGray,
                        unselectedTextColor = TextSecondaryGray
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
                        selectedIconColor = DarkBackground,
                        selectedTextColor = BrightNeonGreen,
                        indicatorColor = BrightNeonGreen,
                        unselectedIconColor = TextSecondaryGray,
                        unselectedTextColor = TextSecondaryGray
                    ),
                    modifier = Modifier.testTag("tab_nutrition")
                )
            }
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Summary Banner with Frosted Glass
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(22.dp)),
                color = Color(0x990D2818)
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
                            color = TextPrimaryWhite
                        )
                        Text(
                            text = LanguageManager.goalName(userProfile.goal, lang),
                            fontSize = 12.sp,
                            color = BrightNeonGreen,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, BrightNeonGreen.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                            .clickable { onRefreshPlanClick() },
                        color = Color(0x3310B981)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh",
                                tint = BrightNeonGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = LanguageManager.refreshPlan(lang),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryWhite
                            )
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
                                val chipBg = if (isSelected) BrightNeonGreen else DarkEmeraldCard
                                val chipText = if (isSelected) DarkBackground else TextPrimaryWhite

                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .border(
                                            1.dp,
                                            if (isSelected) BrightNeonGreen else DarkEmeraldCardBorder,
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
                                    color = BrightNeonGreen,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }

                            if (currentDay.isRestDay) {
                                item {
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(20.dp))
                                            .border(1.dp, DarkEmeraldCardBorder, RoundedCornerShape(20.dp)),
                                        color = DarkEmeraldCard
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(24.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Bedtime,
                                                contentDescription = "Rest Day",
                                                tint = BrightNeonGreen,
                                                modifier = Modifier.size(56.dp)
                                            )
                                            Spacer(modifier = Modifier.height(12.dp))
                                            Text(
                                                text = LanguageManager.restDayTitle(lang),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimaryWhite
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = LanguageManager.restDayDescription(lang),
                                                fontSize = 13.sp,
                                                color = TextSecondaryGray,
                                                lineHeight = 19.sp
                                            )
                                        }
                                    }
                                }
                            } else {
                                items(currentDay.exercises) { exercise ->
                                    ExerciseCard(
                                        exercise = exercise,
                                        onToggle = {
                                            onToggleExercise(currentDay.dayName, exercise.id)
                                        }
                                    )
                                }
                            }

                            item {
                                YouTubeGymSection(lang = lang)
                            }

                            item { Spacer(modifier = Modifier.height(24.dp)) }
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
                                .border(1.dp, NeonGreenAccent.copy(alpha = 0.5f), RoundedCornerShape(18.dp)),
                            color = DarkEmeraldCard
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "🎯 الأهداف الغذائية اليومية",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrightNeonGreen
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    MacroBadge("بروتين", "${nutritionPlan?.targetProteinGrams ?: 150}g")
                                    MacroBadge("كاربوهيدرات", "${nutritionPlan?.targetCarbsGrams ?: 220}g")
                                    MacroBadge("دهون صحية", "${nutritionPlan?.targetFatGrams ?: 60}g")
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
                                .border(1.dp, DarkEmeraldCardBorder, RoundedCornerShape(18.dp)),
                            color = DarkEmeraldCard
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
                                        color = TextPrimaryWhite
                                    )
                                    Text(
                                        text = "$waterGlasses / 10 أكواب اليوم",
                                        fontSize = 13.sp,
                                        color = BrightNeonGreen,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = onRemoveWater,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(DarkEmeraldCardBorder)
                                            .testTag("btn_water_minus")
                                    ) {
                                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Minus", tint = TextPrimaryWhite)
                                    }

                                    Text(
                                        text = "$waterGlasses",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryWhite,
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    )

                                    IconButton(
                                        onClick = onAddWater,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(BrightNeonGreen)
                                            .testTag("btn_water_plus")
                                    ) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = DarkBackground)
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
                            color = TextPrimaryWhite,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }

                    // Meals list
                    val meals = nutritionPlan?.meals ?: emptyList()
                    items(meals) { meal ->
                        MealCard(meal = meal)
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        }
    }
}

@Composable
fun ExerciseCard(exercise: Exercise, onToggle: () -> Unit) {
    val isDone = exercise.isCompleted
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                1.dp,
                if (isDone) BrightNeonGreen.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.08f),
                RoundedCornerShape(20.dp)
            )
            .clickable { onToggle() }
            .testTag("exercise_card_${exercise.id}"),
        color = if (isDone) Color(0xCC0D2818) else Color(0x800D2818)
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
                        checkedColor = BrightNeonGreen,
                        checkmarkColor = DarkBackground,
                        uncheckedColor = TextSecondaryGray
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDone) TextSecondaryGray else TextPrimaryWhite,
                        textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${exercise.muscleTarget} • ${exercise.sets} مجموعات × ${exercise.reps} تكرار",
                        fontSize = 11.sp,
                        color = if (isDone) TextMuted else BrightNeonGreen
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        val searchUri = Uri.parse("https://www.youtube.com/results?search_query=" + Uri.encode("${exercise.name} gym tutorial"))
                        val intent = Intent(Intent.ACTION_VIEW, searchUri)
                        context.startActivity(intent)
                    },
                color = Color(0x40FF0000)
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
                        color = Color(0xFFFF6666)
                    )
                }
            }
        }
    }
}

data class YouTubeGymExercise(
    val title: String,
    val subtitle: String,
    val searchQuery: String
)

@Composable
fun YouTubeGymSection(lang: AppLanguage) {
    val context = LocalContext.current

    val youtubeExercises = listOf(
        YouTubeGymExercise("🏋️ Bench Press (بنش بريس)", "شرح الأداء الصحيح للصدر الأوسط والأسفل", "bench press gym exercise tutorial form"),
        YouTubeGymExercise("🦵 Barbell Squats (السكوات)", "طريقة أداء السكوات بالبار وحماية الركبتين", "barbell squat tutorial form gym"),
        YouTubeGymExercise("🏋️‍♂️ Deadlift (الرفعة المميتة)", "شرح طريقة الديدلفت للظهر والظهر السفلي", "deadlift tutorial form gym"),
        YouTubeGymExercise("💪 Lat Pulldown (سحب الظهر)", "تمرين استهداف الظهر العريض بأمان", "lat pulldown tutorial gym form"),
        YouTubeGymExercise("🎯 Overhead Shoulder Press", "شرح تمرين ضغط الأكتاف بالبار أو الدامبلز", "dumbbell shoulder press tutorial gym"),
        YouTubeGymExercise("⚡ Biceps & Triceps Workout", "تمارين تضخيم عضلات البايسبس والترايسبس", "biceps triceps workout gym tutorial")
    )

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
                color = TextPrimaryWhite
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
                        .border(1.dp, Color(0xFFFF3333).copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                        .clickable {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.youtube.com/results?search_query=" + Uri.encode(item.searchQuery))
                            )
                            context.startActivity(intent)
                        },
                    color = Color(0x661A0000)
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
                                color = TextPrimaryWhite,
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
                            color = TextSecondaryGray,
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
                                tint = Color(0xFFFF6666),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = LanguageManager.youtubeWatchBtn(lang),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF6666)
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
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(22.dp)),
        color = Color(0x800D2818)
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
                    color = BrightNeonGreen
                )

                Text(
                    text = "${meal.totalCalories} kcal",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimaryWhite
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
                        color = TextPrimaryWhite,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = ing.gramsOrQty,
                        fontSize = 12.sp,
                        color = TextSecondaryGray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


@Composable
fun MacroBadge(title: String, amount: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, fontSize = 11.sp, color = TextSecondaryGray)
        Text(text = amount, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimaryWhite)
    }
}
