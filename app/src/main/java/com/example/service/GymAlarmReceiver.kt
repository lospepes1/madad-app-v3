package com.example.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.data.local.GymAlarmRepository

class GymAlarmReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "gym_workout_alarms_channel"
        const val CHANNEL_NAME = "Gym Workout Alarms"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getStringExtra(GymAlarmScheduler.EXTRA_ALARM_ID) ?: return
        val alarmLabel = intent.getStringExtra(GymAlarmScheduler.EXTRA_ALARM_LABEL) ?: "Gym Workout"

        createNotificationChannel(context)
        showAlarmNotification(context, alarmId, alarmLabel)
        triggerVibration(context)

        // Reschedule next repeating trigger for this alarm
        val repository = GymAlarmRepository(context)
        val alarm = repository.getAlarm(alarmId)
        if (alarm != null && alarm.isEnabled) {
            if (alarm.repeatDays.isNotEmpty()) {
                GymAlarmScheduler.scheduleAlarm(context, alarm)
            } else {
                // One-time alarm triggered: mark as disabled
                repository.toggleAlarm(alarmId, false)
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .build()

                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "High priority notifications and reminders for scheduled gym workouts"
                    enableLights(true)
                    enableVibration(true)
                    vibrationPattern = longArrayOf(0, 400, 200, 400, 200, 600)
                    setSound(soundUri, audioAttributes)
                    lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
                }
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    private fun showAlarmNotification(context: Context, alarmId: String, alarmLabel: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return

        val launchIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("navigate_to_tab", 2) // Gym alarm tab
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            alarmId.hashCode() + 100,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val title = "🏋️ $alarmLabel"
        val message = "حان وقت التمرين والجيم! انطلق وحقق أهدافك الرياضية اليوم 💪"

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setVibrate(longArrayOf(0, 500, 200, 500, 200, 500))
            .setContentIntent(pendingIntent)
            .build()

        val notificationId = (System.currentTimeMillis() % 100000).toInt() + alarmId.hashCode() % 1000
        notificationManager.notify(notificationId, notification)
    }

    private fun triggerVibration(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                val vibrator = vibratorManager?.defaultVibrator
                vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 400, 200, 400), -1))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(longArrayOf(0, 400, 200, 400), -1)
            }
        } catch (_: Exception) {}
    }
}
