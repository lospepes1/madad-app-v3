package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.Gender
import com.example.data.model.UserProfile
import com.example.localization.LanguageManager
import com.example.ui.components.InstagramIconButton
import com.example.ui.components.ThemeToggleButton
import com.example.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen(
    userProfile: UserProfile,
    isDarkMode: Boolean = true,
    onToggleDarkMode: () -> Unit = {},
    onSavePersonalData: (Gender, Int, Float, Float) -> Unit,
    onNextClick: () -> Unit
) {
    val lang = userProfile.language
    val colors = AppTheme.colors

    var selectedGender by remember {
        mutableStateOf<Gender?>(if (userProfile.age > 0) userProfile.gender else null)
    }
    var ageText by remember {
        mutableStateOf(if (userProfile.age > 0) userProfile.age.toString() else "")
    }
    var heightText by remember {
        mutableStateOf(if (userProfile.heightCm > 0) userProfile.heightCm.toInt().toString() else "")
    }
    var weightText by remember {
        mutableStateOf(if (userProfile.weightKg > 0) userProfile.weightKg.toInt().toString() else "")
    }

    val ageVal = ageText.toIntOrNull() ?: 0
    val heightVal = heightText.toFloatOrNull() ?: 0f
    val weightVal = weightText.toFloatOrNull() ?: 0f

    val isGenderSelected = selectedGender != null
    val isAgeValid = ageVal in 10..120
    val isHeightValid = heightVal in 100f..250f
    val isWeightValid = weightVal in 30f..300f

    val isFormComplete = isGenderSelected && isAgeValid && isHeightValid && isWeightValid

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(20.dp)
            .testTag("personal_data_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Top Bar: Theme Toggle + Instagram Profile Quick Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ThemeToggleButton(
                        isDark = isDarkMode,
                        onToggle = onToggleDarkMode,
                        language = lang,
                        compact = true
                    )

                    InstagramIconButton()
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = LanguageManager.personalDataTitle(lang),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.primaryAccent
                )

                Text(
                    text = "خطوة 1 من 3 - جميع الحقول إجبارية لحساب السعرات والتمارين بدقة",
                    fontSize = 13.sp,
                    color = colors.textSecondary,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                // Gender Selection
                Text(
                    text = LanguageManager.genderRequiredLabel(lang),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val isMale = selectedGender == Gender.MALE
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                1.5.dp,
                                if (isMale) colors.primaryAccent else colors.cardBorder,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { selectedGender = Gender.MALE }
                            .testTag("gender_male"),
                        color = if (isMale) colors.cardBackgroundOpaque else colors.cardBackground
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Male,
                                contentDescription = "Male",
                                tint = if (isMale) colors.primaryAccent else colors.textSecondary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = LanguageManager.maleLabel(lang),
                                fontSize = 16.sp,
                                fontWeight = if (isMale) FontWeight.Bold else FontWeight.Normal,
                                color = colors.textPrimary
                            )
                        }
                    }

                    val isFemale = selectedGender == Gender.FEMALE
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                1.5.dp,
                                if (isFemale) colors.primaryAccent else colors.cardBorder,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { selectedGender = Gender.FEMALE }
                            .testTag("gender_female"),
                        color = if (isFemale) colors.cardBackgroundOpaque else colors.cardBackground
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Female,
                                contentDescription = "Female",
                                tint = if (isFemale) colors.primaryAccent else colors.textSecondary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = LanguageManager.femaleLabel(lang),
                                fontSize = 16.sp,
                                fontWeight = if (isFemale) FontWeight.Bold else FontWeight.Normal,
                                color = colors.textPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Age Input
                Text(
                    text = LanguageManager.ageLabel(lang),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                OutlinedTextField(
                    value = ageText,
                    onValueChange = { if (it.length <= 3 && it.all { char -> char.isDigit() }) ageText = it },
                    placeholder = {
                        Text(
                            text = LanguageManager.agePlaceholder(lang),
                            color = colors.textMuted,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_age"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colors.cardBackgroundOpaque,
                        unfocusedContainerColor = colors.cardBackground,
                        focusedBorderColor = colors.primaryAccent,
                        unfocusedBorderColor = colors.cardBorder,
                        focusedTextColor = colors.textPrimary,
                        unfocusedTextColor = colors.textPrimary
                    ),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = colors.primaryAccent)
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Height Input
                Text(
                    text = LanguageManager.heightLabel(lang),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                OutlinedTextField(
                    value = heightText,
                    onValueChange = { if (it.length <= 3 && it.all { char -> char.isDigit() }) heightText = it },
                    placeholder = {
                        Text(
                            text = LanguageManager.heightPlaceholder(lang),
                            color = colors.textMuted,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_height"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colors.cardBackgroundOpaque,
                        unfocusedContainerColor = colors.cardBackground,
                        focusedBorderColor = colors.primaryAccent,
                        unfocusedBorderColor = colors.cardBorder,
                        focusedTextColor = colors.textPrimary,
                        unfocusedTextColor = colors.textPrimary
                    ),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Height, contentDescription = null, tint = colors.primaryAccent)
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Weight Input
                Text(
                    text = LanguageManager.weightLabel(lang),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                OutlinedTextField(
                    value = weightText,
                    onValueChange = { if (it.length <= 3 && it.all { char -> char.isDigit() }) weightText = it },
                    placeholder = {
                        Text(
                            text = LanguageManager.weightPlaceholder(lang),
                            color = colors.textMuted,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_weight"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colors.cardBackgroundOpaque,
                        unfocusedContainerColor = colors.cardBackground,
                        focusedBorderColor = colors.primaryAccent,
                        unfocusedBorderColor = colors.cardBorder,
                        focusedTextColor = colors.textPrimary,
                        unfocusedTextColor = colors.textPrimary
                    ),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.MonitorWeight, contentDescription = null, tint = colors.primaryAccent)
                    },
                    singleLine = true
                )

                if (!isFormComplete) {
                    Text(
                        text = LanguageManager.fillAllDataNotice(lang),
                        fontSize = 13.sp,
                        color = colors.textMuted,
                        modifier = Modifier.padding(top = 14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (isFormComplete && selectedGender != null) {
                        onSavePersonalData(selectedGender!!, ageVal, heightVal, weightVal)
                        onNextClick()
                    }
                },
                enabled = isFormComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("personal_data_next_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primaryAccent,
                    contentColor = if (isDarkMode) Color.Black else Color.White,
                    disabledContainerColor = colors.cardBorder,
                    disabledContentColor = colors.textMuted
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = LanguageManager.btnNext(lang),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
