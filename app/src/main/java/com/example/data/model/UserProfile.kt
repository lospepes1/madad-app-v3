package com.example.data.model

enum class AppLanguage(val code: String, val displayName: String, val flag: String) {
    AR("ar", "العربية", "🇹🇳"),
    FR("fr", "Français", "🇫🇷"),
    EN("en", "English", "🇬🇧")
}

enum class Gender { MALE, FEMALE }

enum class Goal {
    CUTTING,   // تنشيف وخسارة الدهون
    BULKING,   // بناء العضلات وزيادة الوزن
    MAINTAIN   // المحافظة على الوزن واللياقة
}

enum class ActivityLevel {
    SEDENTARY, // خامل
    MODERATE,  // متوسط النشاط
    VERY_ACTIVE// نشيط جداً
}

enum class GymLevel {
    BEGINNER,    // مبتدئ (3 أيام - Full Body)
    INTERMEDIATE,// متوسط (4 أيام - Upper / Lower)
    ADVANCED     // متقدم (5-6 أيام - Push / Pull / Legs)
}

data class UserProfile(
    val language: AppLanguage = AppLanguage.AR,
    val gender: Gender = Gender.MALE,
    val age: Int = 0,
    val heightCm: Float = 0f,
    val weightKg: Float = 0f,
    val goal: Goal = Goal.CUTTING,
    val activityLevel: ActivityLevel = ActivityLevel.MODERATE,
    val gymLevel: GymLevel = GymLevel.BEGINNER,
    val isCompleted: Boolean = false
)

data class AnalysisResult(
    val bmi: Float = 0f,
    val bmiCategory: String = "",
    val tdee: Int = 0,
    val healthTip: String = ""
)

data class MealIngredient(
    val name: String,
    val gramsOrQty: String,
    val calories: Int
)

data class Meal(
    val type: String, // Breakfast, Lunch, Snack, Dinner
    val title: String,
    val ingredients: List<MealIngredient>,
    val totalCalories: Int,
    val proteinGrams: Int = 0,
    val carbsGrams: Int = 0,
    val fatGrams: Int = 0
)

data class NutritionPlan(
    val meals: List<Meal>,
    val totalDailyCalories: Int,
    val targetProteinGrams: Int,
    val targetCarbsGrams: Int,
    val targetFatGrams: Int
)

data class Exercise(
    val id: String,
    val name: String,
    val muscleTarget: String,
    val sets: Int,
    val reps: String,
    val isCompleted: Boolean = false,
    val videoId: String = ""
)

data class DayWorkout(
    val dayName: String,
    val title: String,
    val isRestDay: Boolean,
    val exercises: List<Exercise>
)

data class WorkoutPlan(
    val gymLevel: GymLevel,
    val days: List<DayWorkout>
)
