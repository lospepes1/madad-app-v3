package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.GymAlarm
import com.example.service.GymAlarmScheduler
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar

class GymAlarmRepository(private val context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("gym_alarms_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ALARMS_JSON = "saved_gym_alarms"
    }

    fun getAlarms(): List<GymAlarm> {
        val jsonStr = prefs.getString(KEY_ALARMS_JSON, null)
        if (jsonStr.isNullOrEmpty()) {
            // Provide helpful default gym workout alarms on first launch
            val defaultAlarms = listOf(
                GymAlarm(
                    hour = 7,
                    minute = 0,
                    label = "تمارين الصباح والجيم 🌅",
                    isEnabled = true,
                    repeatDays = setOf(Calendar.MONDAY, Calendar.WEDNESDAY, Calendar.FRIDAY)
                ),
                GymAlarm(
                    hour = 17,
                    minute = 30,
                    label = "تمرين بعد الظهر (Leg Day) 🏋️",
                    isEnabled = false,
                    repeatDays = setOf(Calendar.TUESDAY, Calendar.THURSDAY)
                ),
                GymAlarm(
                    hour = 19,
                    minute = 0,
                    label = "جلسة الكارديو واللياقة ⚡",
                    isEnabled = false,
                    repeatDays = setOf(Calendar.SATURDAY)
                )
            )
            saveAlarms(defaultAlarms)
            // Schedule the default active alarm
            defaultAlarms.filter { it.isEnabled }.forEach {
                GymAlarmScheduler.scheduleAlarm(context, it)
            }
            return defaultAlarms
        }

        return try {
            val jsonArray = JSONArray(jsonStr)
            val list = mutableListOf<GymAlarm>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(GymAlarm.fromJson(obj))
            }
            list.sortedWith(compareBy({ it.hour }, { it.minute }))
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getAlarm(alarmId: String): GymAlarm? {
        return getAlarms().find { it.id == alarmId }
    }

    fun saveAlarms(alarms: List<GymAlarm>) {
        val jsonArray = JSONArray()
        alarms.forEach { jsonArray.put(it.toJson()) }
        prefs.edit().putString(KEY_ALARMS_JSON, jsonArray.toString()).apply()
    }

    fun addOrUpdateAlarm(alarm: GymAlarm) {
        val current = getAlarms().toMutableList()
        val index = current.indexOfFirst { it.id == alarm.id }
        if (index >= 0) {
            current[index] = alarm
        } else {
            current.add(alarm)
        }
        saveAlarms(current)

        if (alarm.isEnabled) {
            GymAlarmScheduler.scheduleAlarm(context, alarm)
        } else {
            GymAlarmScheduler.cancelAlarm(context, alarm.id)
        }
    }

    fun toggleAlarm(alarmId: String, isEnabled: Boolean) {
        val current = getAlarms().toMutableList()
        val index = current.indexOfFirst { it.id == alarmId }
        if (index >= 0) {
            val updated = current[index].copy(isEnabled = isEnabled)
            current[index] = updated
            saveAlarms(current)

            if (isEnabled) {
                GymAlarmScheduler.scheduleAlarm(context, updated)
            } else {
                GymAlarmScheduler.cancelAlarm(context, alarmId)
            }
        }
    }

    fun deleteAlarm(alarmId: String) {
        GymAlarmScheduler.cancelAlarm(context, alarmId)
        val current = getAlarms().filterNot { it.id == alarmId }
        saveAlarms(current)
    }
}
