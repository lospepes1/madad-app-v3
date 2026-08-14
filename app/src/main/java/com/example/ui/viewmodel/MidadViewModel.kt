package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.UserPreferencesManager
import com.example.data.model.ActivityLevel
import com.example.data.model.AnalysisResult
import com.example.data.model.AppLanguage
import com.example.data.model.Gender
import com.example.data.model.GymLevel
import com.example.data.model.Goal
import com.example.data.model.NutritionPlan
import com.example.data.model.UserProfile
import com.example.data.model.WorkoutPlan
import com.example.data.model.GeminiApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class Screen {
    WELCOME_LANGUAGE,
    PERSONAL_DATA,
    GOAL_LIFESTYLE,
    ANALYSIS_GYM_LEVEL,
    DASHBOARD
}

class MidadViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsManager = UserPreferencesManager(application)
    private val apiClient = GeminiApiClient()

    private val _currentScreen = MutableStateFlow(Screen.WELCOME_LANGUAGE)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _analysisResult = MutableStateFlow<AnalysisResult?>(null)
    val analysisResult: StateFlow<AnalysisResult?> = _analysisResult.asStateFlow()

    private val _nutritionPlan = MutableStateFlow<NutritionPlan?>(null)
    val nutritionPlan: StateFlow<NutritionPlan?> = _nutritionPlan.asStateFlow()

    private val _workoutPlan = MutableStateFlow<WorkoutPlan?>(null)
    val workoutPlan: StateFlow<WorkoutPlan?> = _workoutPlan.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _waterGlasses = MutableStateFlow(0)
    val waterGlasses: StateFlow<Int> = _waterGlasses.asStateFlow()

    private val _dashboardTab = MutableStateFlow(0) // 0: Exercises, 1: Nutrition
    val dashboardTab: StateFlow<Int> = _dashboardTab.asStateFlow()

    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    init {
        loadSavedData()
    }

    private fun loadSavedData() {
        val savedProfile = prefsManager.getUserProfile()
        val savedLang = prefsManager.getLanguage()
        val savedAnalysis = prefsManager.getAnalysisResult()
        val savedNutrition = prefsManager.getNutritionPlan()
        val savedWorkout = prefsManager.getWorkoutPlan()
        _waterGlasses.value = prefsManager.getWaterGlasses()
        _isDarkMode.value = prefsManager.isDarkMode()

        if (savedProfile != null) {
            _userProfile.value = savedProfile.copy(language = savedLang)
            _analysisResult.value = savedAnalysis
            _nutritionPlan.value = savedNutrition
            _workoutPlan.value = savedWorkout

            if (savedProfile.isCompleted && savedWorkout != null && savedNutrition != null) {
                _currentScreen.value = Screen.DASHBOARD
            } else {
                _currentScreen.value = Screen.PERSONAL_DATA
            }
        } else {
            _userProfile.value = UserProfile(language = savedLang)
            _currentScreen.value = Screen.WELCOME_LANGUAGE
        }
    }

    fun setLanguage(language: AppLanguage) {
        val updated = _userProfile.value.copy(language = language)
        _userProfile.value = updated
        prefsManager.saveLanguage(language)
        prefsManager.saveUserProfile(updated)
    }

    fun updatePersonalData(gender: Gender, age: Int, heightCm: Float, weightKg: Float) {
        val updated = _userProfile.value.copy(
            gender = gender,
            age = age,
            heightCm = heightCm,
            weightKg = weightKg
        )
        _userProfile.value = updated
        prefsManager.saveUserProfile(updated)
    }

    fun updateGoalAndLifestyle(goal: Goal, activityLevel: ActivityLevel) {
        val updated = _userProfile.value.copy(
            goal = goal,
            activityLevel = activityLevel
        )
        _userProfile.value = updated
        prefsManager.saveUserProfile(updated)
    }

    fun updateGymLevel(gymLevel: GymLevel) {
        val updated = _userProfile.value.copy(gymLevel = gymLevel)
        _userProfile.value = updated
        prefsManager.saveUserProfile(updated)
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun runBodyAnalysis() {
        viewModelScope.launch {
            _isLoading.value = true
            val profile = _userProfile.value
            try {
                val (analysis, nutrition) = apiClient.analyzeBodyAndNutrition(profile)
                _analysisResult.value = analysis
                _nutritionPlan.value = nutrition

                prefsManager.saveAnalysisResult(analysis)
                prefsManager.saveNutritionPlan(nutrition)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun generateFullPlanAndGoDashboard() {
        viewModelScope.launch {
            _isLoading.value = true
            val profile = _userProfile.value.copy(isCompleted = true)
            _userProfile.value = profile
            prefsManager.saveUserProfile(profile)

            try {
                if (_analysisResult.value == null || _nutritionPlan.value == null) {
                    val (analysis, nutrition) = apiClient.analyzeBodyAndNutrition(profile)
                    _analysisResult.value = analysis
                    _nutritionPlan.value = nutrition
                    prefsManager.saveAnalysisResult(analysis)
                    prefsManager.saveNutritionPlan(nutrition)
                }

                val workout = apiClient.generateWorkoutPlan(profile)
                _workoutPlan.value = workout
                prefsManager.saveWorkoutPlan(workout)

                _currentScreen.value = Screen.DASHBOARD
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setDashboardTab(tabIndex: Int) {
        _dashboardTab.value = tabIndex
    }

    fun toggleExerciseCompletion(dayName: String, exerciseId: String) {
        val currentWorkout = _workoutPlan.value ?: return
        val updatedDays = currentWorkout.days.map { day ->
            if (day.dayName == dayName) {
                val updatedExercises = day.exercises.map { ex ->
                    if (ex.id == exerciseId) {
                        ex.copy(isCompleted = !ex.isCompleted)
                    } else ex
                }
                day.copy(exercises = updatedExercises)
            } else day
        }
        val updatedWorkout = currentWorkout.copy(days = updatedDays)
        _workoutPlan.value = updatedWorkout
        prefsManager.saveWorkoutPlan(updatedWorkout)
    }

    fun incrementWater() {
        val current = _waterGlasses.value + 1
        _waterGlasses.value = current
        prefsManager.saveWaterGlasses(current)
    }

    fun decrementWater() {
        if (_waterGlasses.value > 0) {
            val current = _waterGlasses.value - 1
            _waterGlasses.value = current
            prefsManager.saveWaterGlasses(current)
        }
    }

    fun resetProfileToEdit() {
        _currentScreen.value = Screen.PERSONAL_DATA
    }

    fun toggleDarkMode() {
        val newMode = !_isDarkMode.value
        _isDarkMode.value = newMode
        prefsManager.saveDarkMode(newMode)
    }

    fun setDarkMode(isDark: Boolean) {
        _isDarkMode.value = isDark
        prefsManager.saveDarkMode(isDark)
    }
}
