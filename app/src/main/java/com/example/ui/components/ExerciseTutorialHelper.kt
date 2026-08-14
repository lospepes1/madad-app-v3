package com.example.ui.components

import com.example.data.model.AppLanguage
import com.example.data.model.Exercise

data class ExerciseTutorialDetail(
    val exerciseName: String,
    val muscleTarget: String,
    val setsAndReps: String,
    val videoId: String,
    val setupTip: String,
    val gripTip: String,
    val executionTip: String,
    val breathingTip: String
)

object ExerciseTutorialHelper {

    fun getTutorial(
        exerciseName: String,
        muscleTarget: String = "",
        setsAndReps: String = "",
        explicitVideoId: String = "",
        lang: AppLanguage = AppLanguage.AR
    ): ExerciseTutorialDetail {
        val lower = exerciseName.lowercase()

        val baseDetail = when {
            // Barbell Squats (User example: "ultWZbUMPL8")
            lower.contains("squat") || lower.contains("سكوات") || lower.contains("fessier") || lower.contains("أفخاذ") -> {
                when (lang) {
                    AppLanguage.FR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Quadriceps, Fessiers & Ischios",
                        setsAndReps = setsAndReps,
                        videoId = "ultWZbUMPL8",
                        setupTip = "Barre posée sur les trapèzes, pieds écartés largeur d'épaules et pointes de pieds légèrement orientées vers l'extérieur (15°-30°).",
                        gripTip = "Mains fermement serrées sur la barre, coudes tirés vers l'arrière.",
                        executionTip = "Initiez le mouvement en poussant les hanches vers l'arrière, descendez jusqu'à ce que les cuisses soient parallèles au sol en gardant le torse fier.",
                        breathingTip = "Grand volume d'air dans le ventre (gainage) avant de descendre, expiration après le passage du point dur."
                    )
                    AppLanguage.EN -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Quads, Glutes & Core",
                        setsAndReps = setsAndReps,
                        videoId = "ultWZbUMPL8",
                        setupTip = "Rest the bar across your upper traps, stand with feet shoulder-width apart with toes flared outward 15°–30°.",
                        gripTip = "Firm grip on the bar, pull elbows back to create a sturdy muscular shelf.",
                        executionTip = "Hinge at hips first, squat down until thighs are at least parallel to the floor while driving knees out in line with toes.",
                        breathingTip = "Deep belly breath and core brace before descent; exhale strongly on the ascent after the sticking point."
                    )
                    AppLanguage.AR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "الأفخاذ، الأرداف والجذع (Quads & Glutes)",
                        setsAndReps = setsAndReps,
                        videoId = "ultWZbUMPL8",
                        setupTip = "ضع البار على عضلات الترابيز العلوية، وقف مع مباعدة القدمين بعرض الكتفين وتوجيه أصابع القدم للخارج قليلاً (15-30 درجة).",
                        gripTip = "امسك البار بقبضة محكمة وثبت لوحي الكتف للخلف لتكوين قاعدة صلبة للبار.",
                        executionTip = "ابدأ بدفع الحوض للخلف ثم اثنِ الركبتين حتى يصبح الفخذان موازيين للأرض مع توجيه الركبتين باتجاه أصابع القدم وصدر مرفوع.",
                        breathingTip = "احبس نفساً عميقاً في البطن (Bracing) أثناء النزول، وازفر بقوة بعد تجاوز أصعب نقطة أثناء الصعود."
                    )
                }
            }

            // Flat Bench Press (User example: "rT7DGvm-3yy")
            lower.contains("bench press") || lower.contains("بنش") || (lower.contains("صدر") && !lower.contains("عالي") && !lower.contains("مائل") && !lower.contains("incline")) || lower.contains("développé couché") -> {
                when (lang) {
                    AppLanguage.FR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Pectoraux (Poitrine)",
                        setsAndReps = setsAndReps,
                        videoId = "rT7DGvm-3yy",
                        setupTip = "Allongez-vous sur le banc plat, les pieds bien ancrés au sol, les omoplates resserrées et le bas du dos légèrement cambré.",
                        gripTip = "Saisissez la barre avec une prise légèrement plus large que la largeur des épaules, les poignets bien droits.",
                        executionTip = "Descendez la barre avec contrôle jusqu'au milieu de la poitrine en gardant les coudes à environ 45-70 degrés, puis poussez puissamment.",
                        breathingTip = "Inspirez profondément pendant la descente, et expirez fort lors de la poussée."
                    )
                    AppLanguage.EN -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Chest (Pectorals)",
                        setsAndReps = setsAndReps,
                        videoId = "rT7DGvm-3yy",
                        setupTip = "Lie flat on the bench with feet firmly planted on the floor, retract your shoulder blades down and back with a slight natural arch in the lower back.",
                        gripTip = "Grip the bar slightly wider than shoulder-width with wrists straight and stacked over elbows.",
                        executionTip = "Lower the bar with control until it gently touches the mid-chest, keeping elbows tucked at roughly 45–70°, then press up explosively.",
                        breathingTip = "Inhale on the controlled way down, and exhale forcefully as you drive the weight up."
                    )
                    AppLanguage.AR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "عضلات الصدر (Pectorals)",
                        setsAndReps = setsAndReps,
                        videoId = "rT7DGvm-3yy",
                        setupTip = "استلقِ على المقعد المسطح مع تثبيت القدمين بإحكام على الأرض، وثني لوحي الكتف للخلف والأسفل مع تقوس طبيعي طفيف في أسفل الظهر.",
                        gripTip = "امسك البار بقبضة أعرض قليلاً من عرض الكتفين مع إبقاء المعصمين مستقيمين تماماً فوق المرفقين.",
                        executionTip = "أنزل البار ببطء وتحكم حتى يلامس منتصف الصدر مع الحفاظ على زاوية الكوع 45-70 درجة، ثم ادفع البار لأعلى بقوة وانفجارية.",
                        breathingTip = "خذ شهيقاً عميقاً عند نزول البار للأسفل، وازفر بقوة أثناء الدفع لأعلى نقطة."
                    )
                }
            }

            // Lat Pulldown (User example: "CAwf7n6Luuc")
            lower.contains("lat pull") || lower.contains("سحب") || lower.contains("tirage") || lower.contains("pull up") || lower.contains("عقلة") -> {
                when (lang) {
                    AppLanguage.FR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Grand Dorsal (Dos Large)",
                        setsAndReps = setsAndReps,
                        videoId = "CAwf7n6Luuc",
                        setupTip = "Cuisse bien calées sous les boudins, buste légèrement incliné en arrière de 10-15°, poitrine bombée.",
                        gripTip = "Prise large en pronation, mains un peu plus larges que les épaules.",
                        executionTip = "Tirez la barre vers le haut des pectoraux en guidant avec les coudes vers le bas et l'arrière, serrez le dos en bas.",
                        breathingTip = "Expirez pendant la traction vers le bas, inspirez lors du retour contrôlé vers le haut."
                    )
                    AppLanguage.EN -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Lats & Upper Back",
                        setsAndReps = setsAndReps,
                        videoId = "CAwf7n6Luuc",
                        setupTip = "Lock thighs firmly under pads, sit with chest lifted and lean back slightly (10°–15°).",
                        gripTip = "Overhand wide grip, slightly wider than shoulder width.",
                        executionTip = "Drive elbows down and back to pull the bar to your upper collarbone; squeeze lats at the bottom, then control the stretch up.",
                        breathingTip = "Exhale as you pull down, inhale smoothly as the bar ascends."
                    )
                    AppLanguage.AR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "عضلات الظهر العريض (Lats)",
                        setsAndReps = setsAndReps,
                        videoId = "CAwf7n6Luuc",
                        setupTip = "ثبّت الفخذين بإحكام تحت المسند، واجلس مع ميل طفيف للخلف (10-15 درجة) وصدر مرفوع للأعلى.",
                        gripTip = "قبضة واسعة أعرض من الكتفين مع توجيه الراحتين للأمام.",
                        executionTip = "اسحب البار نحو أعلى الصدر بتوجيه الكوعين للأسفل وللخلف وعصر الظهر في قاع الحركة، ثم عد ببطء للأعلى.",
                        breathingTip = "ازفر أثناء سحب البار للأسفل، وشهيق أثناء الصعود البطيء والمتحكم فيه."
                    )
                }
            }

            // Dumbbell Shoulder Press (User example: "qEwKCR5JCog")
            lower.contains("shoulder") || lower.contains("overhead") || lower.contains("كتف") || lower.contains("أكتاف") || lower.contains("développé militaire") -> {
                when (lang) {
                    AppLanguage.FR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Deltoïdes & Épaules",
                        setsAndReps = setsAndReps,
                        videoId = "qEwKCR5JCog",
                        setupTip = "Assis le dos bien plaqué contre le dossier ou debout avec abdos et fessiers contractés.",
                        gripTip = "Prise au niveau des oreilles, coudes sous les poignets formant un angle proche de 90°.",
                        executionTip = "Développez les charges verticalement vers le haut sans claquer les coudes en haut.",
                        breathingTip = "Expirez lors de la poussée vers le haut, inspirez à la redescente."
                    )
                    AppLanguage.EN -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Shoulders (Deltoids) & Triceps",
                        setsAndReps = setsAndReps,
                        videoId = "qEwKCR5JCog",
                        setupTip = "Sit upright with back supported or stand tall with glutes and abs braced to prevent spinal hyperextension.",
                        gripTip = "Hold weights level with your ears with wrists stacked over elbows.",
                        executionTip = "Press straight up overhead until arms are nearly extended, then lower with tempo.",
                        breathingTip = "Exhale as you press upward, inhale on the descent."
                    )
                    AppLanguage.AR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "عضلات الأكتاف (Deltoids)",
                        setsAndReps = setsAndReps,
                        videoId = "qEwKCR5JCog",
                        setupTip = "اجلس مع ظهر مسنود بالكامل أو قف مع شد البطن والأرداف لمنع تقوس الظهر وحماية الفقرات.",
                        gripTip = "امسك الأوزان بمحاذاة الأذنين مع جعل المرفقين أسفل المعصمين مباشرة.",
                        executionTip = "ادفع الأوزان رأسياً للأعلى حتى فرد الذراعين تقريباً دون قفل المرفقين بقوة، ثم أنزل ببطء.",
                        breathingTip = "ازفر أثناء دفع الأوزان للأعلى، وشهيق أثناء النزول التدريجي."
                    )
                }
            }

            // Plank / Core / Abs (User example: "pSHjTRCQxIw")
            lower.contains("plank") || lower.contains("بلانك") || lower.contains("gainage") || lower.contains("بطن") || lower.contains("abs") || lower.contains("core") -> {
                when (lang) {
                    AppLanguage.FR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Sangle Abdominale & Gainage",
                        setsAndReps = setsAndReps,
                        videoId = "pSHjTRCQxIw",
                        setupTip = "En appui sur les avant-bras, coudes alignés sous les épaules, corps formant une ligne droite parfaite de la tête aux talons.",
                        gripTip = "Mains posées à plat ou poings fermés détendus, regard vers le sol.",
                        executionTip = "Rétroversion du bassin, serrez les abdos et les fessiers à 100% sans laisser le bas du dos s'affaisser.",
                        breathingTip = "Respirez calmement par le nez en maintenant le diaphragme et la sangle abdominale sous tension constante."
                    )
                    AppLanguage.EN -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Core & Abdominals",
                        setsAndReps = setsAndReps,
                        videoId = "pSHjTRCQxIw",
                        setupTip = "Rest on forearms with elbows stacked directly beneath shoulders; body in a rigid straight line from head to heels.",
                        gripTip = "Palms flat on the floor or light fists, neck neutral looking slightly ahead of hands.",
                        executionTip = "Tuck tailbone slightly, squeeze glutes and brace your core like bracing for a punch without letting hips sag or pike.",
                        breathingTip = "Breathe steadily in and out through your nose while keeping constant 360-degree intra-abdominal pressure."
                    )
                    AppLanguage.AR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "عضلات الجذع والبطن (Core & Abs)",
                        setsAndReps = setsAndReps,
                        videoId = "pSHjTRCQxIw",
                        setupTip = "ارتكز على الساعدين مع محاذاة المرفقين أسفل الكتفين مباشرة، واجعل جسمك في خط مستقيم من الرأس إلى الكعبين.",
                        gripTip = "ضع راحتي اليدين على الأرض بنعومة مع توجيه النظر للأسفل لتفادي إجهاد الرقبة.",
                        executionTip = "اعصر عضلات البطن والأرداف بقوة لمنع هبوط أسفل الظهر أو رفع الحوض لأعلى.",
                        breathingTip = "تنفس بانتظام وهدوء مع المحافظة على شد وانقباض عضلات البطن طوال فترة الثبات."
                    )
                }
            }

            // Incline Press
            lower.contains("incline") || lower.contains("عالي") || lower.contains("مائل") || lower.contains("incliné") -> {
                when (lang) {
                    AppLanguage.FR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Haut des Pectoraux",
                        setsAndReps = setsAndReps,
                        videoId = "8iPEnn-ltC8",
                        setupTip = "Réglez le banc entre 30° et 45° maximum pour bien cibler le faisceau claviculaire sans surcharger les deltoïdes.",
                        gripTip = "Poignets stables, prise alignée avec le haut des pectoraux.",
                        executionTip = "Descendez les haltères ou la barre jusqu'au haut des pectoraux avec un étirement contrôlé, puis développez vers le haut.",
                        breathingTip = "Inspiration à la descente, expiration énergique à la poussée."
                    )
                    AppLanguage.EN -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Upper Chest & Front Delts",
                        setsAndReps = setsAndReps,
                        videoId = "8iPEnn-ltC8",
                        setupTip = "Set the bench angle to 30°–45° maximum to isolate the upper pectoral fibers without over-recruiting anterior shoulders.",
                        gripTip = "Grip weights securely with wrists locked directly over forearms.",
                        executionTip = "Lower weights smoothly until you feel a deep stretch in the upper chest, then press upwards in a slight natural arc.",
                        breathingTip = "Inhale as you lower, exhale forcefully at the top of the press."
                    )
                    AppLanguage.AR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "الصدر العلوي (Upper Chest)",
                        setsAndReps = setsAndReps,
                        videoId = "8iPEnn-ltC8",
                        setupTip = "اضبط زاوية المقعد بين 30 و 45 درجة كحد أقصى لتركيز المقاومة على ألياف الصدر العلوي وحماية مفصل الكتف.",
                        gripTip = "امسك الأوزان بقبضة قوية بمحاذاة الصدر العلوي مع تثبيت المعصمين.",
                        executionTip = "أنزل الأوزان ببطء حتى تشعر بتمدد عضلات الصدر العلوية، ثم ادفع للأعلى بمسار مقوس قليلاً نحو المركز.",
                        breathingTip = "شهيق تدريجي أثناء النزول، وزفير قوي أثناء الرفع لأعلى."
                    )
                }
            }

            // Deadlift
            lower.contains("deadlift") || lower.contains("ديدلفت") || lower.contains("مميتة") || lower.contains("soulevé de terre") -> {
                when (lang) {
                    AppLanguage.FR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Chaîne Postérieure & Dos",
                        setsAndReps = setsAndReps,
                        videoId = "op9kVnSso6Q",
                        setupTip = "Pieds écartés largeur de hanches, la barre au-dessus du milieu du pied. Dos parfaitement neutre.",
                        gripTip = "Prise double pronation ou inversée, mains juste à l'extérieur des tibias.",
                        executionTip = "Poussez le sol avec les talons, gardez la barre collée aux tibias et cuisses jusqu'au verrouillage complet des hanches.",
                        breathingTip = "Gainage abdominal maximal avant de tirer, expiration en fin d'extension."
                    )
                    AppLanguage.EN -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Posterior Chain, Back & Glutes",
                        setsAndReps = setsAndReps,
                        videoId = "op9kVnSso6Q",
                        setupTip = "Stand with feet hip-width apart, bar over mid-foot against shins with a flat neutral spine.",
                        gripTip = "Grip outside the knees firmly, lock lats tight.",
                        executionTip = "Push the floor away with your legs, keeping bar dragged close to legs until standing tall at full hip lockout without hyperextending.",
                        breathingTip = "Inhale deep into belly and brace before pulling, exhale at top lockout."
                    )
                    AppLanguage.AR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "عضلات الظهر، الهامسترينغ والأرداف",
                        setsAndReps = setsAndReps,
                        videoId = "op9kVnSso6Q",
                        setupTip = "قف مع مباعدة القدمين بعرض الحوض والبار ملامس لقصبة الساق، مع الحفاظ على استقامة العمود الفقري الكاملة.",
                        gripTip = "امسك البار بإحكام من خارج الساقين مباشرة مع تفعيل عضلات الظهر العريضة (Lats).",
                        executionTip = "ادفع الأرض بكعبي القدمين واسحب البار بمحاذاة الساقين حتى الوقوف المستقيم وقفل الحوض دون الانحناء للخلف.",
                        breathingTip = "شهيق عميق وحبس الهواء لشد البطن قبل الرفع، وزفير عند الوصول للوقوف الكامل."
                    )
                }
            }

            // Row / Barbell Row / Cable Row
            lower.contains("row") || lower.contains("تجديف") || lower.contains("rowing") -> {
                when (lang) {
                    AppLanguage.FR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Épaisseur du Dos & Trapèzes",
                        setsAndReps = setsAndReps,
                        videoId = "G8l_8chR5BE",
                        setupTip = "Buste penché vers l'avant à 45°, genoux légèrement fléchis et colonne vertébrale neutre sans cambrure excessive.",
                        gripTip = "Prise ferme largeur des épaules, poignets solides.",
                        executionTip = "Tirez la charge vers le bas du ventre en serrant les omoplates l'une contre l'autre en haut du mouvement.",
                        breathingTip = "Expirez en tirant la charge vers le nombril, inspirez en relâchant."
                    )
                    AppLanguage.EN -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Mid Back, Rhomboids & Lats",
                        setsAndReps = setsAndReps,
                        videoId = "G8l_8chR5BE",
                        setupTip = "Hinge forward at 45° with knees slightly unlocked and back completely straight.",
                        gripTip = "Shoulder-width grip, hands locked tight.",
                        executionTip = "Pull bar toward your lower belly/navel, squeezing your shoulder blades together at peak contraction without torso swinging.",
                        breathingTip = "Exhale on the pull toward the belly, inhale on the controlled descent."
                    )
                    AppLanguage.AR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "منتصف الظهر وسماكة الظهر",
                        setsAndReps = setsAndReps,
                        videoId = "G8l_8chR5BE",
                        setupTip = "انحنِ بجذعك للأمام بزاوية 45 درجة مع ظهر مستقيم تماماً وثني بسيط في الركبتين وتثبيت الجذع.",
                        gripTip = "امسك البار أو المقبض بقبضة بعرض الكتفين.",
                        executionTip = "اسحب الوزن باتجاه أسفل البطن (السرة) مع عصر لوحي الكتف معاً في القمة وتجنب تأرجح الظهر.",
                        breathingTip = "ازفر أثناء سحب الوزن باتجاه البطن، وشهيق أثناء الإنزال المتحكم فيه."
                    )
                }
            }

            // Lateral Raises
            lower.contains("lateral") || lower.contains("رفرفة") || lower.contains("élévation") || lower.contains("جانبي") -> {
                when (lang) {
                    AppLanguage.FR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Deltoïde Latéral (Largeur d'Épaule)",
                        setsAndReps = setsAndReps,
                        videoId = "3VcKaXpzqRo",
                        setupTip = "Debout, buste très légèrement penché en avant, coudes souples et légèrement fléchis.",
                        gripTip = "Haltères tenus souplement sans serrer excessivement.",
                        executionTip = "Élevez les bras sur les côtés jusqu'à hauteur des épaules en guidant par les coudes.",
                        breathingTip = "Expirez en montant, inspirez en retenant la descente."
                    )
                    AppLanguage.EN -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Lateral Deltoids (Shoulder Width)",
                        setsAndReps = setsAndReps,
                        videoId = "3VcKaXpzqRo",
                        setupTip = "Stand tall with a tiny forward hinge, knees soft and slight bend in the elbows.",
                        gripTip = "Grip dumbbells lightly without death-gripping.",
                        executionTip = "Raise arms out to the sides leading with your elbows until parallel with shoulders; control the descent.",
                        breathingTip = "Exhale on the raise, inhale slowly on the way down."
                    )
                    AppLanguage.AR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "الكتف الجانبي (Lateral Deltoid)",
                        setsAndReps = setsAndReps,
                        videoId = "3VcKaXpzqRo",
                        setupTip = "قف باستقامة مع ميل طفيف للأمام وثني بسيط في الكوعين دون تحريك الجذع.",
                        gripTip = "امسك الدامبلز بهدوء دون ضغط زائد بالمعصم.",
                        executionTip = "ارفع الذراعين جانباً بقيادة الكوعين حتى مستوى الكتفين فقط لتشغيل العضلة الجانبية، وأنزل ببطء.",
                        breathingTip = "ازفر عند رفع الذراعين للأعلى، وشهيق عند النزول الهادئ."
                    )
                }
            }

            // Biceps Curl
            lower.contains("bicep") || lower.contains("بايسبس") || lower.contains("curl") || lower.contains("ذراع") -> {
                when (lang) {
                    AppLanguage.FR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Biceps Brachial",
                        setsAndReps = setsAndReps,
                        videoId = "ykJmrZ5v0Oo",
                        setupTip = "Debout bien droit, coudes collés le long des flancs fixes pendant toute la répétition.",
                        gripTip = "Prise supination (paumes vers le haut) largeur d'épaules.",
                        executionTip = "Fléchissez les avant-bras vers le haut sans balancer le dos, contractez fort en haut et descendez sur 2 secondes.",
                        breathingTip = "Expirez à la montée, inspirez lors de la descente contrôlée."
                    )
                    AppLanguage.EN -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Biceps Brachii",
                        setsAndReps = setsAndReps,
                        videoId = "ykJmrZ5v0Oo",
                        setupTip = "Stand tall with chest proud, pin your elbows firmly against your ribcage.",
                        gripTip = "Palms facing up (supinated), shoulder-width grip.",
                        executionTip = "Curl the weight up using only your biceps, squeeze peak contraction at the top, then lower for 2 controlled seconds.",
                        breathingTip = "Exhale on the curl up, inhale as you lower the weight."
                    )
                    AppLanguage.AR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "عضلة البايسبس (Biceps)",
                        setsAndReps = setsAndReps,
                        videoId = "ykJmrZ5v0Oo",
                        setupTip = "قف باستقامة مع تثبيت المرفقين بإحكام ملتصقين بجانبي الخصر دون تحريكهما للأمام.",
                        gripTip = "امسك البار أو الدامبلز مع توجيه راحة اليد للأعلى بعرض الكتفين.",
                        executionTip = "ارفع الوزن عبر ثني الكوعين فقط حتى انقباض البايسبس بالكامل، ثم أنزل ببطء لمدة ثانيتين.",
                        breathingTip = "ازفر أثناء رفع الوزن للأعلى، وشهيق أثناء الإنزال التدريجي."
                    )
                }
            }

            // Triceps Pushdown / Extension
            lower.contains("tricep") || lower.contains("ترايسبس") || lower.contains("pushdown") || lower.contains("extension") -> {
                when (lang) {
                    AppLanguage.FR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Triceps (Arrière du bras)",
                        setsAndReps = setsAndReps,
                        videoId = "2-LAMcpzODU",
                        setupTip = "Debout avec une légère inclinaison en avant, coudes verrouillés le long du corps.",
                        gripTip = "Prise neutre pour la corde ou en pronation pour la barre droite.",
                        executionTip = "Poussez vers le bas jusqu'à extension complète des bras, écartez la corde en bas pour une contraction maximale.",
                        breathingTip = "Expirez en poussant vers le bas, inspirez en laissant remonter la charge."
                    )
                    AppLanguage.EN -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Triceps Brachii",
                        setsAndReps = setsAndReps,
                        videoId = "2-LAMcpzODU",
                        setupTip = "Stand with slight forward lean, lock elbows tight to your sides throughout.",
                        gripTip = "Neutral grip on rope or overhand on bar.",
                        executionTip = "Push down by extending only at the elbows, spreading the rope at the bottom for maximum triceps lockout.",
                        breathingTip = "Exhale as you push down, inhale as you control the return up."
                    )
                    AppLanguage.AR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "عضلة الترايسبس (Triceps)",
                        setsAndReps = setsAndReps,
                        videoId = "2-LAMcpzODU",
                        setupTip = "قف بثبات مع ميل خفيف للأمام وتثبيت المرفقين بجانبي الجسم طوال التمرين.",
                        gripTip = "امسك الحبل أو المقبض مع الحفاظ على استقامة المعصمين.",
                        executionTip = "ادفع للأسفل باستخدام الترايسبس وافرد الذراعين تماماً مع فتح الحبل بالأسفل لأقصى انقباض.",
                        breathingTip = "ازفر عند فرد الذراعين للأسفل، وشهيق عند العودة للأعلى ببطء."
                    )
                }
            }

            // Leg Press / Leg Ext / Hamstring
            lower.contains("leg") || lower.contains("press") || lower.contains("فخذ") || lower.contains("quad") -> {
                when (lang) {
                    AppLanguage.FR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Cuisses & Fessiers",
                        setsAndReps = setsAndReps,
                        videoId = "IZxyjW7MPJQ",
                        setupTip = "Dos et bassin bien collés au dossier de la machine sans décoller le bas du dos.",
                        gripTip = "Tenez fermement les poignées latérales de la machine.",
                        executionTip = "Poussez la plateforme avec les talons sans jamais verrouiller complètement les genoux en haut.",
                        breathingTip = "Inspirez lors de la flexion des jambes, expirez puissamment à la poussée."
                    )
                    AppLanguage.EN -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Quads, Hamstrings & Glutes",
                        setsAndReps = setsAndReps,
                        videoId = "IZxyjW7MPJQ",
                        setupTip = "Keep back and hips pressed flat against the seat pad with no lifting of the lower back.",
                        gripTip = "Hold side machine handles firmly for stability.",
                        executionTip = "Press through your heels without locking knees at the top; lower smoothly under control.",
                        breathingTip = "Inhale on the eccentric descent, exhale on the pressing drive."
                    )
                    AppLanguage.AR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "الأفخاذ والأرداف (Legs & Glutes)",
                        setsAndReps = setsAndReps,
                        videoId = "IZxyjW7MPJQ",
                        setupTip = "ثبّت ظهرك ومؤخرتك بإحكام على مسند الجهاز دون رفع أسفل الظهر.",
                        gripTip = "امسك المقابض الجانبية للجهاز لزيادة الثبات.",
                        executionTip = "ادفع المنصة بكعبي القدمين دون قفل الركبتين تماماً، وأنزل بتحكم وتجنب ارتداد الوزن.",
                        breathingTip = "شهيق عميق أثناء نزول الوزن، وزفير قوي أثناء الدفع."
                    )
                }
            }

            // Default / All-Round Gym Exercise
            else -> {
                when (lang) {
                    AppLanguage.FR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Corps Entier & Renforcement",
                        setsAndReps = setsAndReps,
                        videoId = "4Y2ZdHCOXok",
                        setupTip = "Adoptez une posture stable, pieds ancrés, colonne vertébrale neutre et abdos engagés.",
                        gripTip = "Prise équilibrée et sécurisée adaptée à la machine ou aux haltères.",
                        executionTip = "Exécutez le mouvement avec une amplitude complète et une cadence contrôlée (2 sec négative, 1 sec positive).",
                        breathingTip = "Inspirez sur la phase excentrique (retour), expirez lors de l'effort principal."
                    )
                    AppLanguage.EN -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "Muscle Focus & Strength",
                        setsAndReps = setsAndReps,
                        videoId = "4Y2ZdHCOXok",
                        setupTip = "Maintain a stable base of support with feet planted, spine neutral and core braced.",
                        gripTip = "Use a solid, comfortable grip aligned with your joints.",
                        executionTip = "Perform the movement through a full safe range of motion with tempo control (2 seconds down, 1 second up).",
                        breathingTip = "Inhale during the lowering phase, exhale during the contraction phase."
                    )
                    AppLanguage.AR -> ExerciseTutorialDetail(
                        exerciseName = exerciseName,
                        muscleTarget = if (muscleTarget.isNotEmpty()) muscleTarget else "تقوية وبناء العضلات",
                        setsAndReps = setsAndReps,
                        videoId = "4Y2ZdHCOXok",
                        setupTip = "حافظ على وضعية ثبات متوازنة مع تثبيت القدمين، استقامة العمود الفقري وشد عضلات البطن.",
                        gripTip = "امسك الوزن أو المقابض بإحكام مع محاذاة المفاصل.",
                        executionTip = "قم بأداء التمرين بمدى حركي كامل وبسرعة متحكم بها (ثانيتان في النزول وثانية في الرفع).",
                        breathingTip = "شهيق أثناء مرحلة النزول والانبساط، وزفير أثناء مرحلة الدفع والانقباض."
                    )
                }
            }
        }

        return if (explicitVideoId.isNotBlank()) {
            baseDetail.copy(videoId = explicitVideoId)
        } else {
            baseDetail
        }
    }
}
