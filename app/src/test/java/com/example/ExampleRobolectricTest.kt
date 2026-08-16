package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("مداد", appName)
  }

  @Test
  fun `exercise tutorial helper returns valid video IDs and structured instructions`() {
    val benchTutorial = com.example.ui.components.ExerciseTutorialHelper.getTutorial(
      exerciseName = "Bench Press (تمرين بنش بريس مستوي)",
      muscleTarget = "الصدر",
      setsAndReps = "4 × 10",
      lang = com.example.data.model.AppLanguage.AR
    )
    org.junit.Assert.assertEquals("rT7DGvm-3yy", benchTutorial.videoId)
    org.junit.Assert.assertTrue(benchTutorial.setupTip.isNotEmpty())
    org.junit.Assert.assertTrue(benchTutorial.gripTip.isNotEmpty())
    org.junit.Assert.assertTrue(benchTutorial.executionTip.isNotEmpty())
    org.junit.Assert.assertTrue(benchTutorial.breathingTip.isNotEmpty())

    val squatTutorialFr = com.example.ui.components.ExerciseTutorialHelper.getTutorial(
      exerciseName = "Barbell Squat",
      muscleTarget = "Cuisses",
      setsAndReps = "4 × 8",
      lang = com.example.data.model.AppLanguage.FR
    )
    org.junit.Assert.assertEquals("ultWZbUMPL8", squatTutorialFr.videoId)
    org.junit.Assert.assertTrue(squatTutorialFr.executionTip.contains("parallèles"))

    val latTutorial = com.example.ui.components.ExerciseTutorialHelper.getTutorial(
      exerciseName = "Lat Pulldown (سحب ظهر)",
      muscleTarget = "Lats",
      setsAndReps = "4 × 12",
      lang = com.example.data.model.AppLanguage.EN
    )
    org.junit.Assert.assertEquals("CAwf7n6Luuc", latTutorial.videoId)

    val shoulderTutorial = com.example.ui.components.ExerciseTutorialHelper.getTutorial(
      exerciseName = "Dumbbell Press Shoulders",
      muscleTarget = "Shoulders",
      setsAndReps = "3 × 12",
      lang = com.example.data.model.AppLanguage.EN
    )
    org.junit.Assert.assertEquals("qEwKCR5JCog", shoulderTutorial.videoId)

    val plankTutorial = com.example.ui.components.ExerciseTutorialHelper.getTutorial(
      exerciseName = "Plank Core (تمارين المعدة)",
      muscleTarget = "Core",
      setsAndReps = "3 × 45 sec",
      lang = com.example.data.model.AppLanguage.AR
    )
    org.junit.Assert.assertEquals("pSHjTRCQxIw", plankTutorial.videoId)
  }

  @Test
  fun `tdee and bmi explanation dialog strings in Arabic match exact definitions`() {
    val tdeeTitle = com.example.localization.LanguageManager.tdeeDialogTitle(com.example.data.model.AppLanguage.AR)
    val tdeeContent = com.example.localization.LanguageManager.tdeeDialogContent(com.example.data.model.AppLanguage.AR)
    val bmiTitle = com.example.localization.LanguageManager.bmiDialogTitle(com.example.data.model.AppLanguage.AR)
    val bmiContent = com.example.localization.LanguageManager.bmiDialogContent(com.example.data.model.AppLanguage.AR)
    val closeBtn = com.example.localization.LanguageManager.closeBtn(com.example.data.model.AppLanguage.AR)

    org.junit.Assert.assertEquals("ما هو TDEE؟", tdeeTitle)
    org.junit.Assert.assertTrue(tdeeContent.contains("إجمالي السعرات الحرارية التي يحرقها جسمك يومياً"))
    org.junit.Assert.assertEquals("ما هو BMI؟", bmiTitle)
    org.junit.Assert.assertTrue(bmiContent.contains("مؤشر كتلة الجسم"))
    org.junit.Assert.assertEquals("إغلاق", closeBtn)
  }

  @Test
  fun `user profile default metrics are empty and language manager has placeholders`() {
    val defaultProfile = com.example.data.model.UserProfile()
    org.junit.Assert.assertEquals(0, defaultProfile.age)
    org.junit.Assert.assertEquals(0f, defaultProfile.heightCm, 0.01f)
    org.junit.Assert.assertEquals(0f, defaultProfile.weightKg, 0.01f)

    val agePh = com.example.localization.LanguageManager.agePlaceholder(com.example.data.model.AppLanguage.AR)
    val heightPh = com.example.localization.LanguageManager.heightPlaceholder(com.example.data.model.AppLanguage.AR)
    val weightPh = com.example.localization.LanguageManager.weightPlaceholder(com.example.data.model.AppLanguage.AR)

    org.junit.Assert.assertTrue(agePh.isNotEmpty())
    org.junit.Assert.assertTrue(heightPh.isNotEmpty())
    org.junit.Assert.assertTrue(weightPh.isNotEmpty())
  }

  @Test
  fun `instagram intent uri matches official handle`() {
    val instagramUrl = "https://www.instagram.com/aminlgeek/"
    val uri = android.net.Uri.parse(instagramUrl)
    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, uri)
    org.junit.Assert.assertEquals("https://www.instagram.com/aminlgeek/", intent.data.toString())
    org.junit.Assert.assertEquals(android.content.Intent.ACTION_VIEW, intent.action)
  }

  @Test
  fun `meal calculator supports all 7 required food categories and units`() {
    val foods = com.example.data.model.WholeFoodsRepository.foods
    val categories = foods.map { it.category }.toSet()

    org.junit.Assert.assertTrue(categories.contains(com.example.data.model.FoodCategory.VEGETABLES))
    org.junit.Assert.assertTrue(categories.contains(com.example.data.model.FoodCategory.FRUITS))
    org.junit.Assert.assertTrue(categories.contains(com.example.data.model.FoodCategory.MEAT_POULTRY_FISH))
    org.junit.Assert.assertTrue(categories.contains(com.example.data.model.FoodCategory.LEGUMES_GRAINS))
    org.junit.Assert.assertTrue(categories.contains(com.example.data.model.FoodCategory.DAIRY))
    org.junit.Assert.assertTrue(categories.contains(com.example.data.model.FoodCategory.NUTS_DRIED_FRUITS))
    org.junit.Assert.assertTrue(categories.contains(com.example.data.model.FoodCategory.OILS_HEALTHY_FATS))

    // Check serving units
    val milk = foods.first { it.id == "fresh_whole_milk" }
    val oliveOil = foods.first { it.id == "extra_virgin_olive_oil" }
    val chicken = foods.first { it.id == "chicken_breast" }

    org.junit.Assert.assertEquals(com.example.data.model.ServingUnit.ML, milk.unit)
    org.junit.Assert.assertEquals(com.example.data.model.ServingUnit.ML, oliveOil.unit)
    org.junit.Assert.assertEquals(com.example.data.model.ServingUnit.GRAMS, chicken.unit)

    org.junit.Assert.assertEquals("مل", milk.unitLabel(com.example.data.model.AppLanguage.AR))
    org.junit.Assert.assertEquals("غرام", chicken.unitLabel(com.example.data.model.AppLanguage.AR))
    org.junit.Assert.assertEquals("ml", milk.unitLabel(com.example.data.model.AppLanguage.EN))
    org.junit.Assert.assertEquals("g", chicken.unitLabel(com.example.data.model.AppLanguage.EN))
  }

  @Test
  fun `selected food items calculate exact calories and macros correctly`() {
    val foods = com.example.data.model.WholeFoodsRepository.foods
    val chicken = foods.first { it.id == "chicken_breast" }
    val oats = foods.first { it.id == "oats_whole" }

    val selectedChicken = com.example.data.model.SelectedFoodItem(chicken, 200) // 200g chicken: 165*2 = 330 kcal, 31*2 = 62g protein
    val selectedOats = com.example.data.model.SelectedFoodItem(oats, 50) // 50g oats: 389*0.5 = 194 kcal, 16.9*0.5 = 8.45g protein

    org.junit.Assert.assertEquals(330, selectedChicken.calories)
    org.junit.Assert.assertEquals(62.0f, selectedChicken.protein, 0.01f)
    org.junit.Assert.assertEquals(194, selectedOats.calories)
    org.junit.Assert.assertEquals(8.45f, selectedOats.protein, 0.01f)
  }

  @Test
  fun `gym alarm model calculates formatted time and repeat days summary`() {
    val morningAlarm = com.example.data.model.GymAlarm(
      hour = 6,
      minute = 30,
      label = "Leg Day Workout",
      isEnabled = true,
      repeatDays = setOf(java.util.Calendar.MONDAY, java.util.Calendar.WEDNESDAY, java.util.Calendar.FRIDAY)
    )

    org.junit.Assert.assertEquals("06:30 AM", morningAlarm.formattedTime(use24Hour = false))
    org.junit.Assert.assertEquals("06:30", morningAlarm.formattedTime(use24Hour = true))

    val (timeStr, period) = morningAlarm.timeParts()
    org.junit.Assert.assertEquals("06:30", timeStr)
    org.junit.Assert.assertEquals("AM", period)

    val summaryAr = morningAlarm.getRepeatDaysSummary(com.example.data.model.AppLanguage.AR)
    org.junit.Assert.assertTrue(summaryAr.contains("الإثنين") && summaryAr.contains("الأربعاء") && summaryAr.contains("الجمعة"))

    val summaryEn = morningAlarm.getRepeatDaysSummary(com.example.data.model.AppLanguage.EN)
    org.junit.Assert.assertTrue(summaryEn.contains("Mon") && summaryEn.contains("Wed") && summaryEn.contains("Fri"))
  }

  @Test
  fun `gym alarm repository saves adds and toggles alarms correctly`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = com.example.data.local.GymAlarmRepository(context)

    val initialAlarms = repo.getAlarms()
    org.junit.Assert.assertTrue(initialAlarms.isNotEmpty())

    val customAlarm = com.example.data.model.GymAlarm(
      id = "custom_test_alarm_123",
      hour = 18,
      minute = 45,
      label = "Evening Cardio Session ⚡",
      isEnabled = true,
      repeatDays = setOf(java.util.Calendar.TUESDAY, java.util.Calendar.THURSDAY)
    )

    repo.addOrUpdateAlarm(customAlarm)
    val fetched = repo.getAlarm("custom_test_alarm_123")
    org.junit.Assert.assertNotNull(fetched)
    org.junit.Assert.assertEquals("Evening Cardio Session ⚡", fetched?.label)
    org.junit.Assert.assertEquals(18, fetched?.hour)
    org.junit.Assert.assertEquals(45, fetched?.minute)
    org.junit.Assert.assertTrue(fetched?.isEnabled == true)

    // Toggle off
    repo.toggleAlarm("custom_test_alarm_123", false)
    val toggled = repo.getAlarm("custom_test_alarm_123")
    org.junit.Assert.assertFalse(toggled?.isEnabled == true)

    // Delete
    repo.deleteAlarm("custom_test_alarm_123")
    val deleted = repo.getAlarm("custom_test_alarm_123")
    org.junit.Assert.assertNull(deleted)
  }

  @Test
  fun `gym alarm translations return accurate labels across AR FR and EN`() {
    val tabAr = com.example.localization.LanguageManager.tabGymAlarm(com.example.data.model.AppLanguage.AR)
    val tabFr = com.example.localization.LanguageManager.tabGymAlarm(com.example.data.model.AppLanguage.FR)
    val tabEn = com.example.localization.LanguageManager.tabGymAlarm(com.example.data.model.AppLanguage.EN)

    org.junit.Assert.assertTrue(tabAr.contains("منبه الجيم"))
    org.junit.Assert.assertTrue(tabFr.contains("Alarme Gym"))
    org.junit.Assert.assertTrue(tabEn.contains("Gym Alarm"))
  }
}
