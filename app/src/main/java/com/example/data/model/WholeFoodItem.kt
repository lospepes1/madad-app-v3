package com.example.data.model

enum class FoodOrigin {
    PLANT,
    ANIMAL
}

enum class FoodCategory {
    ALL,
    VEGETABLES,
    FRUITS,
    MEAT_POULTRY,
    FISH_SEAFOOD,
    GRAINS_LEGUMES,
    DAIRY_EGGS,
    HEALTHY_FATS_NUTS
}

data class WholeFoodItem(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val nameFr: String,
    val origin: FoodOrigin,
    val category: FoodCategory,
    val caloriesPer100g: Int,
    val proteinPer100g: Float,
    val carbsPer100g: Float,
    val fatPer100g: Float,
    val vitaminsAndMineralsAr: String,
    val vitaminsAndMineralsEn: String,
    val vitaminsAndMineralsFr: String,
    val healthBenefitAr: String,
    val healthBenefitEn: String,
    val healthBenefitFr: String,
    val emoji: String,
    val defaultServingGrams: Int = 100
) {
    fun localizedName(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> nameAr
        AppLanguage.FR -> nameFr
        AppLanguage.EN -> nameEn
    }

    fun localizedVitamins(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> vitaminsAndMineralsAr
        AppLanguage.FR -> vitaminsAndMineralsFr
        AppLanguage.EN -> vitaminsAndMineralsEn
    }

    fun localizedBenefit(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> healthBenefitAr
        AppLanguage.FR -> healthBenefitFr
        AppLanguage.EN -> healthBenefitEn
    }
}

data class SelectedFoodItem(
    val foodItem: WholeFoodItem,
    val quantityGrams: Int
) {
    val calories: Int
        get() = ((foodItem.caloriesPer100g * quantityGrams) / 100f).toInt()

    val protein: Float
        get() = (foodItem.proteinPer100g * quantityGrams) / 100f

    val carbs: Float
        get() = (foodItem.carbsPer100g * quantityGrams) / 100f

    val fat: Float
        get() = (foodItem.fatPer100g * quantityGrams) / 100f
}

data class AiMealCalculationResult(
    val totalCalories: Int,
    val totalProteinGrams: Float,
    val totalCarbsGrams: Float,
    val totalFatGrams: Float,
    val proteinPercentage: Int,
    val carbsPercentage: Int,
    val fatPercentage: Int,
    val glycemicEstimate: String,
    val bestTiming: String,
    val aiInsight: String,
    val isAiGenerated: Boolean = true
)

object WholeFoodsRepository {
    val foods: List<WholeFoodItem> = listOf(
        // ANIMAL BASED
        WholeFoodItem(
            id = "chicken_breast",
            nameAr = "صدر دجاج طازج",
            nameEn = "Fresh Chicken Breast",
            nameFr = "Blanc de Poulet Frais",
            origin = FoodOrigin.ANIMAL,
            category = FoodCategory.MEAT_POULTRY,
            caloriesPer100g = 165,
            proteinPer100g = 31.0f,
            carbsPer100g = 0.0f,
            fatPer100g = 3.6f,
            vitaminsAndMineralsAr = "فيتامين B6, B12, نياسين, فسفور, زنك, سيلينيوم",
            vitaminsAndMineralsEn = "Vitamin B6, B12, Niacin, Phosphorus, Zinc, Selenium",
            vitaminsAndMineralsFr = "Vitamine B6, B12, Niacine, Phosphore, Zinc, Sélénium",
            healthBenefitAr = "أعلى مصدر بروتين نقي لبناء وإصلاح الأنسجة العضلية بدون دهون زائدة.",
            healthBenefitEn = "High-purity protein source for lean muscle growth and recovery.",
            healthBenefitFr = "Source de protéines pures pour le développement musculaire sans graisse.",
            emoji = "🍗",
            defaultServingGrams = 150
        ),
        WholeFoodItem(
            id = "lean_beef",
            nameAr = "لحم بقري هبرة (صافي)",
            nameEn = "Lean Beef Steak",
            nameFr = "Bifteck de Bœuf Maigre",
            origin = FoodOrigin.ANIMAL,
            category = FoodCategory.MEAT_POULTRY,
            caloriesPer100g = 215,
            proteinPer100g = 26.5f,
            carbsPer100g = 0.0f,
            fatPer100g = 11.8f,
            vitaminsAndMineralsAr = "حديد هيمي سريع الامتصاص, كرياتين طبيعي, زنك, B12",
            vitaminsAndMineralsEn = "Heme Iron, Natural Creatine, Zinc, Vitamin B12",
            vitaminsAndMineralsFr = "Fer héminique, Créatine naturelle, Zinc, Vitamine B12",
            healthBenefitAr = "يعزز قوة رفع الأوزان ومستويات الهيموجلوبين والطاقة البدنية.",
            healthBenefitEn = "Boosts strength, hemoglobin levels, and muscular endurance.",
            healthBenefitFr = "Améliore la force physique, le taux d'hémoglobine et l'endurance.",
            emoji = "🥩",
            defaultServingGrams = 150
        ),
        WholeFoodItem(
            id = "fresh_salmon",
            nameAr = "سمك السلمون الطازج",
            nameEn = "Wild Salmon Fillet",
            nameFr = "Filet de Saumon Frais",
            origin = FoodOrigin.ANIMAL,
            category = FoodCategory.FISH_SEAFOOD,
            caloriesPer100g = 208,
            proteinPer100g = 22.0f,
            carbsPer100g = 0.0f,
            fatPer100g = 13.0f,
            vitaminsAndMineralsAr = "أوميغا-3 (EPA/DHA), فيتامين D, B12, بوتاسيوم",
            vitaminsAndMineralsEn = "Omega-3 (EPA/DHA), Vitamin D, Vitamin B12, Potassium",
            vitaminsAndMineralsFr = "Oméga-3 (EPA/DHA), Vitamine D, B12, Potassium",
            healthBenefitAr = "مضاد قوي لالتهابات المفاصل ويعزز صحة القلب والشرايين والدماغ.",
            healthBenefitEn = "Potent anti-inflammatory for joints, brain, and cardiovascular health.",
            healthBenefitFr = "Anti-inflammatoire puissant pour les articulations, le cœur et le cerveau.",
            emoji = "🐟",
            defaultServingGrams = 150
        ),
        WholeFoodItem(
            id = "tuna_fillet",
            nameAr = "تونة طازجة / طبيعية بالماء",
            nameEn = "Fresh / Natural Tuna",
            nameFr = "Thon Frais / Naturel",
            origin = FoodOrigin.ANIMAL,
            category = FoodCategory.FISH_SEAFOOD,
            caloriesPer100g = 130,
            proteinPer100g = 28.0f,
            carbsPer100g = 0.0f,
            fatPer100g = 1.0f,
            vitaminsAndMineralsAr = "سيلينيوم, فيتامين B3, B6, B12, أوميغا 3",
            vitaminsAndMineralsEn = "Selenium, Vitamin B3, B6, B12, Omega-3",
            vitaminsAndMineralsFr = "Sélénium, Vitamines B3, B6, B12, Oméga 3",
            healthBenefitAr = "وجبة تنشيف مثالية عالية البروتين وخالية تماماً من الكربوهيدرات والدهون.",
            healthBenefitEn = "Ultimate cutting protein source with virtually zero carbs and fats.",
            healthBenefitFr = "Protéine idéale pour la sèche, sans glucides et ultra faible en lipides.",
            emoji = "🐠",
            defaultServingGrams = 120
        ),
        WholeFoodItem(
            id = "whole_eggs",
            nameAr = "بيض دجاج كامل طبيعي",
            nameEn = "Whole Natural Eggs",
            nameFr = "Œufs Entiers Naturels",
            origin = FoodOrigin.ANIMAL,
            category = FoodCategory.DAIRY_EGGS,
            caloriesPer100g = 143,
            proteinPer100g = 12.6f,
            carbsPer100g = 0.8f,
            fatPer100g = 9.9f,
            vitaminsAndMineralsAr = "كولين (Choline), لوتين, فيتامين A, D, E, B12, حديد",
            vitaminsAndMineralsEn = "Choline, Lutein, Vitamin A, D, E, B12, Iron",
            vitaminsAndMineralsFr = "Choline, Lutéine, Vitamines A, D, E, B12, Fer",
            healthBenefitAr = "أعلى قيمة بيولوجية للبروتين وتوفر الكولين لتعزيز وظائف الدماغ والأعصاب.",
            healthBenefitEn = "Gold standard protein bioavailability and brain-boosting choline.",
            healthBenefitFr = "Meilleure valeur biologique protéique et choline pour le cerveau.",
            emoji = "🥚",
            defaultServingGrams = 100
        ),
        WholeFoodItem(
            id = "ricotta_cheese",
            nameAr = "جبن ريكوتا طبيعي (Ricotta)",
            nameEn = "Natural Ricotta Cheese",
            nameFr = "Fromage Ricotta Naturel",
            origin = FoodOrigin.ANIMAL,
            category = FoodCategory.DAIRY_EGGS,
            caloriesPer100g = 138,
            proteinPer100g = 11.4f,
            carbsPer100g = 3.0f,
            fatPer100g = 9.0f,
            vitaminsAndMineralsAr = "كالسيوم عالي, فسفور, فيتامين A, B2, سيلينيوم",
            vitaminsAndMineralsEn = "High Calcium, Phosphorus, Vitamin A, B2, Selenium",
            vitaminsAndMineralsFr = "Calcium élevé, Phosphore, Vitamines A, B2, Sélénium",
            healthBenefitAr = "غني ببروتين مصل اللبن (Whey) الطبيعي سريع الامتصاص لصحة العظام والعضلات.",
            healthBenefitEn = "Rich in natural whey protein for bone density and rapid muscle synthesis.",
            healthBenefitFr = "Riche en protéines de lactosérum naturel pour la densité osseuse.",
            emoji = "🧀",
            defaultServingGrams = 100
        ),
        WholeFoodItem(
            id = "greek_yogurt",
            nameAr = "زبادي يوناني طبيعي",
            nameEn = "Plain Greek Yogurt",
            nameFr = "Yaourt Grec Nature",
            origin = FoodOrigin.ANIMAL,
            category = FoodCategory.DAIRY_EGGS,
            caloriesPer100g = 97,
            proteinPer100g = 10.0f,
            carbsPer100g = 3.6f,
            fatPer100g = 5.0f,
            vitaminsAndMineralsAr = "بروبيوتيك (بكتيريا نافعة), كالسيوم, B12, بوتاسيوم",
            vitaminsAndMineralsEn = "Probiotics, Calcium, Vitamin B12, Potassium",
            vitaminsAndMineralsFr = "Probiotiques, Calcium, Vitamine B12, Potassium",
            healthBenefitAr = "يحسن صحة الجهاز الهضمي والامتصاص ويوفر بروتين بطيء الامتصاص (كازين).",
            healthBenefitEn = "Boosts gut microbiome and delivers sustained casein protein.",
            healthBenefitFr = "Favorise la flore intestinale et apporte de la caséine à digestion lente.",
            emoji = "🥣",
            defaultServingGrams = 150
        ),
        WholeFoodItem(
            id = "sardines",
            nameAr = "سردين طازج مشوي",
            nameEn = "Fresh Grilled Sardines",
            nameFr = "Sardines Fraîches Grillées",
            origin = FoodOrigin.ANIMAL,
            category = FoodCategory.FISH_SEAFOOD,
            caloriesPer100g = 208,
            proteinPer100g = 24.6f,
            carbsPer100g = 0.0f,
            fatPer100g = 11.5f,
            vitaminsAndMineralsAr = "كالسيوم مركز, فيتامين D, أوميغا-3, حديد, يود",
            vitaminsAndMineralsEn = "Concentrated Calcium, Vitamin D, Omega-3, Iron, Iodine",
            vitaminsAndMineralsFr = "Calcium concentré, Vitamine D, Oméga-3, Fer, Iode",
            healthBenefitAr = "يقوي المفاصل وكثافة العظام بفضل الكالسيوم الطبيعي وفيتامين D.",
            healthBenefitEn = "Strengthens bones and joints with natural calcium and vitamin D.",
            healthBenefitFr = "Renforce les os et articulations grâce au calcium naturel et vitamine D.",
            emoji = "🐟",
            defaultServingGrams = 120
        ),

        // PLANT BASED
        WholeFoodItem(
            id = "oats_whole",
            nameAr = "شوفان حبوب كاملة / بسيسة",
            nameEn = "Whole Rolled Oats / Bsissa",
            nameFr = "Flocons d'Avoine / Bsissa",
            origin = FoodOrigin.PLANT,
            category = FoodCategory.GRAINS_LEGUMES,
            caloriesPer100g = 389,
            proteinPer100g = 16.9f,
            carbsPer100g = 66.3f,
            fatPer100g = 6.9f,
            vitaminsAndMineralsAr = "بيتا جلوكان (ألياف قابلة للذوبان), مغنيسيوم, حديد, زنك, B1",
            vitaminsAndMineralsEn = "Beta-Glucan Fiber, Magnesium, Iron, Zinc, Vitamin B1",
            vitaminsAndMineralsFr = "Fibres Bêta-glucanes, Magnésium, Fer, Zinc, Vitamine B1",
            healthBenefitAr = "طاقة مستدامة طويلة المدى قبل التمارين وتخفيض الكوليسترول الضار.",
            healthBenefitEn = "Sustained workout energy and cholesterol reduction via beta-glucan.",
            healthBenefitFr = "Énergie durable avant l'entraînement et régulation du cholestérol.",
            emoji = "🌾",
            defaultServingGrams = 60
        ),
        WholeFoodItem(
            id = "lentils_brown",
            nameAr = "عدس طبيعي بني / أحمر",
            nameEn = "Natural Cooked Lentils",
            nameFr = "Lentilles Cuites Naturelles",
            origin = FoodOrigin.PLANT,
            category = FoodCategory.GRAINS_LEGUMES,
            caloriesPer100g = 116,
            proteinPer100g = 9.0f,
            carbsPer100g = 20.1f,
            fatPer100g = 0.4f,
            vitaminsAndMineralsAr = "فولات (حمض الفوليك), حديد نباتي, مغنيسيوم, بوتاسيوم",
            vitaminsAndMineralsEn = "Folate, Plant-based Iron, Magnesium, Potassium",
            vitaminsAndMineralsFr = "Folate, Fer végétal, Magnésium, Potassium",
            healthBenefitAr = "غني بالألياف والبروتين النباتي المشبع الذي يحافظ على استقرار سكر الدم.",
            healthBenefitEn = "Rich in plant protein and dietary fiber for stable blood sugar levels.",
            healthBenefitFr = "Riche en protéines végétales et fibres pour stabiliser la glycémie.",
            emoji = "🍲",
            defaultServingGrams = 150
        ),
        WholeFoodItem(
            id = "chickpeas",
            nameAr = "حمص مسلوق طبيعي",
            nameEn = "Boiled Chickpeas",
            nameFr = "Pois Chiches Bouillis",
            origin = FoodOrigin.PLANT,
            category = FoodCategory.GRAINS_LEGUMES,
            caloriesPer100g = 164,
            proteinPer100g = 8.9f,
            carbsPer100g = 27.4f,
            fatPer100g = 2.6f,
            vitaminsAndMineralsAr = "منغنيز, نحاس, فولات, فسفور, ألياف عالية",
            vitaminsAndMineralsEn = "Manganese, Copper, Folate, Phosphorus, High Fiber",
            vitaminsAndMineralsFr = "Manganèse, Cuivre, Folate, Phosphore, Fibres",
            healthBenefitAr = "يمنح شبعاً طويلاً ويدعم صحة البكتيريا المعوية وعمليات الأيض.",
            healthBenefitEn = "Promotes long satiety and supports gut microbiome and metabolism.",
            healthBenefitFr = "Procure une satiété prolongée et soutient le métabolisme.",
            emoji = "🧆",
            defaultServingGrams = 150
        ),
        WholeFoodItem(
            id = "fresh_spinach",
            nameAr = "سبانخ طازجة خضراء",
            nameEn = "Fresh Raw Spinach",
            nameFr = "Épinards Frais",
            origin = FoodOrigin.PLANT,
            category = FoodCategory.VEGETABLES,
            caloriesPer100g = 23,
            proteinPer100g = 2.9f,
            carbsPer100g = 3.6f,
            fatPer100g = 0.4f,
            vitaminsAndMineralsAr = "فيتامين K (480% RDA), فيتامين A, فيتامين C, حديد, نترات طبيعية",
            vitaminsAndMineralsEn = "Vitamin K, Vitamin A, Vitamin C, Iron, Dietary Nitrates",
            vitaminsAndMineralsFr = "Vitamine K, Vitamine A, Vitamine C, Fer, Nitrates naturels",
            healthBenefitAr = "النترات الطبيعية تزيد تدفق الدم وتوسيع الأوعية الدموية أثناء التمارين (Pump).",
            healthBenefitEn = "Natural nitrates expand blood vessels and boost muscular pumps.",
            healthBenefitFr = "Les nitrates naturels améliorent la congestion musculaire à l'entraînement.",
            emoji = "🥬",
            defaultServingGrams = 100
        ),
        WholeFoodItem(
            id = "sweet_potato",
            nameAr = "بطاطا حلوة مشوية",
            nameEn = "Baked Sweet Potato",
            nameFr = "Patate Douce Rôtie",
            origin = FoodOrigin.PLANT,
            category = FoodCategory.VEGETABLES,
            caloriesPer100g = 86,
            proteinPer100g = 1.6f,
            carbsPer100g = 20.1f,
            fatPer100g = 0.1f,
            vitaminsAndMineralsAr = "بيتا كاروتين (Vit A), فيتامين C, بوتاسيوم, منغنيز",
            vitaminsAndMineralsEn = "Beta-Carotene (Vit A), Vitamin C, Potassium, Manganese",
            vitaminsAndMineralsFr = "Bêta-carotène (Vit A), Vitamine C, Potassium, Manganèse",
            healthBenefitAr = "أفضل مصدر كاربوهيدرات معقدة لإعادة تعبئة الجليكوجين العضلي.",
            healthBenefitEn = "Premium complex carb for rapid muscle glycogen replenishment.",
            healthBenefitFr = "Glucide complexe idéal pour recharger le glycogène musculaire.",
            emoji = "🍠",
            defaultServingGrams = 200
        ),
        WholeFoodItem(
            id = "fresh_broccoli",
            nameAr = "بروكلي طازج على البخار",
            nameEn = "Steamed Fresh Broccoli",
            nameFr = "Brocoli Frais à la Vapeur",
            origin = FoodOrigin.PLANT,
            category = FoodCategory.VEGETABLES,
            caloriesPer100g = 34,
            proteinPer100g = 2.8f,
            carbsPer100g = 6.6f,
            fatPer100g = 0.4f,
            vitaminsAndMineralsAr = "فيتامين C (110% RDA), فيتامين K, سولفورافان (Sulforaphane)",
            vitaminsAndMineralsEn = "Vitamin C (110% RDA), Vitamin K, Sulforaphane",
            vitaminsAndMineralsFr = "Vitamine C, Vitamine K, Sulforaphane anti-oxydant",
            healthBenefitAr = "مضاد أكسدة استثنائي يقلل هرمون الإستروجين الضار ويعزز المناعة.",
            healthBenefitEn = "Powerful antioxidant compound supporting hormonal balance and immunity.",
            healthBenefitFr = "Antioxydant puissant qui soutient l'immunité et l'équilibre hormonal.",
            emoji = "🥦",
            defaultServingGrams = 150
        ),
        WholeFoodItem(
            id = "dates_degla",
            nameAr = "تمر دقلة نور طبيعي",
            nameEn = "Natural Deglet Noor Dates",
            nameFr = "Dattes Deglet Nour Naturelles",
            origin = FoodOrigin.PLANT,
            category = FoodCategory.FRUITS,
            caloriesPer100g = 282,
            proteinPer100g = 2.5f,
            carbsPer100g = 75.0f,
            fatPer100g = 0.4f,
            vitaminsAndMineralsAr = "بوتاسيوم عالي جداً, مغنيسيوم, فيتامين B6, نحاس",
            vitaminsAndMineralsEn = "High Potassium, Magnesium, Vitamin B6, Copper",
            vitaminsAndMineralsFr = "Potassium très élevé, Magnésium, Vitamine B6, Cuivre",
            healthBenefitAr = "سناك سريع الامتصاص يمنع التشنجات العضلية ويوفر طاقة فورية للتمرين.",
            healthBenefitEn = "Instant pre-workout energy and potassium to prevent muscle cramps.",
            healthBenefitFr = "Énergie rapide avant l'effort et potassium pour éviter les crampes.",
            emoji = "🌴",
            defaultServingGrams = 50
        ),
        WholeFoodItem(
            id = "fresh_banana",
            nameAr = "موز طبيعي طازج",
            nameEn = "Fresh Ripe Banana",
            nameFr = "Banane Fraîche",
            origin = FoodOrigin.PLANT,
            category = FoodCategory.FRUITS,
            caloriesPer100g = 89,
            proteinPer100g = 1.1f,
            carbsPer100g = 22.8f,
            fatPer100g = 0.3f,
            vitaminsAndMineralsAr = "بوتاسيوم, فيتامين B6, فيتامين C, ألياف البكتين",
            vitaminsAndMineralsEn = "Potassium, Vitamin B6, Vitamin C, Pectin Fiber",
            vitaminsAndMineralsFr = "Potassium, Vitamine B6, Vitamine C, Fibres pectine",
            healthBenefitAr = "يعوض الشوارد المفقودة في العرق ويسهل الهضم ويزيد التركيز.",
            healthBenefitEn = "Replenishes electrolytes lost in sweat and supports quick recovery.",
            healthBenefitFr = "Reconstitue les électrolytes perdus et favorise la récupération.",
            emoji = "🍌",
            defaultServingGrams = 120
        ),
        WholeFoodItem(
            id = "extra_virgin_olive_oil",
            nameAr = "زيت زيتون بكر ممتاز",
            nameEn = "Extra Virgin Olive Oil",
            nameFr = "Huile d'Olive Extra Vierge",
            origin = FoodOrigin.PLANT,
            category = FoodCategory.HEALTHY_FATS_NUTS,
            caloriesPer100g = 884,
            proteinPer100g = 0.0f,
            carbsPer100g = 0.0f,
            fatPer100g = 100.0f,
            vitaminsAndMineralsAr = "بوليفينولات (مضادات أكسدة), فيتامين E, أوميغا-9 (حمض الأوليك)",
            vitaminsAndMineralsEn = "Polyphenols, Vitamin E, Omega-9 (Oleic Acid)",
            vitaminsAndMineralsFr = "Polyphénols, Vitamine E, Oméga-9 (Acide oléique)",
            healthBenefitAr = "أفضل دهون أحادية غير مشبعة تدعم هرمون التستوستيرون وصحة القلب.",
            healthBenefitEn = "Monounsaturated fat gold standard boosting natural hormone synthesis.",
            healthBenefitFr = "Graisse saine favorisant la production hormonale et la santé cardiaque.",
            emoji = "🫒",
            defaultServingGrams = 15
        ),
        WholeFoodItem(
            id = "raw_almonds",
            nameAr = "لوز نيء طبيعي",
            nameEn = "Raw Natural Almonds",
            nameFr = "Amandes Crues Naturelles",
            origin = FoodOrigin.PLANT,
            category = FoodCategory.HEALTHY_FATS_NUTS,
            caloriesPer100g = 579,
            proteinPer100g = 21.2f,
            carbsPer100g = 21.6f,
            fatPer100g = 49.9f,
            vitaminsAndMineralsAr = "فيتامين E (مضاد شيخوخة), مغنيسيوم, زنك, كالسيوم",
            vitaminsAndMineralsEn = "Vitamin E, Magnesium, Zinc, Calcium",
            vitaminsAndMineralsFr = "Vitamine E, Magnésium, Zinc, Calcium",
            healthBenefitAr = "يحسن جودة النوم والاستشفاء العضلي الليلي بفضل وفرة المغنيسيوم.",
            healthBenefitEn = "Elevates sleep quality and overnight muscle repair with rich magnesium.",
            healthBenefitFr = "Améliore la récupération nocturne et le sommeil grâce au magnésium.",
            emoji = "🥜",
            defaultServingGrams = 30
        ),
        WholeFoodItem(
            id = "avocado",
            nameAr = "أفوكادو طازج",
            nameEn = "Fresh Hass Avocado",
            nameFr = "Avocat Frais",
            origin = FoodOrigin.PLANT,
            category = FoodCategory.HEALTHY_FATS_NUTS,
            caloriesPer100g = 160,
            proteinPer100g = 2.0f,
            carbsPer100g = 8.5f,
            fatPer100g = 14.7f,
            vitaminsAndMineralsAr = "بوتاسيوم (أعلى من الموز), فيتامين K, حمض الفوليك, ألياف",
            vitaminsAndMineralsEn = "Potassium, Vitamin K, Folate, Dietary Fiber",
            vitaminsAndMineralsFr = "Potassium, Vitamine K, Folate, Fibres alimentaires",
            healthBenefitAr = "يساعد على امتصاص الفيتامينات الذائبة في الدهون (A, D, E, K) من الوجبات.",
            healthBenefitEn = "Enhances nutrient absorption of fat-soluble vitamins (A, D, E, K).",
            healthBenefitFr = "Favorise l'assimilation des vitamines liposolubles (A, D, E, K).",
            emoji = "🥑",
            defaultServingGrams = 80
        ),
        WholeFoodItem(
            id = "berries_mix",
            nameAr = "توت بري مشكل (فراولة وتوت)",
            nameEn = "Mixed Fresh Berries",
            nameFr = "Mélange de Baies Fraîches",
            origin = FoodOrigin.PLANT,
            category = FoodCategory.FRUITS,
            caloriesPer100g = 57,
            proteinPer100g = 0.7f,
            carbsPer100g = 14.5f,
            fatPer100g = 0.3f,
            vitaminsAndMineralsAr = "أنثوسيانين (مضادات أكسدة قوية), فيتامين C, منغنيز",
            vitaminsAndMineralsEn = "Anthocyanins, Vitamin C, Manganese",
            vitaminsAndMineralsFr = "Anthocyanines, Vitamine C, Manganèse",
            healthBenefitAr = "مؤشر جلايسيمي منخفض جداً ومثالي لتقليل إجهاد العضلات التأكسدي بعد التمرين.",
            healthBenefitEn = "Low glycemic index fruit mitigating oxidative muscle stress post-workout.",
            healthBenefitFr = "Faible indice glycémique réduisant le stress oxydatif musculaire.",
            emoji = "🫐",
            defaultServingGrams = 100
        )
    )
}
