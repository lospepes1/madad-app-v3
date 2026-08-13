package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.AnalysisGymLevelScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GoalLifestyleScreen
import com.example.ui.screens.PersonalDataScreen
import com.example.ui.screens.WelcomeLanguageScreen
import com.example.ui.theme.MidadTheme
import com.example.ui.viewmodel.MidadViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MidadTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MidadMainApp()
                }
            }
        }
    }
}

@Composable
fun MidadMainApp(viewModel: MidadViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val analysisResult by viewModel.analysisResult.collectAsStateWithLifecycle()
    val nutritionPlan by viewModel.nutritionPlan.collectAsStateWithLifecycle()
    val workoutPlan by viewModel.workoutPlan.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val waterGlasses by viewModel.waterGlasses.collectAsStateWithLifecycle()
    val dashboardTab by viewModel.dashboardTab.collectAsStateWithLifecycle()

    when (currentScreen) {
        Screen.WELCOME_LANGUAGE -> {
            WelcomeLanguageScreen(
                selectedLanguage = userProfile.language,
                onLanguageSelected = { viewModel.setLanguage(it) },
                onStartClick = { viewModel.navigateTo(Screen.PERSONAL_DATA) }
            )
        }

        Screen.PERSONAL_DATA -> {
            PersonalDataScreen(
                userProfile = userProfile,
                onSavePersonalData = { gender, age, height, weight ->
                    viewModel.updatePersonalData(gender, age, height, weight)
                },
                onNextClick = { viewModel.navigateTo(Screen.GOAL_LIFESTYLE) }
            )
        }

        Screen.GOAL_LIFESTYLE -> {
            GoalLifestyleScreen(
                userProfile = userProfile,
                onSaveGoalAndLifestyle = { goal, activity ->
                    viewModel.updateGoalAndLifestyle(goal, activity)
                },
                onAnalyzeClick = {
                    viewModel.runBodyAnalysis()
                    viewModel.navigateTo(Screen.ANALYSIS_GYM_LEVEL)
                }
            )
        }

        Screen.ANALYSIS_GYM_LEVEL -> {
            AnalysisGymLevelScreen(
                userProfile = userProfile,
                analysisResult = analysisResult,
                isLoading = isLoading,
                onGymLevelSelected = { viewModel.updateGymLevel(it) },
                onCreatePlanClick = { viewModel.generateFullPlanAndGoDashboard() }
            )
        }

        Screen.DASHBOARD -> {
            DashboardScreen(
                userProfile = userProfile,
                analysisResult = analysisResult,
                nutritionPlan = nutritionPlan,
                workoutPlan = workoutPlan,
                activeTab = dashboardTab,
                waterGlasses = waterGlasses,
                onTabSelected = { viewModel.setDashboardTab(it) },
                onToggleExercise = { day, exId -> viewModel.toggleExerciseCompletion(day, exId) },
                onAddWater = { viewModel.incrementWater() },
                onRemoveWater = { viewModel.decrementWater() },
                onLanguageChanged = { viewModel.setLanguage(it) },
                onEditProfileClick = { viewModel.resetProfileToEdit() },
                onRefreshPlanClick = { viewModel.generateFullPlanAndGoDashboard() }
            )
        }
    }
}

