package com.example.localization

import com.example.data.model.ActivityLevel
import com.example.data.model.AppLanguage
import com.example.data.model.Gender
import com.example.data.model.GymLevel
import com.example.data.model.Goal

data class LocalizedYouTubeExercise(
    val title: String,
    val subtitle: String,
    val videoId: String
)

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

    // Screen 2 - Personal Data
    fun personalDataTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "البيانات الشخصية"
        AppLanguage.FR -> "Données Personnelles"
        AppLanguage.EN -> "Personal Data"
    }

    fun step1Subtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "خطوة 1 من 3 - جميع الحقول إجبارية لحساب السعرات والتمارين بدقة"
        AppLanguage.FR -> "Étape 1 sur 3 - Tous les champs sont obligatoires pour un calcul précis"
        AppLanguage.EN -> "Step 1 of 3 - All fields required for accurate calories & workout calculation"
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
        AppLanguage.AR -> "العمر (18 - 100 سنة) *"
        AppLanguage.FR -> "Âge (18 - 100 ans) *"
        AppLanguage.EN -> "Age (18 - 100 years) *"
    }

    fun agePlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مثال: 25"
        AppLanguage.FR -> "ex. 25"
        AppLanguage.EN -> "e.g., 25"
    }

    fun heightLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الطول (145 - 205 سم) *"
        AppLanguage.FR -> "Taille (145 - 205 cm) *"
        AppLanguage.EN -> "Height (145 - 205 cm) *"
    }

    fun heightPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مثال: 175"
        AppLanguage.FR -> "ex. 175"
        AppLanguage.EN -> "e.g., 175"
    }

    fun weightLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الوزن الحالي (50 - 120 كغ) *"
        AppLanguage.FR -> "Poids actuel (50 - 120 kg) *"
        AppLanguage.EN -> "Current Weight (50 - 120 kg) *"
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

    // Validation Error Messages
    fun ageRangeError(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "العمر يجب أن يكون بين 18 و 100 سنة"
        AppLanguage.FR -> "L'âge doit être compris entre 18 et 100 ans"
        AppLanguage.EN -> "Age must be between 18 and 100 years"
    }

    fun heightRangeError(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الطول يجب أن يكون بين 145 و 205 سم"
        AppLanguage.FR -> "La taille doit être comprise entre 145 et 205 cm"
        AppLanguage.EN -> "Height must be between 145 and 205 cm"
    }

    fun weightRangeError(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الوزن يجب أن يكون بين 50 و 120 كغ"
        AppLanguage.FR -> "Le poids doit être compris entre 50 et 120 kg"
        AppLanguage.EN -> "Weight must be between 50 and 120 kg"
    }

    fun fieldRequiredError(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "هذا الحقل مطلوب"
        AppLanguage.FR -> "Ce champ est obligatoire"
        AppLanguage.EN -> "This field is required"
    }

    fun selectGenderNotice(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "يرجى تحديد الجنس"
        AppLanguage.FR -> "Veuillez choisir le genre"
        AppLanguage.EN -> "Please select gender"
    }

    fun btnNext(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "التالي ➡️"
        AppLanguage.FR -> "Suivant ➡️"
        AppLanguage.EN -> "Next ➡️"
    }

    fun fillAllDataNotice(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "⚠️ يرجى تصحيح الحقول المحددة واستيفاء الشروط للمتابعة"
        AppLanguage.FR -> "⚠️ Veuillez remplir tous les champs selon les critères requis"
        AppLanguage.EN -> "⚠️ Please ensure all fields meet the required ranges to continue"
    }

    // Screen 3 - Goal & Lifestyle
    fun goalLifestyleTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الهدف ومستوى النشاط"
        AppLanguage.FR -> "Objectif & Style de vie"
        AppLanguage.EN -> "Goal & Lifestyle"
    }

    fun step2Subtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "خطوة 2 من 3 - حدد هدفك ونمط حياتك لتخصيص جدولك"
        AppLanguage.FR -> "Étape 2 sur 3 - Définissez votre objectif et niveau d'activité"
        AppLanguage.EN -> "Step 2 of 3 - Define your primary goal and daily activity level"
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

    // Screen 4 - Analysis & Gym Level
    fun analysisTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "نتيجة التحليل ومستوى الجيم"
        AppLanguage.FR -> "Résultat de l'analyse & Niveau Gym"
        AppLanguage.EN -> "Analysis Result & Gym Level"
    }

    fun step3Subtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "خطوة 3 من 3 - نتائج تحليلك من الذكاء الاصطناعي واختيار جدول التمارين"
        AppLanguage.FR -> "Étape 3 sur 3 - Résultats de votre analyse IA et choix du programme"
        AppLanguage.EN -> "Step 3 of 3 - AI body analysis results and training routine selection"
    }

    fun analyzingStatusText(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "جاري المعالجة والتحليل..."
        AppLanguage.FR -> "Traitement et analyse en cours..."
        AppLanguage.EN -> "Processing & analyzing body metrics..."
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
            AppLanguage.FR -> "🟢 Débutant (3 jours - Full Body)"
            AppLanguage.EN -> "🟢 Beginner (3 Days - Full Body)"
        }
        GymLevel.INTERMEDIATE -> when (lang) {
            AppLanguage.AR -> "🟡 متوسط (4 أيام - Upper / Lower)"
            AppLanguage.FR -> "🟡 Intermédiaire (4 jours - Upper / Lower)"
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
    fun dayMonday(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الإثنين"
        AppLanguage.FR -> "Lundi"
        AppLanguage.EN -> "Monday"
    }

    fun dayTuesday(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الثلاثاء"
        AppLanguage.FR -> "Mardi"
        AppLanguage.EN -> "Tuesday"
    }

    fun dayWednesday(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الأربعاء"
        AppLanguage.FR -> "Mercredi"
        AppLanguage.EN -> "Wednesday"
    }

    fun dayThursday(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الخميس"
        AppLanguage.FR -> "Jeudi"
        AppLanguage.EN -> "Thursday"
    }

    fun dayFriday(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الجمعة"
        AppLanguage.FR -> "Vendredi"
        AppLanguage.EN -> "Friday"
    }

    fun daySaturday(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "السبت"
        AppLanguage.FR -> "Samedi"
        AppLanguage.EN -> "Saturday"
    }

    fun daySunday(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الأحد"
        AppLanguage.FR -> "Dimanche"
        AppLanguage.EN -> "Sunday"
    }

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

    fun dailyNutritionGoals(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "🎯 الأهداف الغذائية اليومية"
        AppLanguage.FR -> "🎯 Objectifs Nutritionnels Quotidiens"
        AppLanguage.EN -> "🎯 Daily Nutrition Targets"
    }

    fun macroProtein(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "بروتين"
        AppLanguage.FR -> "Protéines"
        AppLanguage.EN -> "Protein"
    }

    fun macroCarbs(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "كاربوهيدرات"
        AppLanguage.FR -> "Glucides"
        AppLanguage.EN -> "Carbs"
    }

    fun macroFats(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "دهون صحية"
        AppLanguage.FR -> "Lipides"
        AppLanguage.EN -> "Healthy Fats"
    }

    fun waterTrackerTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "💧 متابع شرب الماء اليومي"
        AppLanguage.FR -> "💧 Suivi de l'eau quotidienne"
        AppLanguage.EN -> "💧 Daily Water Tracker"
    }

    fun waterGlassesStatus(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "$count / 10 أكواب اليوم"
        AppLanguage.FR -> "$count / 10 verres aujourd'hui"
        AppLanguage.EN -> "$count / 10 glasses today"
    }

    fun setsLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مجموعات"
        AppLanguage.FR -> "séries"
        AppLanguage.EN -> "sets"
    }

    fun repsLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تكرار"
        AppLanguage.FR -> "réps"
        AppLanguage.EN -> "reps"
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
        AppLanguage.EN -> "🇹🇳 Local Tunisian Foods & Meals"
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

    fun youtubeExercisesList(lang: AppLanguage): List<LocalizedYouTubeExercise> = when (lang) {
        AppLanguage.FR -> listOf(
            LocalizedYouTubeExercise("🏋️ Développé Couché (Bench)", "Exécution pour pectoraux et force", "rT7DGvm-3yy"),
            LocalizedYouTubeExercise("🦵 Squat à la Barre", "Technique complète du squat et posture", "ultWZbUMPL8"),
            LocalizedYouTubeExercise("🏋️‍♂️ Soulevé de Terre (Deadlift)", "Exécution sécurisée dos et fessiers", "op9kVnSso6Q"),
            LocalizedYouTubeExercise("💪 Tirage Poitrine (Lat Pulldown)", "Isolation du grand dorsal sans tricher", "CAwf7n6Luuc"),
            LocalizedYouTubeExercise("🎯 Développé Militaire Épaules", "Renforcement des deltoïdes", "qEwKCR5JCog"),
            LocalizedYouTubeExercise("⚡ Biceps & Triceps", "Technique bras pour volume optimal", "ykJmrZ5v0Oo")
        )
        AppLanguage.EN -> listOf(
            LocalizedYouTubeExercise("🏋️ Barbell Bench Press", "Proper form for chest strength & hypertrophy", "rT7DGvm-3yy"),
            LocalizedYouTubeExercise("🦵 Barbell Back Squat", "Full squat depth and knee alignment guide", "ultWZbUMPL8"),
            LocalizedYouTubeExercise("🏋️‍♂️ Conventional Deadlift", "Back safety and hip drive mechanics", "op9kVnSso6Q"),
            LocalizedYouTubeExercise("💪 Wide-Grip Lat Pulldown", "Lats engagement & elbow drive tutorial", "CAwf7n6Luuc"),
            LocalizedYouTubeExercise("🎯 Overhead Shoulder Press", "Strict pressing form & shoulder stability", "qEwKCR5JCog"),
            LocalizedYouTubeExercise("⚡ Arms (Biceps & Triceps)", "Isolation technique for arm growth", "ykJmrZ5v0Oo")
        )
        AppLanguage.AR -> listOf(
            LocalizedYouTubeExercise("🏋️ Bench Press (بنش بريس)", "شرح الأداء الصحيح للصدر الأوسط والأسفل", "rT7DGvm-3yy"),
            LocalizedYouTubeExercise("🦵 Barbell Squats (السكوات)", "طريقة أداء السكوات بالبار وحماية الركبتين", "ultWZbUMPL8"),
            LocalizedYouTubeExercise("🏋️‍♂️ Deadlift (الرفعة المميتة)", "شرح طريقة الديدلفت للظهر والظهر السفلي", "op9kVnSso6Q"),
            LocalizedYouTubeExercise("💪 Lat Pulldown (سحب الظهر)", "تمرين استهداف الظهر العريض بأمان", "CAwf7n6Luuc"),
            LocalizedYouTubeExercise("🎯 Overhead Shoulder Press", "شرح تمرين ضغط الأكتاف بالبار أو الدامبلز", "qEwKCR5JCog"),
            LocalizedYouTubeExercise("⚡ Biceps & Triceps Workout", "تمارين تضخيم عضلات البايسبس والترايسبس", "ykJmrZ5v0Oo")
        )
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

    fun openInYoutube(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "فتح في تطبيق YouTube ↗"
        AppLanguage.FR -> "Ouvrir dans YouTube ↗"
        AppLanguage.EN -> "Open in YouTube App ↗"
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

    // Custom Food & Calorie Calculator Tab & Screen
    fun tabCustomCalculator(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "حاسبة الوجبات 🧮"
        AppLanguage.FR -> "Calculateur 🧮"
        AppLanguage.EN -> "Meal Calc 🧮"
    }

    fun customCalculatorTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "حاسبة السعرات والماكروز للوجبات"
        AppLanguage.FR -> "Calculateur de Calories & Macros"
        AppLanguage.EN -> "Meal Calorie & Macro Calculator"
    }

    fun customCalculatorSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "اختر الأطعمة الطبيعية وحدد الكمية (غرام أو مل) لحساب السعرات والماكروز فورياً"
        AppLanguage.FR -> "Sélectionnez vos aliments naturels et ajustez la quantité (g ou ml) en direct"
        AppLanguage.EN -> "Select whole foods and adjust quantity (g or ml) for real-time aggregation"
    }

    fun categoryAll(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الكل"
        AppLanguage.FR -> "Tous"
        AppLanguage.EN -> "All"
    }

    fun categoryVegetables(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الخضروات بأنواعها 🥬"
        AppLanguage.FR -> "Légumes variés 🥬"
        AppLanguage.EN -> "Vegetables 🥬"
    }

    fun categoryFruits(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الفواكه بأنواعها 🍎"
        AppLanguage.FR -> "Fruits variés 🍎"
        AppLanguage.EN -> "Fruits 🍎"
    }

    fun categoryMeatPoultryFish(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "اللحوم، الدواجن والأسماك 🍗🐟"
        AppLanguage.FR -> "Viandes, Volailles & Poissons 🍗🐟"
        AppLanguage.EN -> "Meats, Poultry & Fish 🍗🐟"
    }

    fun categoryLegumesGrains(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "البقوليات والحبوب 🌾"
        AppLanguage.FR -> "Légumineuses & Céréales 🌾"
        AppLanguage.EN -> "Legumes & Grains 🌾"
    }

    fun categoryDairy(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الحليب ومشتقاته 🥛🧀"
        AppLanguage.FR -> "Lait & Produits Laitiers 🥛🧀"
        AppLanguage.EN -> "Dairy & Dairy Products 🥛🧀"
    }

    fun categoryNutsDriedFruits(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "المكسرات والفواكه الجافة 🥜🍇"
        AppLanguage.FR -> "Noix & Fruits Secs 🥜🍇"
        AppLanguage.EN -> "Nuts & Dried Fruits 🥜🍇"
    }

    fun categoryOilsHealthyFats(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الزيوت والدهون الصحية 🫒🥑"
        AppLanguage.FR -> "Huiles & Graisses Saines 🫒🥑"
        AppLanguage.EN -> "Oils & Healthy Fats 🫒🥑"
    }

    fun myMealPlate(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "🍽️ مكونات وجبتك المختارة"
        AppLanguage.FR -> "🍽️ Ingrédients de Votre Repas"
        AppLanguage.EN -> "🍽️ Selected Meal Items"
    }

    fun noFoodSelected(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "لم تختر أي مكونات بعد. انقر على أي طعام أعلاه لإضافته وتحديد الكمية."
        AppLanguage.FR -> "Aucun ingrédient sélectionné. Cliquez sur un aliment ci-dessus pour l'ajouter."
        AppLanguage.EN -> "No items selected yet. Click any whole food above to set quantity and add to meal."
    }

    fun gramsUnit(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "غرام"
        AppLanguage.FR -> "g"
        AppLanguage.EN -> "g"
    }

    fun mlUnit(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مل"
        AppLanguage.FR -> "ml"
        AppLanguage.EN -> "ml"
    }

    fun adjustQuantity(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تحديد الكمية"
        AppLanguage.FR -> "Ajuster la quantité"
        AppLanguage.EN -> "Set Quantity"
    }

    fun clearPlate(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مسح الكل"
        AppLanguage.FR -> "Effacer tout"
        AppLanguage.EN -> "Clear All"
    }

    fun mealSummaryTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "📊 ملخص الوجبة (Meal Summary)"
        AppLanguage.FR -> "📊 Résumé du Repas (Meal Summary)"
        AppLanguage.EN -> "📊 Meal Summary"
    }

    fun totalCaloriesLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إجمالي السعرات الحرارية"
        AppLanguage.FR -> "Calories Totales"
        AppLanguage.EN -> "Total Calories"
    }

    fun totalMealCalories(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "السعرات الإجمالية للوجبة"
        AppLanguage.FR -> "Calories Totales du Repas"
        AppLanguage.EN -> "Total Meal Calories"
    }

    fun macroBreakdown(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "البروتين، الكربوهيدرات، والدهون (Macros)"
        AppLanguage.FR -> "Protéines, Glucides et Lipides"
        AppLanguage.EN -> "Protein, Carbs & Fats"
    }

    fun foodSummaryHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "📊 ملخص الوجبة الشامل"
        AppLanguage.FR -> "📊 Résumé Complet du Repas"
        AppLanguage.EN -> "📊 Comprehensive Meal Summary"
    }

    fun per100gLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "لكل 100غ / 100مل:"
        AppLanguage.FR -> "Pour 100g / 100ml :"
        AppLanguage.EN -> "Per 100g / 100ml:"
    }

    fun tapToAddToPlate(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إضافة للوجبة +"
        AppLanguage.FR -> "Ajouter au repas +"
        AppLanguage.EN -> "Add to Meal +"
    }

    fun searchFoodPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "ابحث عن أي طعام طبيعي..."
        AppLanguage.FR -> "Rechercher un aliment..."
        AppLanguage.EN -> "Search whole foods..."
    }

    fun totalWeightSelected(totalGrams: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إجمالي وزن وحجم الوجبة: $totalGrams وحدة"
        AppLanguage.FR -> "Poids/Volume total : $totalGrams"
        AppLanguage.EN -> "Total meal quantity: $totalGrams"
    }
}
