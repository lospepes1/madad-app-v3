package com.example.data.model

import com.example.localization.LanguageManager
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar
import java.util.UUID

data class GymAlarm(
    val id: String = UUID.randomUUID().toString(),
    val hour: Int = 7, // 0..23
    val minute: Int = 0, // 0..59
    val label: String = "Leg Day Workout",
    val isEnabled: Boolean = true,
    val repeatDays: Set<Int> = setOf(Calendar.MONDAY, Calendar.WEDNESDAY, Calendar.FRIDAY), // Calendar.SUNDAY..SATURDAY
    val vibrate: Boolean = true,
    val sound: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
) {
    /**
     * Returns formatted time string like "07:00" or "07:00 AM"
     */
    fun formattedTime(use24Hour: Boolean = false): String {
        val minStr = if (minute < 10) "0$minute" else "$minute"
        return if (use24Hour) {
            val hrStr = if (hour < 10) "0$hour" else "$hour"
            "$hrStr:$minStr"
        } else {
            val period = if (hour < 12) "AM" else "PM"
            val displayHour = when {
                hour == 0 -> 12
                hour > 12 -> hour - 12
                else -> hour
            }
            val hrStr = if (displayHour < 10) "0$displayHour" else "$displayHour"
            "$hrStr:$minStr $period"
        }
    }

    fun timeParts(): Pair<String, String> {
        val minStr = if (minute < 10) "0$minute" else "$minute"
        val period = if (hour < 12) "AM" else "PM"
        val displayHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        val hrStr = if (displayHour < 10) "0$displayHour" else "$displayHour"
        return Pair("$hrStr:$minStr", period)
    }

    /**
     * Calculates the exact next trigger time in epoch milliseconds.
     */
    fun getNextTriggerMillis(): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (repeatDays.isEmpty()) {
            // Once-off alarm: if past today, schedule for tomorrow
            if (target.timeInMillis <= now.timeInMillis) {
                target.add(Calendar.DAY_OF_YEAR, 1)
            }
            return target.timeInMillis
        }

        // Repeating on specific days
        var daysAhead = 0
        while (daysAhead < 8) {
            val candidate = (now.get(Calendar.DAY_OF_WEEK) - 1 + daysAhead) % 7 + 1
            if (repeatDays.contains(candidate)) {
                val candidateTarget = (target.clone() as Calendar).apply {
                    add(Calendar.DAY_OF_YEAR, daysAhead)
                }
                if (candidateTarget.timeInMillis > now.timeInMillis) {
                    return candidateTarget.timeInMillis
                }
            }
            daysAhead++
        }

        // Fallback
        target.add(Calendar.DAY_OF_YEAR, 1)
        return target.timeInMillis
    }

    /**
     * Computes human-readable time remaining string (e.g., "in 4 hrs, 20 mins").
     */
    fun getTimeRemainingDescription(lang: AppLanguage): String {
        val triggerMillis = getNextTriggerMillis()
        val diffMillis = triggerMillis - System.currentTimeMillis()
        if (diffMillis <= 0) return ""

        val totalMinutes = (diffMillis / (1000 * 60)).toInt()
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        return when (lang) {
            AppLanguage.AR -> {
                when {
                    hours > 0 && minutes > 0 -> "المنبه القادم خلال $hours ساعة و $minutes دقيقة"
                    hours > 0 -> "المنبه القادم خلال $hours ساعة"
                    minutes > 0 -> "المنبه القادم خلال $minutes دقيقة"
                    else -> "المنبه القادم خلال أقل من دقيقة"
                }
            }
            AppLanguage.FR -> {
                when {
                    hours > 0 && minutes > 0 -> "Dans $hours h et $minutes min"
                    hours > 0 -> "Dans $hours h"
                    minutes > 0 -> "Dans $minutes min"
                    else -> "Dans moins d'une minute"
                }
            }
            AppLanguage.EN -> {
                when {
                    hours > 0 && minutes > 0 -> "In $hours hrs, $minutes mins"
                    hours > 0 -> "In $hours hrs"
                    minutes > 0 -> "In $minutes mins"
                    else -> "In less than a minute"
                }
            }
        }
    }

    /**
     * Localized description of repeat days (e.g. "Mon, Wed, Fri", "Every day", "Once").
     */
    fun getRepeatDaysSummary(lang: AppLanguage): String {
        if (repeatDays.isEmpty()) {
            return when (lang) {
                AppLanguage.AR -> "مرة واحدة ⏱️"
                AppLanguage.FR -> "Une seule fois ⏱️"
                AppLanguage.EN -> "Once ⏱️"
            }
        }
        if (repeatDays.size == 7) {
            return when (lang) {
                AppLanguage.AR -> "كل يوم 🔄"
                AppLanguage.FR -> "Tous les jours 🔄"
                AppLanguage.EN -> "Every day 🔄"
            }
        }
        val weekdays = setOf(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY)
        if (repeatDays == weekdays) {
            return when (lang) {
                AppLanguage.AR -> "أيام الأسبوع (الإثنين - الجمعة)"
                AppLanguage.FR -> "En semaine (Lun - Ven)"
                AppLanguage.EN -> "Weekdays (Mon - Fri)"
            }
        }
        val weekends = setOf(Calendar.SATURDAY, Calendar.SUNDAY)
        if (repeatDays == weekends) {
            return when (lang) {
                AppLanguage.AR -> "عطلة نهاية الأسبوع (السبت - الأحد)"
                AppLanguage.FR -> "Week-end (Sam - Dim)"
                AppLanguage.EN -> "Weekends (Sat - Sun)"
            }
        }

        val dayNames = listOf(
            Calendar.MONDAY to LanguageManager.dayShortMon(lang),
            Calendar.TUESDAY to LanguageManager.dayShortTue(lang),
            Calendar.WEDNESDAY to LanguageManager.dayShortWed(lang),
            Calendar.THURSDAY to LanguageManager.dayShortThu(lang),
            Calendar.FRIDAY to LanguageManager.dayShortFri(lang),
            Calendar.SATURDAY to LanguageManager.dayShortSat(lang),
            Calendar.SUNDAY to LanguageManager.dayShortSun(lang)
        )

        return dayNames
            .filter { repeatDays.contains(it.first) }
            .joinToString(if (lang == AppLanguage.AR) "، " else ", ") { it.second }
    }

    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("id", id)
        json.put("hour", hour)
        json.put("minute", minute)
        json.put("label", label)
        json.put("isEnabled", isEnabled)
        val daysArray = JSONArray()
        repeatDays.forEach { daysArray.put(it) }
        json.put("repeatDays", daysArray)
        json.put("vibrate", vibrate)
        json.put("sound", sound)
        json.put("createdAt", createdAt)
        return json
    }

    companion object {
        fun fromJson(json: JSONObject): GymAlarm {
            val repeatDays = mutableSetOf<Int>()
            val daysArray = json.optJSONArray("repeatDays")
            if (daysArray != null) {
                for (i in 0 until daysArray.length()) {
                    repeatDays.add(daysArray.getInt(i))
                }
            }
            return GymAlarm(
                id = json.optString("id", UUID.randomUUID().toString()),
                hour = json.optInt("hour", 7),
                minute = json.optInt("minute", 0),
                label = json.optString("label", "Gym Workout"),
                isEnabled = json.optBoolean("isEnabled", true),
                repeatDays = repeatDays,
                vibrate = json.optBoolean("vibrate", true),
                sound = json.optBoolean("sound", true),
                createdAt = json.optLong("createdAt", System.currentTimeMillis())
            )
        }
    }
}
