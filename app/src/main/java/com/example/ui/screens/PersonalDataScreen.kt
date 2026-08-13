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
import com.example.ui.theme.BrightNeonGreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkEmeraldCardBorder
import com.example.ui.theme.NeonGreenAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryWhite
import com.example.ui.theme.TextSecondaryGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen(
    userProfile: UserProfile,
    onSavePersonalData: (Gender, Int, Float, Float) -> Unit,
    onNextClick: () -> Unit
) {
    val lang = userProfile.language

    var selectedGender by remember { mutableStateOf(userProfile.gender) }
    var ageText by remember { mutableStateOf(if (userProfile.age > 0) userProfile.age.toString() else "25") }
    var heightText by remember { mutableStateOf(if (userProfile.heightCm > 0) userProfile.heightCm.toInt().toString() else "175") }
    var weightText by remember { mutableStateOf(if (userProfile.weightKg > 0) userProfile.weightKg.toInt().toString() else "75") }

    val ageVal = ageText.toIntOrNull() ?: 0
    val heightVal = heightText.toFloatOrNull() ?: 0f
    val weightVal = weightText.toFloatOrNull() ?: 0f

    val isFormComplete = ageVal in 10..120 && heightVal in 100f..250f && weightVal in 30f..300f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
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
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = LanguageManager.personalDataTitle(lang),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrightNeonGreen
                )

                Text(
                    text = "خطوة 1 من 3 - أدخل بياناتك بدقة لحساب السعرات",
                    fontSize = 13.sp,
                    color = TextSecondaryGray,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                // Gender Selection
                Text(
                    text = LanguageManager.genderLabel(lang),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimaryWhite,
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
                                if (isMale) BrightNeonGreen else DarkEmeraldCardBorder,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { selectedGender = Gender.MALE }
                            .testTag("gender_male"),
                        color = if (isMale) DarkEmeraldCard else DarkEmeraldCard.copy(alpha = 0.4f)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Male,
                                contentDescription = "Male",
                                tint = if (isMale) BrightNeonGreen else TextSecondaryGray
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = LanguageManager.maleLabel(lang),
                                fontSize = 16.sp,
                                fontWeight = if (isMale) FontWeight.Bold else FontWeight.Normal,
                                color = TextPrimaryWhite
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
                                if (isFemale) BrightNeonGreen else DarkEmeraldCardBorder,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { selectedGender = Gender.FEMALE }
                            .testTag("gender_female"),
                        color = if (isFemale) DarkEmeraldCard else DarkEmeraldCard.copy(alpha = 0.4f)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Female,
                                contentDescription = "Female",
                                tint = if (isFemale) BrightNeonGreen else TextSecondaryGray
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = LanguageManager.femaleLabel(lang),
                                fontSize = 16.sp,
                                fontWeight = if (isFemale) FontWeight.Bold else FontWeight.Normal,
                                color = TextPrimaryWhite
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
                    color = TextPrimaryWhite,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                OutlinedTextField(
                    value = ageText,
                    onValueChange = { if (it.length <= 3 && it.all { char -> char.isDigit() }) ageText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_age"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkEmeraldCard,
                        unfocusedContainerColor = DarkEmeraldCard.copy(alpha = 0.5f),
                        focusedBorderColor = BrightNeonGreen,
                        unfocusedBorderColor = DarkEmeraldCardBorder,
                        focusedTextColor = TextPrimaryWhite,
                        unfocusedTextColor = TextPrimaryWhite
                    ),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = BrightNeonGreen)
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Height Input
                Text(
                    text = LanguageManager.heightLabel(lang),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimaryWhite,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                OutlinedTextField(
                    value = heightText,
                    onValueChange = { if (it.length <= 3 && it.all { char -> char.isDigit() }) heightText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_height"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkEmeraldCard,
                        unfocusedContainerColor = DarkEmeraldCard.copy(alpha = 0.5f),
                        focusedBorderColor = BrightNeonGreen,
                        unfocusedBorderColor = DarkEmeraldCardBorder,
                        focusedTextColor = TextPrimaryWhite,
                        unfocusedTextColor = TextPrimaryWhite
                    ),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Height, contentDescription = null, tint = BrightNeonGreen)
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Weight Input
                Text(
                    text = LanguageManager.weightLabel(lang),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimaryWhite,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                OutlinedTextField(
                    value = weightText,
                    onValueChange = { if (it.length <= 3 && it.all { char -> char.isDigit() }) weightText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_weight"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkEmeraldCard,
                        unfocusedContainerColor = DarkEmeraldCard.copy(alpha = 0.5f),
                        focusedBorderColor = BrightNeonGreen,
                        unfocusedBorderColor = DarkEmeraldCardBorder,
                        focusedTextColor = TextPrimaryWhite,
                        unfocusedTextColor = TextPrimaryWhite
                    ),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.MonitorWeight, contentDescription = null, tint = BrightNeonGreen)
                    },
                    singleLine = true
                )

                if (!isFormComplete) {
                    Text(
                        text = LanguageManager.fillAllDataNotice(lang),
                        fontSize = 13.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (isFormComplete) {
                        onSavePersonalData(selectedGender, ageVal, heightVal, weightVal)
                        onNextClick()
                    }
                },
                enabled = isFormComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("personal_data_next_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonGreenAccent,
                    contentColor = DarkBackground,
                    disabledContainerColor = DarkEmeraldCardBorder,
                    disabledContentColor = TextMuted
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = LanguageManager.btnNext(lang),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
