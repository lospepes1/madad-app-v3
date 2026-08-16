package com.example.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.data.model.GymAlarm

object GymAlarmScheduler {
    private const val TAG = "GymAlarmScheduler"
    const val EXTRA_ALARM_ID = "extra_alarm_id"
    const val EXTRA_ALARM_LABEL = "extra_alarm_label"
    const val EXTRA_ALARM_HOUR = "extra_alarm_hour"
    const val EXTRA_ALARM_MINUTE = "extra_alarm_minute"

    fun scheduleAlarm(context: Context, alarm: GymAlarm) {
        if (!alarm.isEnabled) {
            cancelAlarm(context, alarm.id)
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val triggerMillis = alarm.getNextTriggerMillis()

        val intent = Intent(context, GymAlarmReceiver::class.java).apply {
            action = "com.example.ACTION_GYM_ALARM"
            putExtra(EXTRA_ALARM_ID, alarm.id)
            putExtra(EXTRA_ALARM_LABEL, alarm.label)
            putExtra(EXTRA_ALARM_HOUR, alarm.hour)
            putExtra(EXTRA_ALARM_MINUTE, alarm.minute)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    val showIntent = Intent(context, com.example.MainActivity::class.java)
                    val showPendingIntent = PendingIntent.getActivity(
                        context,
                        alarm.id.hashCode() + 1,
                        showIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerMillis, showPendingIntent)
                    alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
                } else {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
            }
            Log.d(TAG, "Scheduled alarm ${alarm.id} for $triggerMillis")
        } catch (e: SecurityException) {
            Log.e(TAG, "Failed to schedule exact alarm: ${e.message}")
            try {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
            } catch (ex: Exception) {
                Log.e(TAG, "Fallback alarm schedule error: ${ex.message}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error scheduling alarm: ${e.message}")
        }
    }

    fun cancelAlarm(context: Context, alarmId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, GymAlarmReceiver::class.java).apply {
            action = "com.example.ACTION_GYM_ALARM"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId.hashCode(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            Log.d(TAG, "Cancelled alarm $alarmId")
        }
    }
}
