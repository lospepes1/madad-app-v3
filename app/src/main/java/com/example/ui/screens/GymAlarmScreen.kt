package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.example.data.local.GymAlarmRepository
import com.example.data.model.AppLanguage
import com.example.data.model.GymAlarm
import com.example.localization.LanguageManager
import com.example.ui.theme.AppTheme
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymAlarmScreen(
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val colors = AppTheme.colors
    val repository = remember { GymAlarmRepository(context) }

    var alarmsList by remember { mutableStateOf(repository.getAlarms()) }
    var editingAlarm by remember { mutableStateOf<GymAlarm?>(null) }
    var isDialogVisible by remember { mutableStateOf(false) }

    // Permission state check for POST_NOTIFICATIONS (Android 13+)
    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
    }

    fun refreshAlarms() {
        alarmsList = repository.getAlarms()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("gym_alarm_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp)
        ) {
            // Screen Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = LanguageManager.gymAlarmTitle(language),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = colors.textPrimary
                        )
                        val activeCount = alarmsList.count { it.isEnabled }
                        Text(
                            text = when (language) {
                                AppLanguage.AR -> if (activeCount > 0) "$activeCount منبهات مفعلة" else "لا توجد منبهات مفعلة"
                                AppLanguage.FR -> if (activeCount > 0) "$activeCount alarme(s) active(s)" else "Aucune alarme active"
                                AppLanguage.EN -> if (activeCount > 0) "$activeCount active alarm(s)" else "No active alarms"
                            },
                            fontSize = 13.sp,
                            color = colors.textSecondary,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    // Simple "Set Alarm" action button in header
                    Button(
                        onClick = {
                            editingAlarm = null
                            isDialogVisible = true
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primaryAccent,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier.testTag("btn_set_alarm_header")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Set Alarm",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = when (language) {
                                AppLanguage.AR -> "ضبط منبه"
                                AppLanguage.FR -> "Régler"
                                AppLanguage.EN -> "Set Alarm"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Notification Permission Notice (Android 13+ if not granted)
            if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.dp, Color(0xFFFF9800), RoundedCornerShape(14.dp)),
                        color = Color(0xFFFF9800).copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = Color(0xFFFF9800),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = LanguageManager.permissionRequiredTitle(language),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textPrimary
                                )
                            }
                            Button(
                                onClick = { permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF9800),
                                    contentColor = Color.Black
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(text = "OK", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Empty state if no alarms exist
            if (alarmsList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Alarm,
                                contentDescription = null,
                                tint = colors.textSecondary.copy(alpha = 0.4f),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = when (language) {
                                    AppLanguage.AR -> "لا توجد منبهات للجيم، اضغط + لضبط منبه"
                                    AppLanguage.FR -> "Aucune alarme. Appuyez sur + pour régler"
                                    AppLanguage.EN -> "No gym alarms. Tap + to set an alarm"
                                },
                                fontSize = 14.sp,
                                color = colors.textSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Alarm Clock List Items
            items(alarmsList, key = { it.id }) { alarm ->
                MinimalGymAlarmItem(
                    alarm = alarm,
                    onToggle = { isEnabled ->
                        repository.toggleAlarm(alarm.id, isEnabled)
                        refreshAlarms()
                    },
                    onEdit = {
                        editingAlarm = alarm
                        isDialogVisible = true
                    },
                    onDelete = {
                        repository.deleteAlarm(alarm.id)
                        refreshAlarms()
                        Toast.makeText(
                            context,
                            if (language == AppLanguage.AR) "تم حذف المنبه" else "Alarm deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }

        // Floating Action Button for Setting Alarm
        FloatingActionButton(
            onClick = {
                editingAlarm = null
                isDialogVisible = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("fab_set_alarm"),
            containerColor = colors.primaryAccent,
            contentColor = Color.Black,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Set Alarm",
                modifier = Modifier.size(28.dp)
            )
        }
    }

    // ========================================================
    // Clean & Standard Time Picker Dialog
    // ========================================================
    if (isDialogVisible) {
        val initialAlarm = editingAlarm ?: GymAlarm(
            hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
            minute = (Calendar.getInstance().get(Calendar.MINUTE) / 5) * 5,
            label = if (language == AppLanguage.AR) "تمرين الجيم 🏋️" else "Gym Workout 🏋️"
        )

        val timePickerState = rememberTimePickerState(
            initialHour = initialAlarm.hour,
            initialMinute = initialAlarm.minute,
            is24Hour = false
        )

        var labelText by remember { mutableStateOf(initialAlarm.label) }

        Dialog(onDismissRequest = { isDialogVisible = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.5.dp, colors.primaryAccent.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                color = colors.cardBackgroundOpaque
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (editingAlarm != null) {
                            LanguageManager.editAlarmTitle(language)
                        } else {
                            when (language) {
                                AppLanguage.AR -> "ضبط منبه الجيم"
                                AppLanguage.FR -> "Régler l'alarme gym"
                                AppLanguage.EN -> "Set Gym Alarm"
                            }
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = colors.textPrimary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Standard Material 3 TimePicker
                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            clockDialColor = colors.surface,
                            selectorColor = colors.primaryAccent,
                            periodSelectorBorderColor = colors.primaryAccent,
                            periodSelectorSelectedContainerColor = colors.primaryAccent,
                            periodSelectorSelectedContentColor = Color.Black,
                            periodSelectorUnselectedContainerColor = colors.surface,
                            periodSelectorUnselectedContentColor = colors.textPrimary,
                            timeSelectorSelectedContainerColor = colors.primaryAccent,
                            timeSelectorSelectedContentColor = Color.Black,
                            timeSelectorUnselectedContainerColor = colors.surface,
                            timeSelectorUnselectedContentColor = colors.textPrimary
                        ),
                        modifier = Modifier.testTag("time_picker_component")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Alarm Label Field with Quick Presets (e.g., Morning Run, Leg Day, Cardio)
                    OutlinedTextField(
                        value = labelText,
                        onValueChange = { labelText = it },
                        label = {
                            Text(
                                text = when (language) {
                                    AppLanguage.AR -> "تسمية المنبه (مثل: تمرين الساقين، الجري الصباحي)"
                                    AppLanguage.FR -> "Libellé (ex: Course du matin, Leg Day)"
                                    AppLanguage.EN -> "Alarm Label (e.g., Morning Run, Leg Day)"
                                }
                            )
                        },
                        placeholder = {
                            Text(
                                text = when (language) {
                                    AppLanguage.AR -> "مثال: تمرين الساقين 🦵"
                                    AppLanguage.FR -> "Ex: Morning Run 🏃"
                                    AppLanguage.EN -> "e.g., Morning Run 🏃"
                                },
                                color = colors.textSecondary.copy(alpha = 0.5f)
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_alarm_label"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primaryAccent,
                            unfocusedBorderColor = colors.cardBorder,
                            focusedTextColor = colors.textPrimary,
                            unfocusedTextColor = colors.textPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick Custom Preset Label Chips
                    val suggestedLabels = when (language) {
                        AppLanguage.AR -> listOf("الجري الصباحي 🏃", "تمرين الساقين 🦵", "صدر وذراعين 💪", "كارديو ⚡", "تمرين شامل 🏋️")
                        AppLanguage.FR -> listOf("Morning Run 🏃", "Leg Day 🦵", "Cardio ⚡", "Pectoraux 💪", "Full Body 🏋️")
                        AppLanguage.EN -> listOf("Morning Run 🏃", "Leg Day 🦵", "Cardio ⚡", "Chest & Arms 💪", "Full Body 🏋️")
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        suggestedLabels.take(3).forEach { suggestion ->
                            val isSelected = labelText == suggestion
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { labelText = suggestion },
                                color = if (isSelected) colors.primaryAccent else colors.surface,
                                shape = RoundedCornerShape(8.dp),
                                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, colors.cardBorder)
                            ) {
                                Text(
                                    text = suggestion,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.Black else colors.textSecondary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Dialog Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { isDialogVisible = false },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.textSecondary)
                        ) {
                            Text(text = LanguageManager.cancelBtn(language))
                        }

                        Button(
                            onClick = {
                                val finalLabel = if (labelText.isNotBlank()) {
                                    labelText.trim()
                                } else {
                                    if (language == AppLanguage.AR) "تمرين الجيم 🏋️" else "Gym Workout 🏋️"
                                }

                                val savedAlarm = (editingAlarm ?: GymAlarm()).copy(
                                    hour = timePickerState.hour,
                                    minute = timePickerState.minute,
                                    label = finalLabel,
                                    isEnabled = true
                                )

                                repository.addOrUpdateAlarm(savedAlarm)
                                refreshAlarms()
                                isDialogVisible = false

                                Toast.makeText(
                                    context,
                                    "⏰ ${savedAlarm.formattedTime()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier
                                .weight(1.3f)
                                .testTag("btn_save_alarm_dialog"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.primaryAccent,
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = when (language) {
                                    AppLanguage.AR -> "حفظ المنبه"
                                    AppLanguage.FR -> "Enregistrer"
                                    AppLanguage.EN -> "Save Alarm"
                                },
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MinimalGymAlarmItem(
    alarm: GymAlarm,
    onToggle: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val colors = AppTheme.colors
    val isEnabled = alarm.isEnabled
    val (timeStr, period) = alarm.timeParts()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = if (isEnabled) colors.primaryAccent.copy(alpha = 0.5f) else colors.cardBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onEdit() }
            .testTag("alarm_item_${alarm.id}"),
        color = if (isEnabled) colors.cardBackgroundOpaque else colors.cardBackgroundOpaque.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Clock Digits and Label
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = timeStr,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isEnabled) colors.textPrimary else colors.textSecondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = period,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isEnabled) colors.primaryAccent else colors.textSecondary,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                if (alarm.label.isNotBlank()) {
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isEnabled) colors.primaryAccent.copy(alpha = 0.15f) else colors.surface.copy(alpha = 0.6f)
                        ) {
                            Text(
                                text = alarm.label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isEnabled) colors.primaryAccent else colors.textSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Right: Delete button and ON/OFF Switch
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("btn_delete_alarm_${alarm.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = colors.textSecondary.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Switch(
                    checked = isEnabled,
                    onCheckedChange = { onToggle(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = colors.primaryAccent,
                        uncheckedThumbColor = colors.textSecondary,
                        uncheckedTrackColor = colors.surface
                    ),
                    modifier = Modifier.testTag("switch_alarm_${alarm.id}")
                )
            }
        }
    }
}
