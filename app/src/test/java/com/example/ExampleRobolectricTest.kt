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
}
