package com.example.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.data.local.GymAlarmRepository

class GymBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == Intent.ACTION_BOOT_COMPLETED || action == "android.intent.action.QUICKBOOT_POWERON") {
            Log.d("GymBootReceiver", "Boot completed: Rescheduling all enabled gym alarms")
            val repository = GymAlarmRepository(context)
            val alarms = repository.getAlarms()
            alarms.filter { it.isEnabled }.forEach { alarm ->
                GymAlarmScheduler.scheduleAlarm(context, alarm)
            }
        }
    }
}
