package com.example.localization

import com.example.data.model.ActivityLevel
import com.example.data.model.AppLanguage
import com.example.data.model.Gender
import com.example.data.model.GymLevel
import com.example.data.model.Goal

object LanguageManager {

    fun welcomeTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مرحباً بك في مداد"
        AppLanguage.FR -> "Bienvenue sur Midad"
        AppLanguage.EN -> "Welcome to Midad"
    }

    fun welcomeSlogan(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "جسمك، طاقتك، وحياتك... في مكان واحد."
        AppLanguage.FR -> "Votre corps, votre énergie, votre vie... en un seul endroit."
        AppLanguage.EN -> "Your body, your energy, your life... all in one place."
    }

    fun selectLanguageHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "اختر لغتك المفضلة"
        AppLanguage.FR -> "Choisissez votre langue"
        AppLanguage.EN -> "Choose your language"
    }

    fun btnStartNow(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "ابدأ الآن 🚀"
        AppLanguage.FR -> "Commencer 🚀"
        AppLanguage.EN -> "Start Now 🚀"
    }

    // Screen 2
    fun personalDataTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "البيانات الشخصية"
        AppLanguage.FR -> "Données Personnelles"
        AppLanguage.EN -> "Personal Data"
    }

    fun genderLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الجنس"
        AppLanguage.FR -> "Genre"
        AppLanguage.EN -> "Gender"
    }

    fun maleLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "ذكر ♂️"
        AppLanguage.FR -> "Homme ♂️"
        AppLanguage.EN -> "Male ♂️"
    }

    fun femaleLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "أنثى ♀️"
        AppLanguage.FR -> "Femme ♀️"
        AppLanguage.EN -> "Female ♀️"
    }

    fun ageLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "العمر (سنوات) *"
        AppLanguage.FR -> "Âge (ans) *"
        AppLanguage.EN -> "Age (years) *"
    }

    fun agePlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مثال: 25"
        AppLanguage.FR -> "ex. 25"
        AppLanguage.EN -> "e.g., 25"
    }

    fun heightLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الطول (سم - cm) *"
        AppLanguage.FR -> "Taille (cm) *"
        AppLanguage.EN -> "Height (cm) *"
    }

    fun heightPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مثال: 175"
        AppLanguage.FR -> "ex. 175"
        AppLanguage.EN -> "e.g., 175"
    }

    fun weightLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الوزن الحالي (كغ - kg) *"
        AppLanguage.FR -> "Poids actuel (kg) *"
        AppLanguage.EN -> "Current Weight (kg) *"
    }

    fun weightPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مثال: 75"
        AppLanguage.FR -> "ex. 75"
        AppLanguage.EN -> "e.g., 75"
    }

    fun genderRequiredLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الجنس *"
        AppLanguage.FR -> "Genre *"
        AppLanguage.EN -> "Gender *"
    }

    fun btnNext(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "التالي ➡️"
        AppLanguage.FR -> "Suivant ➡️"
        AppLanguage.EN -> "Next ➡️"
    }

    fun fillAllDataNotice(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "⚠️ يرجى اختيار الجنس وإدخال العمر والطول والوزن للمتابعة"
        AppLanguage.FR -> "⚠️ Veuillez choisir le genre et renseigner l'âge, la taille et le poids"
        AppLanguage.EN -> "⚠️ Please select gender and enter valid age, height, and weight"
    }

    // Screen 3
    fun goalLifestyleTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الهدف ومستوى النشاط"
        AppLanguage.FR -> "Objectif & Style de vie"
        AppLanguage.EN -> "Goal & Lifestyle"
    }

    fun mainGoalHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الهدف الرئيسي"
        AppLanguage.FR -> "Objectif Principal"
        AppLanguage.EN -> "Main Goal"
    }

    fun goalName(goal: Goal, lang: AppLanguage): String = when (goal) {
        Goal.CUTTING -> when (lang) {
            AppLanguage.AR -> "📉 تنشيف وخسارة الدهون"
            AppLanguage.FR -> "📉 Séchage & Perte de gras"
            AppLanguage.EN -> "📉 Cutting & Fat Loss"
        }
        Goal.BULKING -> when (lang) {
            AppLanguage.AR -> "💪 بناء العضلات وزيادة الوزن"
            AppLanguage.FR -> "💪 Prise de masse & Muscle"
            AppLanguage.EN -> "💪 Bulking & Muscle Gain"
        }
        Goal.MAINTAIN -> when (lang) {
            AppLanguage.AR -> "⚖️ المحافظة على الوزن واللياقة"
            AppLanguage.FR -> "⚖️ Maintien de poids & Forme"
            AppLanguage.EN -> "⚖️ Weight Maintenance & Fitness"
        }
    }

    fun activityHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مستوى النشاط اليومي"
        AppLanguage.FR -> "Niveau d'activité quotidienne"
        AppLanguage.EN -> "Daily Activity Level"
    }

    fun activityName(act: ActivityLevel, lang: AppLanguage): String = when (act) {
        ActivityLevel.SEDENTARY -> when (lang) {
            AppLanguage.AR -> "🧘 خامل (عمل مكتبي / حركة قليلة)"
            AppLanguage.FR -> "🧘 Sédentaire (Travail de bureau / Peu de mouvement)"
            AppLanguage.EN -> "🧘 Sedentary (Desk job / Low movement)"
        }
        ActivityLevel.MODERATE -> when (lang) {
            AppLanguage.AR -> "🚶 متوسط النشاط (حركة يومية عادية)"
            AppLanguage.FR -> "🚶 Modérément actif (Activité quotidienne normale)"
            AppLanguage.EN -> "🚶 Moderately Active (Normal daily movement)"
        }
        ActivityLevel.VERY_ACTIVE -> when (lang) {
            AppLanguage.AR -> "⚡ نشيط جداً (عمل يدوي / حركة وتعب مستمر)"
            AppLanguage.FR -> "⚡ Très actif (Travail manuel / Effort continu)"
            AppLanguage.EN -> "⚡ Very Active (Manual labor / Heavy physical strain)"
        }
    }

    fun btnAnalyzeAndSave(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تحليل الجسم والحفظ ✨"
        AppLanguage.FR -> "Analyser & Enregistrer ✨"
        AppLanguage.EN -> "Analyze Body & Save ✨"
    }

    // Screen 4
    fun analysisTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "نتيجة التحليل ومستوى الجيم"
        AppLanguage.FR -> "Résultat de l'analyse & Niveau Gym"
        AppLanguage.EN -> "Analysis Result & Gym Level"
    }

    fun analyzingStatusText(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "جاري المعالجة..."
        AppLanguage.FR -> "Traitement..."
        AppLanguage.EN -> "Processing..."
    }

    fun bmiLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مؤشر كتلة الجسم (BMI)"
        AppLanguage.FR -> "Indice de Masse Corporelle (IMC)"
        AppLanguage.EN -> "Body Mass Index (BMI)"
    }

    fun tdeeLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "احتياج السعرات الحرارية اليومي (TDEE)"
        AppLanguage.FR -> "Besoins Caloriques Quotidiens (TDEE)"
        AppLanguage.EN -> "Daily Caloric Need (TDEE)"
    }

    fun healthTipHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "💡 نصيحة صحية مخصصة من الذكاء الاصطناعي"
        AppLanguage.FR -> "💡 Conseil Santé Personnalisé IA"
        AppLanguage.EN -> "💡 Personalized AI Health Tip"
    }

    fun gymLevelHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تحديد مستوى التمرين في الجيم (إجباري)"
        AppLanguage.FR -> "Définir le niveau d'entraînement (Obligatoire)"
        AppLanguage.EN -> "Select Gym Training Level (Required)"
    }

    fun gymLevelName(level: GymLevel, lang: AppLanguage): String = when (level) {
        GymLevel.BEGINNER -> when (lang) {
            AppLanguage.AR -> "🟢 مبتدئ (3 أيام - Full Body)"
            AppLanguage.FR -> "🟢 Débutant (3 jours - Corps complet)"
            AppLanguage.EN -> "🟢 Beginner (3 Days - Full Body)"
        }
        GymLevel.INTERMEDIATE -> when (lang) {
            AppLanguage.AR -> "🟡 متوسط (4 أيام - Upper / Lower)"
            AppLanguage.FR -> "🟡 Intermédiaire (4 jours - Haut / Bas)"
            AppLanguage.EN -> "🟡 Intermediate (4 Days - Upper / Lower)"
        }
        GymLevel.ADVANCED -> when (lang) {
            AppLanguage.AR -> "🔴 متقدم / قوي (5-6 أيام - Push / Pull / Legs)"
            AppLanguage.FR -> "🔴 Avancé / Fort (5-6 jours - Push / Pull / Legs)"
            AppLanguage.EN -> "🔴 Advanced / Pro (5-6 Days - Push / Pull / Legs)"
        }
    }

    fun btnCreateFullPlan(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إنشاء خطتي الكاملة 🔥"
        AppLanguage.FR -> "Créer Mon Plan Complet 🔥"
        AppLanguage.EN -> "Create My Full Plan 🔥"
    }

    // Dashboard
    fun tabExercises(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "التمارين 🏋️"
        AppLanguage.FR -> "Exercices 🏋️"
        AppLanguage.EN -> "Exercises 🏋️"
    }

    fun tabNutrition(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "التغذية 🥗"
        AppLanguage.FR -> "Nutrition 🥗"
        AppLanguage.EN -> "Nutrition 🥗"
    }

    fun editProfile(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تعديل الملف"
        AppLanguage.FR -> "Modifier profil"
        AppLanguage.EN -> "Edit Profile"
    }

    fun refreshPlan(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تحديث الخطة"
        AppLanguage.FR -> "Actualiser plan"
        AppLanguage.EN -> "Refresh Plan"
    }

    fun waterTrackerTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "💧 متابع شرب الماء اليومي"
        AppLanguage.FR -> "💧 Suivi de l'eau quotidienne"
        AppLanguage.EN -> "💧 Daily Water Tracker"
    }

    fun restDayTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "😴 يوم راحة واستشفاء عضلي"
        AppLanguage.FR -> "😴 Jour de repos & récupération"
        AppLanguage.EN -> "😴 Rest & Muscle Recovery Day"
    }

    fun restDayDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "استغل هذا اليوم للنوم الجيد، التغذية السليمة، وإعادة شحن طاقتك للأيام القادمة."
        AppLanguage.FR -> "Profitez de cette journée pour bien dormir, bien vous nourrir et recharger vos batteries."
        AppLanguage.EN -> "Use this day for good sleep, healthy nutrition, and recharging your energy for upcoming sessions."
    }

    fun caloriesUnit(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "سعرة حرارية"
        AppLanguage.FR -> "kcal"
        AppLanguage.EN -> "kcal"
    }

    fun tunisianIngredientsHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "🇹🇳 أطعمة ووجبات تونسية محلية"
        AppLanguage.FR -> "🇹🇳 Repas & Ingrédients Locaux Tunisiens"
        AppLanguage.EN -> "🇹🇳 Local Tunisian Foods & Ingredients"
    }

    fun youtubeSuggestionsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "▶️ تمارين جيم مقترحة للشرح من YouTube"
        AppLanguage.FR -> "▶️ Exercices Gym Suggérés sur YouTube"
        AppLanguage.EN -> "▶️ Suggested Gym Exercises on YouTube"
    }

    fun youtubeWatchBtn(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "شرح التمرين"
        AppLanguage.FR -> "Voir tuto"
        AppLanguage.EN -> "Watch Guide"
    }

    fun tutorialModalTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "🎥 شرح التمرين وتكنيك الأداء الصحيح"
        AppLanguage.FR -> "🎥 Guide d'Exécution & Vidéo"
        AppLanguage.EN -> "🎥 Exercise Form & Video Guide"
    }

    fun setupPositionLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "وضعية الجسم والتمركز"
        AppLanguage.FR -> "Position & Posture de Départ"
        AppLanguage.EN -> "Setup & Body Positioning"
    }

    fun gripLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "المسكة والقبضة"
        AppLanguage.FR -> "Prise en main & Alignement"
        AppLanguage.EN -> "Grip & Hand Placement"
    }

    fun executionLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "طريقة الأداء والمدى الحركي"
        AppLanguage.FR -> "Mouvement & Exécution"
        AppLanguage.EN -> "Execution & Range of Motion"
    }

    fun breathingSafetyLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "التنفس وتجنب الأخطاء"
        AppLanguage.FR -> "Respiration & Conseils de Sécurité"
        AppLanguage.EN -> "Breathing & Safety Tips"
    }

    fun closeTutorialBtn(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إغلاق والعودة للجدول"
        AppLanguage.FR -> "Fermer le tutoriel"
        AppLanguage.EN -> "Close & Return to Workout"
    }

    fun tdeeDialogTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "ما هو TDEE؟"
        AppLanguage.FR -> "Qu'est-ce que le TDEE ?"
        AppLanguage.EN -> "What is TDEE?"
    }

    fun tdeeDialogContent(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "هو إجمالي السعرات الحرارية التي يحرقها جسمك يومياً بناءً على بياناتك الشخصية ومستوى نشاطك. يساعدك هذا الرقم في معرفة كمية الطعام المناسبة لهدفك (خسارة أو زيادة الوزن)."
        AppLanguage.FR -> "C'est la dépense énergétique journalière totale de votre corps selon vos données personnelles et votre niveau d'activité. Ce chiffre vous aide à connaître la quantité de nourriture appropriée à votre objectif (perte ou prise de poids)."
        AppLanguage.EN -> "It is the total calories your body burns per day based on your personal metrics and activity level. This number helps you determine the right calorie intake for your goal (weight loss or muscle gain)."
    }

    fun bmiDialogTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "ما هو BMI؟"
        AppLanguage.FR -> "Qu'est-ce que le BMI (IMC) ?"
        AppLanguage.EN -> "What is BMI?"
    }

    fun bmiDialogContent(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "هو مؤشر كتلة الجسم، وهو قياس يعتمد على وزنك وطولك لمعرفة ما إذا كان وزنك مثالياً أو يحتاج لتعديل."
        AppLanguage.FR -> "C'est l'Indice de Masse Corporelle, une mesure basée sur votre poids et votre taille pour évaluer si votre corpulence est idéale ou nécessite un ajustement."
        AppLanguage.EN -> "It is the Body Mass Index, a measurement based on your weight and height to determine if your weight is in a healthy range or needs adjustment."
    }

    fun closeBtn(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إغلاق"
        AppLanguage.FR -> "Fermer"
        AppLanguage.EN -> "Close"
    }

    fun darkModeLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الوضع الليلي"
        AppLanguage.FR -> "Mode Sombre"
        AppLanguage.EN -> "Dark Mode"
    }

    fun lightModeLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الوضع النهاري"
        AppLanguage.FR -> "Mode Clair"
        AppLanguage.EN -> "Light Mode"
    }

    fun toggleThemeLabel(lang: AppLanguage, isDark: Boolean): String = if (isDark) lightModeLabel(lang) else darkModeLabel(lang)
}
