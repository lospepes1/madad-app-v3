package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.AnalysisResult
import com.example.data.model.AppLanguage
import com.example.data.model.NutritionPlan
import com.example.data.model.UserProfile
import com.example.data.model.WorkoutPlan
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class UserPreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("midad_prefs", Context.MODE_PRIVATE)
    private val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    companion object {
        private const val KEY_LANGUAGE = "key_language"
        private const val KEY_USER_PROFILE = "key_user_profile"
        private const val KEY_ANALYSIS_RESULT = "key_analysis_result"
        private const val KEY_NUTRITION_PLAN = "key_nutrition_plan"
        private const val KEY_WORKOUT_PLAN = "key_workout_plan"
        private const val KEY_WATER_GLASSES = "key_water_glasses"
        private const val KEY_DARK_MODE = "key_dark_mode"
    }

    fun isDarkMode(): Boolean = prefs.getBoolean(KEY_DARK_MODE, true)

    fun saveDarkMode(isDark: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_MODE, isDark).apply()
    }

    fun saveLanguage(language: AppLanguage) {
        prefs.edit().putString(KEY_LANGUAGE, language.code).apply()
    }

    fun getLanguage(): AppLanguage {
        val code = prefs.getString(KEY_LANGUAGE, AppLanguage.AR.code) ?: AppLanguage.AR.code
        return AppLanguage.entries.find { it.code == code } ?: AppLanguage.AR
    }

    fun saveUserProfile(profile: UserProfile) {
        try {
            val jsonAdapter = moshi.adapter(UserProfile::class.java)
            val json = jsonAdapter.toJson(profile)
            prefs.edit().putString(KEY_USER_PROFILE, json).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getUserProfile(): UserProfile? {
        val json = prefs.getString(KEY_USER_PROFILE, null) ?: return null
        return try {
            val jsonAdapter = moshi.adapter(UserProfile::class.java)
            jsonAdapter.fromJson(json)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun saveAnalysisResult(result: AnalysisResult) {
        try {
            val jsonAdapter = moshi.adapter(AnalysisResult::class.java)
            val json = jsonAdapter.toJson(result)
            prefs.edit().putString(KEY_ANALYSIS_RESULT, json).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAnalysisResult(): AnalysisResult? {
        val json = prefs.getString(KEY_ANALYSIS_RESULT, null) ?: return null
        return try {
            val jsonAdapter = moshi.adapter(AnalysisResult::class.java)
            jsonAdapter.fromJson(json)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun saveNutritionPlan(plan: NutritionPlan) {
        try {
            val jsonAdapter = moshi.adapter(NutritionPlan::class.java)
            val json = jsonAdapter.toJson(plan)
            prefs.edit().putString(KEY_NUTRITION_PLAN, json).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getNutritionPlan(): NutritionPlan? {
        val json = prefs.getString(KEY_NUTRITION_PLAN, null) ?: return null
        return try {
            val jsonAdapter = moshi.adapter(NutritionPlan::class.java)
            jsonAdapter.fromJson(json)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun saveWorkoutPlan(plan: WorkoutPlan) {
        try {
            val jsonAdapter = moshi.adapter(WorkoutPlan::class.java)
            val json = jsonAdapter.toJson(plan)
            prefs.edit().putString(KEY_WORKOUT_PLAN, json).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getWorkoutPlan(): WorkoutPlan? {
        val json = prefs.getString(KEY_WORKOUT_PLAN, null) ?: return null
        return try {
            val jsonAdapter = moshi.adapter(WorkoutPlan::class.java)
            jsonAdapter.fromJson(json)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getWaterGlasses(): Int = prefs.getInt(KEY_WATER_GLASSES, 0)

    fun saveWaterGlasses(count: Int) {
        prefs.edit().putInt(KEY_WATER_GLASSES, count).apply()
    }

    fun clearAllData() {
        prefs.edit().clear().apply()
    }
}
