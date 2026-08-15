package com.example.data.model

import com.example.BuildConfig
import com.example.localization.LanguageManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class GeminiApiClient {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    private fun getApiKey(): String {
        return try {
            val key = BuildConfig.GEMINI_API_KEY
            if (key.isNull_or_empty() || key.contains("MY_GEMINI_API_KEY")) {
                "AQ.Ab8RN6LvMrHQZQx9Tg-H3WlDryGqoNtcXlRlwOETvvwdFvrzfA"
            } else key
        } catch (e: Exception) {
            "AQ.Ab8RN6LvMrHQZQx9Tg-H3WlDryGqoNtcXlRlwOETvvwdFvrzfA"
        }
    }

    private fun String?.isNull_or_empty(): Boolean = this == null || this.trim().isEmpty()

    /**
     * Prompt 1: Analyze Body & Generate Nutrition Plan
     */
    suspend fun analyzeBodyAndNutrition(profile: UserProfile): Pair<AnalysisResult, NutritionPlan> = withContext(Dispatchers.IO) {
        val calculatedBmi = calculateBmi(profile.weightKg, profile.heightCm)
        val calculatedTdee = calculateTdee(profile)

        val promptText = """
            أنت خبير تغذية ورياضة متخصص. قم بتحليل البيانات التالية لمستخدم تطبيق 'مداد':
            - الجنس: ${if (profile.gender == Gender.MALE) "ذكر" else "أنثى"}
            - العمر: ${profile.age}
            - الطول: ${profile.heightCm.toInt()} cm
            - الوزن: ${profile.weightKg} kg
            - الهدف: ${profile.goal.name}
            - مستوى النشاط: ${profile.activityLevel.name}
            - اللغة المطلوبة للرد: ${profile.language.code}

            المطلوب (قم بالرد بصيغة JSON فقط بهذا الهيكل):
            {
              "bmi_category": "فئة كتلة الجسم مثل: وزن طبيعي / زيادة وزن / تنشيف",
              "health_tip": "نصيحة صحية قصيرة ومباشرة ومحفزة جداً تناسب الهدف",
              "daily_calories": $calculatedTdee,
              "target_protein": ${ (profile.weightKg * 2.0f).toInt() },
              "target_carbs": ${ (calculatedTdee * 0.45f / 4f).toInt() },
              "target_fats": ${ (calculatedTdee * 0.25f / 9f).toInt() },
              "meals": [
                {
                  "type": "Breakfast",
                  "title": "فطور الصباح",
                  "ingredients": [
                    {"name": "بسيسة بالفاكية أو زيت الزيتون", "grams": "80g", "calories": 300},
                    {"name": "ريكوتا تونسية طازجة", "grams": "100g", "calories": 140},
                    {"name": "بيض مسلوق", "grams": "2 حبات", "calories": 150}
                  ],
                  "total_calories": 590,
                  "protein": 32,
                  "carbs": 55,
                  "fats": 20
                },
                {
                  "type": "Lunch",
                  "title": "وجبة الغداء",
                  "ingredients": [
                    {"name": "صدر دجاج مشوي أو حوت مشوي (سمك)", "grams": "200g", "calories": 330},
                    {"name": "سلطة مشوية تونسية بزيت الزيتون والفرمنتج", "grams": "150g", "calories": 180},
                    {"name": "أرز بني أو بطاطا حلوة", "grams": "150g", "calories": 200}
                  ],
                  "total_calories": 710,
                  "protein": 52,
                  "carbs": 50,
                  "fats": 22
                },
                {
                  "type": "Snack",
                  "title": "وجبة خفيفة (سناك)",
                  "ingredients": [
                    {"name": "تمر تونسي (دقلة نور) وشوفان", "grams": "5 حبات + 40g شوفان", "calories": 220},
                    {"name": "علبة تونة بالماء", "grams": "80g", "calories": 90}
                  ],
                  "total_calories": 310,
                  "protein": 24,
                  "carbs": 42,
                  "fats": 4
                },
                {
                  "type": "Dinner",
                  "title": "وجبة العشاء",
                  "ingredients": [
                    {"name": "عجة تونسية بيض وتونة بدون زيت زائد", "grams": "صحن متوسط", "calories": 350},
                    {"name": "سلطة خضراء طازجة", "grams": "200g", "calories": 60}
                  ],
                  "total_calories": 410,
                  "protein": 30,
                  "carbs": 15,
                  "fats": 18
                }
              ]
            }
        """.trimIndent()

        try {
            val rawJson = callGeminiApi(promptText)
            val parsedPair = parseNutritionJsonResponse(rawJson, profile, calculatedBmi, calculatedTdee)
            if (parsedPair != null) {
                return@withContext parsedPair
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Return scientific fallback plan
        return@withContext getFallbackNutritionPlan(profile, calculatedBmi, calculatedTdee)
    }

    /**
     * Prompt 2: Generate Workout Plan
     */
    suspend fun generateWorkoutPlan(profile: UserProfile): WorkoutPlan = withContext(Dispatchers.IO) {
        val promptText = """
            قم بإنشاء جدول تمارين جيم كامل لمستخدم بتطبيق 'مداد' بناءً على الخيارات التالية:
            - المستوى المطلوب: ${profile.gymLevel.name}
            - الهدف: ${profile.goal.name}
            - اللغة المطلوبة: ${profile.language.code}

            المطلوب (قم بالرد بصيغة JSON فقط بهذا الهيكل exact structure):
            {
              "days": [
                {
                  "day_name": "الإثنين",
                  "title": "عضلات الصدر والترابايس (Push Day)",
                  "is_rest_day": false,
                  "exercises": [
                    {"id": "ex1", "name": "Bench Press (تمرين بنش بريس مستوي)", "muscle": "الصدر السفلي والأوسط", "sets": 4, "reps": "8-12"},
                    {"id": "ex2", "name": "Incline Dumbbell Press (بنش بريس العالي)", "muscle": "الصدر العلوي", "sets": 3, "reps": "10-12"},
                    {"id": "ex3", "name": "Triceps Rope Pushdown", "muscle": "الترايسبس", "sets": 4, "reps": "12-15"}
                  ]
                },
                {
                  "day_name": "الثلاثاء",
                  "title": "عضلات الظهر والبايسبس (Pull Day)",
                  "is_rest_day": false,
                  "exercises": [
                    {"id": "ex4", "name": "Lat Pulldown (سحب ظهر أمامي)", "muscle": "الظهر العريض", "sets": 4, "reps": "10-12"},
                    {"id": "ex5", "name": "Barbell Row (تجديف بالبار)", "muscle": "منتصف الظهر", "sets": 3, "reps": "8-10"},
                    {"id": "ex6", "name": "Biceps Barbell Curl", "muscle": "البايسبس", "sets": 4, "reps": "10-12"}
                  ]
                },
                {
                  "day_name": "الأربعاء",
                  "title": "يوم راحة واستشفاء",
                  "is_rest_day": true,
                  "exercises": []
                },
                {
                  "day_name": "الخميس",
                  "title": "عضلات الأرجل والأكتاف (Legs & Shoulders)",
                  "is_rest_day": false,
                  "exercises": [
                    {"id": "ex7", "name": "Squat (تمرين السكوات بالبار)", "muscle": "الأفخاذ والأرداف", "sets": 4, "reps": "8-10"},
                    {"id": "ex8", "name": "Dumbbell Shoulder Press", "muscle": "الأكتاف", "sets": 4, "reps": "10-12"},
                    {"id": "ex9", "name": "Lateral Raises (رفرفة جانبي)", "muscle": "الكتف الجانبي", "sets": 4, "reps": "15"}
                  ]
                },
                {
                  "day_name": "الجمعة",
                  "title": "يوم راحة واسترخاء",
                  "is_rest_day": true,
                  "exercises": []
                }
              ]
            }
        """.trimIndent()

        try {
            val rawJson = callGeminiApi(promptText)
            val parsedPlan = parseWorkoutJsonResponse(rawJson, profile.gymLevel)
            if (parsedPlan != null) {
                return@withContext parsedPlan
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return@withContext getFallbackWorkoutPlan(profile)
    }

    private fun callGeminiApi(prompt: String): String {
        val apiKey = getApiKey()
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

        val jsonBody = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                    })
                })
            })
            put("generationConfig", JSONObject().apply {
                put("responseMimeType", "application/json")
            })
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val request = Request.Builder()
            .url(url)
            .post(jsonBody.toString().toRequestBody(mediaType))
            .build()

        val response = okHttpClient.newCall(request).execute()
        val responseBody = response.body?.string() ?: throw Exception("Response body is null")

        if (!response.isSuccessful) {
            throw Exception("API call failed with code ${response.code}: $responseBody")
        }

        val jsonObj = JSONObject(responseBody)
        val candidates = jsonObj.optJSONArray("candidates")
        if (candidates != null && candidates.length() > 0) {
            val firstCandidate = candidates.getJSONObject(0)
            val content = firstCandidate.getJSONObject("content")
            val parts = content.getJSONArray("parts")
            if (parts.length() > 0) {
                return parts.getJSONObject(0).getString("text")
            }
        }
        throw Exception("No content returned from Gemini API")
    }

    private fun parseNutritionJsonResponse(
        jsonString: String,
        profile: UserProfile,
        calculatedBmi: Float,
        calculatedTdee: Int
    ): Pair<AnalysisResult, NutritionPlan>? {
        return try {
            val cleanJson = jsonString.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
            val obj = JSONObject(cleanJson)

            val healthTip = obj.optString("health_tip", getDefaultHealthTip(profile))
            val dailyCalories = obj.optInt("daily_calories", calculatedTdee)
            val protein = obj.optInt("target_protein", (profile.weightKg * 2.0f).toInt())
            val carbs = obj.optInt("target_carbs", (dailyCalories * 0.45f / 4f).toInt())
            val fats = obj.optInt("target_fats", (dailyCalories * 0.25f / 9f).toInt())

            val mealsList = mutableListOf<Meal>()
            val mealsArray = obj.optJSONArray("meals")
            if (mealsArray != null) {
                for (i in 0 until mealsArray.length()) {
                    val mObj = mealsArray.getJSONObject(i)
                    val type = mObj.optString("type", "Meal ${i + 1}")
                    val title = mObj.optString("title", "وجبة ${i + 1}")
                    val totalCal = mObj.optInt("total_calories", 400)
                    val prot = mObj.optInt("protein", 25)
                    val carb = mObj.optInt("carbs", 40)
                    val fat = mObj.optInt("fats", 12)

                    val ingredientsList = mutableListOf<MealIngredient>()
                    val ingArray = mObj.optJSONArray("ingredients")
                    if (ingArray != null) {
                        for (j in 0 until ingArray.length()) {
                            val ingObj = ingArray.getJSONObject(j)
                            ingredientsList.add(
                                MealIngredient(
                                    name = ingObj.optString("name", "مكون محلي"),
                                    gramsOrQty = ingObj.optString("grams", "100g"),
                                    calories = ingObj.optInt("calories", 100)
                                )
                            )
                        }
                    }

                    mealsList.add(
                        Meal(
                            type = type,
                            title = title,
                            ingredients = ingredientsList,
                            totalCalories = totalCal,
                            proteinGrams = prot,
                            carbsGrams = carb,
                            fatGrams = fat
                        )
                    )
                }
            }

            if (mealsList.isEmpty()) return null

            val analysis = AnalysisResult(
                bmi = calculatedBmi,
                bmiCategory = getBmiCategory(calculatedBmi, profile.language),
                tdee = dailyCalories,
                healthTip = healthTip
            )

            val nutrition = NutritionPlan(
                meals = mealsList,
                totalDailyCalories = dailyCalories,
                targetProteinGrams = protein,
                targetCarbsGrams = carbs,
                targetFatGrams = fats
            )

            Pair(analysis, nutrition)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun parseWorkoutJsonResponse(jsonString: String, gymLevel: GymLevel): WorkoutPlan? {
        return try {
            val cleanJson = jsonString.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
            val obj = JSONObject(cleanJson)
            val daysArray = obj.optJSONArray("days") ?: return null

            val daysList = mutableListOf<DayWorkout>()
            for (i in 0 until daysArray.length()) {
                val dayObj = daysArray.getJSONObject(i)
                val dayName = dayObj.optString("day_name", "اليوم ${i + 1}")
                val title = dayObj.optString("title", "تمرين اليوم")
                val isRest = dayObj.optBoolean("is_rest_day", false)

                val exercisesList = mutableListOf<Exercise>()
                val exArray = dayObj.optJSONArray("exercises")
                if (exArray != null && !isRest) {
                    for (j in 0 until exArray.length()) {
                        val exObj = exArray.getJSONObject(j)
                        exercisesList.add(
                            Exercise(
                                id = exObj.optString("id", "ex_${i}_$j"),
                                name = exObj.optString("name", "تمرين رياضي"),
                                muscleTarget = exObj.optString("muscle", "العضلات المستهدفة"),
                                sets = exObj.optInt("sets", 4),
                                reps = exObj.optString("reps", "10-12")
                            )
                        )
                    }
                }

                daysList.add(
                    DayWorkout(
                        dayName = dayName,
                        title = title,
                        isRestDay = isRest,
                        exercises = exercisesList
                    )
                )
            }

            if (daysList.isEmpty()) null else WorkoutPlan(gymLevel, daysList)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Helper functions for scientific formulas
    fun calculateBmi(weightKg: Float, heightCm: Float): Float {
        if (heightCm <= 0f) return 22f
        val heightM = heightCm / 100f
        return ((weightKg / (heightM * heightM)) * 10f).roundToInt() / 10f
    }

    fun calculateTdee(profile: UserProfile): Int {
        // Mifflin-St Jeor Equation
        val bmr = if (profile.gender == Gender.MALE) {
            (10 * profile.weightKg) + (6.25 * profile.heightCm) - (5 * profile.age) + 5
        } else {
            (10 * profile.weightKg) + (6.25 * profile.heightCm) - (5 * profile.age) - 161
        }

        val activityMultiplier = when (profile.activityLevel) {
            ActivityLevel.SEDENTARY -> 1.2
            ActivityLevel.MODERATE -> 1.55
            ActivityLevel.VERY_ACTIVE -> 1.725
        }

        val maintenanceCal = (bmr * activityMultiplier).roundToInt()

        return when (profile.goal) {
            Goal.CUTTING -> (maintenanceCal - 400).coerceAtLeast(1200)
            Goal.BULKING -> maintenanceCal + 350
            Goal.MAINTAIN -> maintenanceCal
        }
    }

    private fun getBmiCategory(bmi: Float, lang: AppLanguage): String {
        return when {
            bmi < 18.5f -> when (lang) {
                AppLanguage.AR -> "نقص في الوزن (نحافة)"
                AppLanguage.FR -> "Insuffisance pondérale"
                AppLanguage.EN -> "Underweight"
            }
            bmi in 18.5f..24.9f -> when (lang) {
                AppLanguage.AR -> "وزن مثالي وطبيعي 🟢"
                AppLanguage.FR -> "Poids idéal & normal 🟢"
                AppLanguage.EN -> "Optimal Normal Weight 🟢"
            }
            bmi in 25.0f..29.9f -> when (lang) {
                AppLanguage.AR -> "زيادة خفيفة في الوزن 🟡"
                AppLanguage.FR -> "Surpoids léger 🟡"
                AppLanguage.EN -> "Slight Overweight 🟡"
            }
            else -> when (lang) {
                AppLanguage.AR -> "سمنة - يحتاج خطة تنشيف 🔴"
                AppLanguage.FR -> "Obésité - Plan recommandé 🔴"
                AppLanguage.EN -> "Overweight - Cutting plan 🔴"
            }
        }
    }

    private fun getDefaultHealthTip(profile: UserProfile): String = when (profile.language) {
        AppLanguage.AR -> when (profile.goal) {
            Goal.CUTTING -> "حافظ على شرب 3 لتر ماء يومياً وزد من نسبة البروتين للتقليل من الشهية والحفاظ على الكتلة العضلية أثناء التنشيف."
            Goal.BULKING -> "ركز على تناول الكاربوهيدرات المعقدة مثل البسيسة والشوفان قبل التمارين لزيادة الطاقة والكتلة العضلية الصافية."
            Goal.MAINTAIN -> "التوازن هو السر! حافظ على أداء تمرينك 3-4 مرات أسبوعياً مع تناول أطعمة صحية متكاملة."
        }
        AppLanguage.FR -> "Buvez 3L d'eau par jour et privilégiez les protéines locales comme la ricotta et le thon pour atteindre vos objectifs."
        AppLanguage.EN -> "Drink at least 3L of water daily and prioritize quality protein (eggs, tuna, chicken breast) to optimize recovery and energy."
    }

    private fun getFallbackNutritionPlan(
        profile: UserProfile,
        bmi: Float,
        tdee: Int
    ): Pair<AnalysisResult, NutritionPlan> {
        val analysis = AnalysisResult(
            bmi = bmi,
            bmiCategory = getBmiCategory(bmi, profile.language),
            tdee = tdee,
            healthTip = getDefaultHealthTip(profile)
        )

        val meals = when (profile.language) {
            AppLanguage.AR -> listOf(
                Meal(
                    type = "Breakfast",
                    title = "فطور الصباح التونسي الصحي",
                    ingredients = listOf(
                        MealIngredient("بسيسة تونسية بالفاكية وزيت الزيتون", "70g", 280),
                        MealIngredient("ريكوتا تونسية طازجة", "100g", 140),
                        MealIngredient("بيض مسلوق", "2 حبات", 150)
                    ),
                    totalCalories = 570,
                    proteinGrams = 30,
                    carbsGrams = 50,
                    fatGrams = 18
                ),
                Meal(
                    type = "Lunch",
                    title = "وجبة الغداء المشوية",
                    ingredients = listOf(
                        MealIngredient("صدر دجاج مشوي / حوت مشوي (سمك)", "200g", 320),
                        MealIngredient("سلطة مشوية تونسية", "150g", 160),
                        MealIngredient("أرز بني أو أرز مفور", "150g", 200)
                    ),
                    totalCalories = 680,
                    proteinGrams = 50,
                    carbsGrams = 48,
                    fatGrams = 20
                ),
                Meal(
                    type = "Snack",
                    title = "سناك الطاقة والحيوية",
                    ingredients = listOf(
                        MealIngredient("تمر دقلة نور وشوفان", "5 حبات + 40g", 210),
                        MealIngredient("علبة تونة بالماء", "80g", 90)
                    ),
                    totalCalories = 300,
                    proteinGrams = 24,
                    carbsGrams = 40,
                    fatGrams = 4
                ),
                Meal(
                    type = "Dinner",
                    title = "وجبة العشاء الخفيفة",
                    ingredients = listOf(
                        MealIngredient("عجة تونسية خفيفة بالبيض والتونة", "صحن متوسط", 340),
                        MealIngredient("سلطة خضراء طازجة", "200g", 50)
                    ),
                    totalCalories = 390,
                    proteinGrams = 28,
                    carbsGrams = 14,
                    fatGrams = 16
                )
            )
            AppLanguage.FR -> listOf(
                Meal(
                    type = "Breakfast",
                    title = "Petit-déjeuner Vitalité",
                    ingredients = listOf(
                        MealIngredient("Flocons d'avoine et fruits secs", "70g", 280),
                        MealIngredient("Ricotta fraîche ou fromage blanc", "100g", 140),
                        MealIngredient("Œufs durs ou au plat", "2 unités", 150)
                    ),
                    totalCalories = 570,
                    proteinGrams = 30,
                    carbsGrams = 50,
                    fatGrams = 18
                ),
                Meal(
                    type = "Lunch",
                    title = "Déjeuner Protéiné Grillé",
                    ingredients = listOf(
                        MealIngredient("Blanc de poulet ou poisson grillé", "200g", 320),
                        MealIngredient("Salade de légumes grillés", "150g", 160),
                        MealIngredient("Riz complet ou patates douces", "150g", 200)
                    ),
                    totalCalories = 680,
                    proteinGrams = 50,
                    carbsGrams = 48,
                    fatGrams = 20
                ),
                Meal(
                    type = "Snack",
                    title = "Collation Énergie & Musculation",
                    ingredients = listOf(
                        MealIngredient("Dattes naturelles & amandes", "5 unités + 30g", 210),
                        MealIngredient("Boîte de thon au naturel", "80g", 90)
                    ),
                    totalCalories = 300,
                    proteinGrams = 24,
                    carbsGrams = 40,
                    fatGrams = 4
                ),
                Meal(
                    type = "Dinner",
                    title = "Dîner Léger et Digeste",
                    ingredients = listOf(
                        MealIngredient("Omelette aux herbes et thon", "1 portion", 340),
                        MealIngredient("Salade verte et huile d'olive", "200g", 50)
                    ),
                    totalCalories = 390,
                    proteinGrams = 28,
                    carbsGrams = 14,
                    fatGrams = 16
                )
            )
            AppLanguage.EN -> listOf(
                Meal(
                    type = "Breakfast",
                    title = "Power Energy Breakfast",
                    ingredients = listOf(
                        MealIngredient("Oats with nuts & seeds", "70g", 280),
                        MealIngredient("Fresh cottage cheese or Greek yogurt", "100g", 140),
                        MealIngredient("Whole boiled eggs", "2 eggs", 150)
                    ),
                    totalCalories = 570,
                    proteinGrams = 30,
                    carbsGrams = 50,
                    fatGrams = 18
                ),
                Meal(
                    type = "Lunch",
                    title = "High-Protein Grill Lunch",
                    ingredients = listOf(
                        MealIngredient("Grilled chicken breast or sea fish", "200g", 320),
                        MealIngredient("Roasted vegetable mix", "150g", 160),
                        MealIngredient("Brown rice or sweet potato", "150g", 200)
                    ),
                    totalCalories = 680,
                    proteinGrams = 50,
                    carbsGrams = 48,
                    fatGrams = 20
                ),
                Meal(
                    type = "Snack",
                    title = "Pre-Workout Fuel Snack",
                    ingredients = listOf(
                        MealIngredient("Dates & natural almonds", "5 pcs + 30g", 210),
                        MealIngredient("Canned tuna in water", "80g", 90)
                    ),
                    totalCalories = 300,
                    proteinGrams = 24,
                    carbsGrams = 40,
                    fatGrams = 4
                ),
                Meal(
                    type = "Dinner",
                    title = "Light Recovery Dinner",
                    ingredients = listOf(
                        MealIngredient("Herb omelette with tuna", "1 plate", 340),
                        MealIngredient("Fresh garden salad with olive oil", "200g", 50)
                    ),
                    totalCalories = 390,
                    proteinGrams = 28,
                    carbsGrams = 14,
                    fatGrams = 16
                )
            )
        }

        val nutritionPlan = NutritionPlan(
            meals = meals,
            totalDailyCalories = tdee,
            targetProteinGrams = (profile.weightKg * 2.0f).toInt(),
            targetCarbsGrams = (tdee * 0.45f / 4f).toInt(),
            targetFatGrams = (tdee * 0.25f / 9f).toInt()
        )

        return Pair(analysis, nutritionPlan)
    }

    private fun getFallbackWorkoutPlan(profile: UserProfile): WorkoutPlan {
        val lang = profile.language
        val d1 = LanguageManager.dayMonday(lang)
        val d2 = LanguageManager.dayTuesday(lang)
        val d3 = LanguageManager.dayWednesday(lang)
        val d4 = LanguageManager.dayThursday(lang)
        val d5 = LanguageManager.dayFriday(lang)
        val d6 = LanguageManager.daySaturday(lang)
        val d7 = LanguageManager.daySunday(lang)
        val restText = LanguageManager.restDayTitle(lang)

        val days = when (profile.gymLevel) {
            GymLevel.BEGINNER -> listOf(
                DayWorkout(
                    dayName = d1,
                    title = if (lang == AppLanguage.AR) "تمرين شامل للجسم (Full Body 1)" else if (lang == AppLanguage.FR) "Entraînement Corps Complet (Full Body 1)" else "Full Body Workout (Part 1)",
                    isRestDay = false,
                    exercises = listOf(
                        Exercise("ex1", "Barbell Squat", if (lang == AppLanguage.AR) "الأفخاذ والأرداف" else if (lang == AppLanguage.FR) "Cuisses & Fessiers" else "Quads & Glutes", 3, "10-12", videoId = "ultWZbUMPL8"),
                        Exercise("ex2", "Bench Press", if (lang == AppLanguage.AR) "الصدر الأوسط" else if (lang == AppLanguage.FR) "Pectoraux" else "Mid Chest", 3, "10-12", videoId = "rT7DgCr-3pg"),
                        Exercise("ex3", "Lat Pulldown", if (lang == AppLanguage.AR) "الظهر العريض" else if (lang == AppLanguage.FR) "Grand Dorsal" else "Lats & Upper Back", 3, "10-12", videoId = "CAwf7n6Luuc"),
                        Exercise("ex4", "Dumbbell Shoulder Press", if (lang == AppLanguage.AR) "الأكتاف" else if (lang == AppLanguage.FR) "Épaules" else "Shoulders", 3, "12", videoId = "qEwKCR5JCog"),
                        Exercise("ex5", "Plank Core Hold", if (lang == AppLanguage.AR) "المعدة والبطن" else if (lang == AppLanguage.FR) "Abdominaux & Tronc" else "Core & Abs", 3, "45 sec", videoId = "pSHjTRCQxIw")
                    )
                ),
                DayWorkout(d2, restText, true, emptyList()),
                DayWorkout(
                    dayName = d3,
                    title = if (lang == AppLanguage.AR) "تمرين شامل للجسم (Full Body 2)" else if (lang == AppLanguage.FR) "Entraînement Corps Complet (Full Body 2)" else "Full Body Workout (Part 2)",
                    isRestDay = false,
                    exercises = listOf(
                        Exercise("ex6", "Romanian Deadlift", if (lang == AppLanguage.AR) "الخلفيات والأرداف" else if (lang == AppLanguage.FR) "Ischio-jambiers & Fessiers" else "Hamstrings & Glutes", 3, "10", videoId = "op9kVnSso6Q"),
                        Exercise("ex7", "Incline Dumbbell Press", if (lang == AppLanguage.AR) "الصدر العلوي" else if (lang == AppLanguage.FR) "Haut des Pectoraux" else "Upper Chest", 3, "10-12", videoId = "8iPEnn-ltC8"),
                        Exercise("ex8", "Seated Cable Row", if (lang == AppLanguage.AR) "منتصف الظهر" else if (lang == AppLanguage.FR) "Milieu du Dos" else "Mid Back", 3, "12", videoId = "G8l_8chR5BE"),
                        Exercise("ex9", "Biceps Cable Curl", if (lang == AppLanguage.AR) "البايسبس" else if (lang == AppLanguage.FR) "Biceps" else "Biceps", 3, "12-15", videoId = "ykJmrZ5v0Oo"),
                        Exercise("ex10", "Triceps Rope Extension", if (lang == AppLanguage.AR) "الترايسبس" else if (lang == AppLanguage.FR) "Triceps" else "Triceps", 3, "12-15", videoId = "2-LAMcpzODU")
                    )
                ),
                DayWorkout(d4, restText, true, emptyList()),
                DayWorkout(
                    dayName = d5,
                    title = if (lang == AppLanguage.AR) "تمرين شامل للجسم (Full Body 3)" else if (lang == AppLanguage.FR) "Entraînement Corps Complet (Full Body 3)" else "Full Body Workout (Part 3)",
                    isRestDay = false,
                    exercises = listOf(
                        Exercise("ex11", "Leg Press Machine", if (lang == AppLanguage.AR) "الأفخاذ والأرجل" else if (lang == AppLanguage.FR) "Presse à Cuisses" else "Quads & Legs", 3, "12", videoId = "IZxyjW7MPJQ"),
                        Exercise("ex12", "Dumbbell Flyes", if (lang == AppLanguage.AR) "عضلات الصدر" else if (lang == AppLanguage.FR) "Pectoraux Écartés" else "Chest Flyes", 3, "12", videoId = "rT7DgCr-3pg"),
                        Exercise("ex13", "Dumbbell Lateral Raises", if (lang == AppLanguage.AR) "الكتف الجانبي" else if (lang == AppLanguage.FR) "Élévations Latérales" else "Lateral Delts", 4, "15", videoId = "3VcKaXpzqRo"),
                        Exercise("ex14", "Plank Core Hold", if (lang == AppLanguage.AR) "الجذع والبطن" else if (lang == AppLanguage.FR) "Gainage Abdominal" else "Core Plank", 3, "60 sec", videoId = "pSHjTRCQxIw")
                    )
                ),
                DayWorkout(d6, restText, true, emptyList()),
                DayWorkout(d7, restText, true, emptyList())
            )

            GymLevel.INTERMEDIATE -> listOf(
                DayWorkout(
                    dayName = d1,
                    title = if (lang == AppLanguage.AR) "الجزء العلوي من الجسم (Upper Body A)" else if (lang == AppLanguage.FR) "Haut du Corps (Upper Body A)" else "Upper Body Power (Session A)",
                    isRestDay = false,
                    exercises = listOf(
                        Exercise("ex1", "Barbell Bench Press", if (lang == AppLanguage.AR) "الصدر" else if (lang == AppLanguage.FR) "Pectoraux" else "Chest", 4, "8-10", videoId = "rT7DgCr-3pg"),
                        Exercise("ex2", "Bent Over Barbell Row", if (lang == AppLanguage.AR) "الظهر" else if (lang == AppLanguage.FR) "Dos & Grand Dorsal" else "Back & Lats", 4, "8-10", videoId = "G8l_8chR5BE"),
                        Exercise("ex3", "Overhead Dumbbell Press", if (lang == AppLanguage.AR) "الأكتاف" else if (lang == AppLanguage.FR) "Épaules" else "Shoulders", 3, "10-12", videoId = "qEwKCR5JCog"),
                        Exercise("ex4", "Incline Dumbbell Curl", if (lang == AppLanguage.AR) "البايسبس" else if (lang == AppLanguage.FR) "Biceps" else "Biceps", 3, "12", videoId = "ykJmrZ5v0Oo"),
                        Exercise("ex5", "Triceps Rope Pushdown", if (lang == AppLanguage.AR) "الترايسبس" else if (lang == AppLanguage.FR) "Triceps" else "Triceps", 3, "12", videoId = "2-LAMcpzODU")
                    )
                ),
                DayWorkout(
                    dayName = d2,
                    title = if (lang == AppLanguage.AR) "الجزء السفلي والبطن (Lower Body A)" else if (lang == AppLanguage.FR) "Bas du Corps (Lower Body A)" else "Lower Body & Core (Session A)",
                    isRestDay = false,
                    exercises = listOf(
                        Exercise("ex6", "Barbell Back Squat", if (lang == AppLanguage.AR) "الأفخاذ" else if (lang == AppLanguage.FR) "Cuisses & Fessiers" else "Quads & Glutes", 4, "8-10", videoId = "ultWZbUMPL8"),
                        Exercise("ex7", "Leg Press Machine", if (lang == AppLanguage.AR) "الأفخاذ والأرجل" else if (lang == AppLanguage.FR) "Presse à Cuisses" else "Quads & Calves", 4, "12", videoId = "IZxyjW7MPJQ"),
                        Exercise("ex8", "Romanian Deadlift", if (lang == AppLanguage.AR) "الأفخاذ والأرداف" else if (lang == AppLanguage.FR) "Ischio-jambiers" else "Hamstrings & Glutes", 3, "10-12", videoId = "op9kVnSso6Q"),
                        Exercise("ex9", "Plank Core Hold", if (lang == AppLanguage.AR) "عضلات البطن والجذع" else if (lang == AppLanguage.FR) "Gainage Abdominal" else "Core & Abs", 3, "60 sec", videoId = "pSHjTRCQxIw")
                    )
                ),
                DayWorkout(d3, restText, true, emptyList()),
                DayWorkout(
                    dayName = d4,
                    title = if (lang == AppLanguage.AR) "الجزء العلوي من الجسم (Upper Body B)" else if (lang == AppLanguage.FR) "Haut du Corps (Upper Body B)" else "Upper Body Hypertrophy (Session B)",
                    isRestDay = false,
                    exercises = listOf(
                        Exercise("ex10", "Incline Barbell Bench Press", if (lang == AppLanguage.AR) "الصدر العلوي" else if (lang == AppLanguage.FR) "Haut des Pectoraux" else "Upper Chest", 4, "8-10", videoId = "8iPEnn-ltC8"),
                        Exercise("ex11", "Lat Pulldown Wide Grip", if (lang == AppLanguage.AR) "الظهر العريض" else if (lang == AppLanguage.FR) "Grand Dorsal" else "Wide Lats", 4, "10-12", videoId = "CAwf7n6Luuc"),
                        Exercise("ex12", "Lateral Raises", if (lang == AppLanguage.AR) "الكتف الجانبي" else if (lang == AppLanguage.FR) "Élévations Latérales" else "Side Delts", 4, "15", videoId = "3VcKaXpzqRo"),
                        Exercise("ex13", "Biceps Barbell Curl", if (lang == AppLanguage.AR) "البايسبس والساعد" else if (lang == AppLanguage.FR) "Biceps à la barre" else "Biceps & Forearms", 3, "12", videoId = "ykJmrZ5v0Oo")
                    )
                ),
                DayWorkout(
                    dayName = d5,
                    title = if (lang == AppLanguage.AR) "الجزء السفلي والبطن (Lower Body B)" else if (lang == AppLanguage.FR) "Bas du Corps (Lower Body B)" else "Lower Body Strength (Session B)",
                    isRestDay = false,
                    exercises = listOf(
                        Exercise("ex14", "Deadlift", if (lang == AppLanguage.AR) "الظهر السفلي والأرجل" else if (lang == AppLanguage.FR) "Soulevé de Terre" else "Lower Back & Legs", 4, "6-8", videoId = "op9kVnSso6Q"),
                        Exercise("ex15", "Barbell Squats", if (lang == AppLanguage.AR) "الأفخاذ الأمامية" else if (lang == AppLanguage.FR) "Quadriceps" else "Quads", 3, "10-12", videoId = "ultWZbUMPL8"),
                        Exercise("ex16", "Plank Core Exercise", if (lang == AppLanguage.AR) "الجذع والبطن" else if (lang == AppLanguage.FR) "Gainage Abdominal" else "Core & Obliques", 4, "45 sec", videoId = "pSHjTRCQxIw")
                    )
                ),
                DayWorkout(d6, restText, true, emptyList()),
                DayWorkout(d7, restText, true, emptyList())
            )

            GymLevel.ADVANCED -> listOf(
                DayWorkout(
                    dayName = d1,
                    title = if (lang == AppLanguage.AR) "يوم الدفع (Push Day - الصدر والأكتاف والتراي)" else if (lang == AppLanguage.FR) "Jour Poussée (Push Day - Pecs/Épaules/Triceps)" else "Push Day (Chest, Shoulders & Triceps)",
                    isRestDay = false,
                    exercises = listOf(
                        Exercise("ex1", "Barbell Bench Press", if (lang == AppLanguage.AR) "الصدر الرئيسي" else if (lang == AppLanguage.FR) "Pectoraux" else "Main Chest", 4, "6-8", videoId = "rT7DgCr-3pg"),
                        Exercise("ex2", "Incline Dumbbell Press", if (lang == AppLanguage.AR) "الصدر العلوي" else if (lang == AppLanguage.FR) "Haut des Pecs" else "Upper Chest", 4, "8-10", videoId = "8iPEnn-ltC8"),
                        Exercise("ex3", "Dumbbell Press Shoulders", if (lang == AppLanguage.AR) "الأكتاف الأمامية" else if (lang == AppLanguage.FR) "Épaules" else "Front Delts", 4, "8-10", videoId = "qEwKCR5JCog"),
                        Exercise("ex4", "Lateral Raises", if (lang == AppLanguage.AR) "الأكتاف الجانبية" else if (lang == AppLanguage.FR) "Élévations Latérales" else "Side Delts", 4, "15", videoId = "3VcKaXpzqRo"),
                        Exercise("ex5", "Triceps Rope Pushdown", if (lang == AppLanguage.AR) "الترايسبس" else if (lang == AppLanguage.FR) "Triceps" else "Triceps", 4, "10-12", videoId = "2-LAMcpzODU")
                    )
                ),
                DayWorkout(
                    dayName = d2,
                    title = if (lang == AppLanguage.AR) "يوم السحب (Pull Day - الظهر والبايسبس)" else if (lang == AppLanguage.FR) "Jour Tirage (Pull Day - Dos & Biceps)" else "Pull Day (Back, Lats & Biceps)",
                    isRestDay = false,
                    exercises = listOf(
                        Exercise("ex6", "Deadlift", if (lang == AppLanguage.AR) "الظهر الكامل" else if (lang == AppLanguage.FR) "Dos & Chaîne Postérieure" else "Full Back & Posterior Chain", 4, "6-8", videoId = "op9kVnSso6Q"),
                        Exercise("ex7", "Wide Grip Lat Pulldown", if (lang == AppLanguage.AR) "الظهر العريض" else if (lang == AppLanguage.FR) "Grand Dorsal" else "Wide Lats", 4, "10-12", videoId = "CAwf7n6Luuc"),
                        Exercise("ex8", "Barbell Bent Over Row", if (lang == AppLanguage.AR) "منتصف الظهر" else if (lang == AppLanguage.FR) "Milieu du Dos" else "Mid Back", 4, "8-10", videoId = "G8l_8chR5BE"),
                        Exercise("ex9", "Barbell Biceps Curl", if (lang == AppLanguage.AR) "البايسبس" else if (lang == AppLanguage.FR) "Biceps" else "Biceps Peak", 4, "10-12", videoId = "ykJmrZ5v0Oo")
                    )
                ),
                DayWorkout(
                    dayName = d3,
                    title = if (lang == AppLanguage.AR) "يوم الأرجل (Legs Day - الأرجل والمعدة)" else if (lang == AppLanguage.FR) "Jour Jambes (Legs Day & Tronc)" else "Legs Day & Core Stability",
                    isRestDay = false,
                    exercises = listOf(
                        Exercise("ex10", "Barbell Squat", if (lang == AppLanguage.AR) "الأفخاذ الأمامية" else if (lang == AppLanguage.FR) "Quadriceps & Fessiers" else "Quads & Glutes", 4, "6-8", videoId = "ultWZbUMPL8"),
                        Exercise("ex11", "Romanian Deadlift", if (lang == AppLanguage.AR) "خلفيات الأرجل" else if (lang == AppLanguage.FR) "Ischio-jambiers" else "Hamstrings", 4, "8-10", videoId = "op9kVnSso6Q"),
                        Exercise("ex12", "Leg Press Machine", if (lang == AppLanguage.AR) "الأرجل" else if (lang == AppLanguage.FR) "Presse à Cuisses" else "Full Legs", 3, "12", videoId = "IZxyjW7MPJQ"),
                        Exercise("ex13", "Plank Core Stability", if (lang == AppLanguage.AR) "عضلات البطن والجذع" else if (lang == AppLanguage.FR) "Gainage Tronc" else "Core & Abdominals", 4, "60 sec", videoId = "pSHjTRCQxIw")
                    )
                ),
                DayWorkout(d4, restText, true, emptyList()),
                DayWorkout(
                    dayName = d5,
                    title = if (lang == AppLanguage.AR) "جزء علوي مستهدف (Upper Focus)" else if (lang == AppLanguage.FR) "Haut du Corps Ciblé (Upper Focus)" else "Upper Body Hypertrophy",
                    isRestDay = false,
                    exercises = listOf(
                        Exercise("ex14", "Dumbbell Incline Bench Press", if (lang == AppLanguage.AR) "الصدر" else if (lang == AppLanguage.FR) "Pectoraux" else "Incline Chest", 4, "10-12", videoId = "8iPEnn-ltC8"),
                        Exercise("ex15", "Seated Cable Row", if (lang == AppLanguage.AR) "الظهر" else if (lang == AppLanguage.FR) "Dos" else "Mid Lats", 4, "10-12", videoId = "G8l_8chR5BE"),
                        Exercise("ex16", "Dumbbell Lateral Raises", if (lang == AppLanguage.AR) "الأكتاف" else if (lang == AppLanguage.FR) "Épaules" else "Side Delts", 4, "15", videoId = "3VcKaXpzqRo")
                    )
                ),
                DayWorkout(
                    dayName = d6,
                    title = if (lang == AppLanguage.AR) "أرجل وبطن متقدم (Legs & Core)" else if (lang == AppLanguage.FR) "Jambes & Abdominaux (Legs & Core)" else "Legs & Core Conditioning",
                    isRestDay = false,
                    exercises = listOf(
                        Exercise("ex17", "Hack Squat / Leg Press", if (lang == AppLanguage.AR) "الأفخاذ" else if (lang == AppLanguage.FR) "Cuisses" else "Quads", 4, "10-12", videoId = "IZxyjW7MPJQ"),
                        Exercise("ex18", "Lying Leg Curls", if (lang == AppLanguage.AR) "الخلفيات" else if (lang == AppLanguage.FR) "Ischio-jambiers" else "Hamstrings", 4, "12", videoId = "op9kVnSso6Q"),
                        Exercise("ex19", "Abdominal Cable Crunches", if (lang == AppLanguage.AR) "البطن" else if (lang == AppLanguage.FR) "Abdominaux" else "Abs Crunches", 4, "15", videoId = "pSHjTRCQxIw")
                    )
                ),
                DayWorkout(d7, restText, true, emptyList())
            )
        }

        return WorkoutPlan(profile.gymLevel, days)
    }

    /**
     * Prompt 3: AI Meal Calorie & Macro Calculator
     */
    suspend fun calculateCustomMealWithAi(
        selectedFoods: List<SelectedFoodItem>,
        profile: UserProfile
    ): AiMealCalculationResult = withContext(Dispatchers.IO) {
        if (selectedFoods.isEmpty()) {
            return@withContext AiMealCalculationResult(
                totalCalories = 0,
                totalProteinGrams = 0f,
                totalCarbsGrams = 0f,
                totalFatGrams = 0f,
                proteinPercentage = 0,
                carbsPercentage = 0,
                fatPercentage = 0,
                glycemicEstimate = "-",
                bestTiming = "-",
                aiInsight = if (profile.language == AppLanguage.AR) "الرجاء اختيار مكونات من القائمة لحساب السعرات والماكروز" else "Please select ingredients to calculate calories",
                isAiGenerated = false
            )
        }

        // Calculate exact base mathematical values
        val exactCalories = selectedFoods.sumOf { it.calories }
        val exactProtein = selectedFoods.fold(0f) { acc, item -> acc + item.protein }
        val exactCarbs = selectedFoods.fold(0f) { acc, item -> acc + item.carbs }
        val exactFat = selectedFoods.fold(0f) { acc, item -> acc + item.fat }

        val totalMacroGrams = exactProtein + exactCarbs + exactFat
        val protPct = if (totalMacroGrams > 0) ((exactProtein * 4f) / (exactCalories.coerceAtLeast(1)) * 100).roundToInt().coerceIn(0, 100) else 0
        val carbPct = if (totalMacroGrams > 0) ((exactCarbs * 4f) / (exactCalories.coerceAtLeast(1)) * 100).roundToInt().coerceIn(0, 100) else 0
        val fatPct = (100 - protPct - carbPct).coerceAtLeast(0)

        val ingredientsSummary = selectedFoods.joinToString("\n") {
            "- ${it.foodItem.nameAr} (${it.foodItem.nameEn}): ${it.quantityGrams}g"
        }

        val promptText = """
            أنت خبير تغذية وعلوم رياضية في تطبيق 'مداد'. قام المستخدم بتجميع وجبة مخصصة من المكونات الطبيعية التالية:
            $ingredientsSummary

            بيانات المستخدم:
            - الهدف: ${profile.goal.name}
            - الجنس: ${profile.gender.name}
            - اللغة المطلوبة للرد: ${profile.language.code}

            المطلوب:
            قم بتحليل هذه الوجبة بدقة وقدم النتيجة بتنسيق JSON فقط بهذا الشكل:
            {
              "total_calories": $exactCalories,
              "protein_grams": ${((exactProtein * 10).roundToInt() / 10.0)},
              "carbs_grams": ${((exactCarbs * 10).roundToInt() / 10.0)},
              "fat_grams": ${((exactFat * 10).roundToInt() / 10.0)},
              "glycemic_estimate": "منخفض / متوسط / مرتفع",
              "best_timing": "مثالي كـ: وجبة قبل التمرين بساعتين / وجبة استشفاء بعد التمرين / وجبة غداء رئيسية",
              "ai_insight": "نصيحة غذائية ذكية وموجزة (جملتان) تشرح فوائد هذه التركيبة العضلية والصحية وكيف تخدم هدف ${profile.goal.name}."
            }
        """.trimIndent()

        try {
            val rawJson = callGeminiApi(promptText)
            val cleanJson = rawJson.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
            val obj = JSONObject(cleanJson)

            val cal = obj.optInt("total_calories", exactCalories)
            val p = obj.optDouble("protein_grams", exactProtein.toDouble()).toFloat()
            val c = obj.optDouble("carbs_grams", exactCarbs.toDouble()).toFloat()
            val f = obj.optDouble("fat_grams", exactFat.toDouble()).toFloat()
            val gly = obj.optString("glycemic_estimate", if (profile.language == AppLanguage.AR) "متوازن (Low-Med GI)" else "Balanced GI")
            val timing = obj.optString("best_timing", getFallbackTiming(exactProtein, exactCarbs, profile.language))
            val insight = obj.optString("ai_insight", getFallbackAiInsight(selectedFoods, profile))

            return@withContext AiMealCalculationResult(
                totalCalories = cal,
                totalProteinGrams = p,
                totalCarbsGrams = c,
                totalFatGrams = f,
                proteinPercentage = protPct,
                carbsPercentage = carbPct,
                fatPercentage = fatPct,
                glycemicEstimate = gly,
                bestTiming = timing,
                aiInsight = insight,
                isAiGenerated = true
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Resilient scientific fallback
        return@withContext AiMealCalculationResult(
            totalCalories = exactCalories,
            totalProteinGrams = ((exactProtein * 10).roundToInt() / 10f),
            totalCarbsGrams = ((exactCarbs * 10).roundToInt() / 10f),
            totalFatGrams = ((exactFat * 10).roundToInt() / 10f),
            proteinPercentage = protPct,
            carbsPercentage = carbPct,
            fatPercentage = fatPct,
            glycemicEstimate = if (profile.language == AppLanguage.AR) "متوازن وطبيعي" else "Balanced GI",
            bestTiming = getFallbackTiming(exactProtein, exactCarbs, profile.language),
            aiInsight = getFallbackAiInsight(selectedFoods, profile),
            isAiGenerated = false
        )
    }

    private fun getFallbackTiming(protein: Float, carbs: Float, lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> when {
            carbs > 40f && protein < 20f -> "مثالية قبل التمرين بـ 90-120 دقيقة لشحن طاقة العضلات"
            protein >= 25f -> "مثالية بعد التمرين مباشرة لتسريع البناء والاستشفاء العضلي"
            else -> "وجبة مغذية متكاملة مناسبة كغداء أو عشاء رئيسي"
        }
        AppLanguage.FR -> if (protein >= 25f) "Idéal post-entraînement pour la récupération musculaire" else "Repas équilibré pour l'énergie quotidienne"
        AppLanguage.EN -> if (protein >= 25f) "Ideal post-workout for muscle protein synthesis" else "Balanced meal for daily sustained energy"
    }

    private fun getFallbackAiInsight(selectedFoods: List<SelectedFoodItem>, profile: UserProfile): String {
        val totalProtein = selectedFoods.fold(0f) { acc, item -> acc + item.protein }
        val isHighProtein = totalProtein >= 25f
        return when (profile.language) {
            AppLanguage.AR -> if (isHighProtein) {
                "تركيبة ممتازة غنية بالأحماض الأمينية الأساسية والمعادن الطبيعية، تساهم في الحفاظ على الكتلة العضلية وتعزيز الشعور بالشبع لفترة طويلة."
            } else {
                "مزيج طبيعي متوازن يوفر فيتامينات وألياف حيوية تدعم نشاط الجهاز الهضمي وثبات مستويات الطاقة بدون هبوط مفاجئ في سكر الدم."
            }
            AppLanguage.FR -> if (isHighProtein) {
                "Excellente combinaison riche en acides aminés et nutriments complets pour la récupération et la satiété."
            } else {
                "Mélange naturel équilibré apportant des vitamines et fibres essentielles pour une énergie stable."
            }
            AppLanguage.EN -> if (isHighProtein) {
                "Outstanding whole-food blend providing complete amino acids and micronutrients to support muscle recovery and prolonged satiety."
            } else {
                "Balanced natural combination providing essential fiber and vitamins for steady metabolic energy without sugar spikes."
            }
        }
    }
}

