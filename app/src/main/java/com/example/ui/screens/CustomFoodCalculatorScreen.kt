package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppLanguage
import com.example.data.model.FoodCategory
import com.example.data.model.SelectedFoodItem
import com.example.data.model.WholeFoodItem
import com.example.data.model.WholeFoodsRepository
import com.example.localization.LanguageManager
import com.example.ui.theme.AppTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFoodCalculatorScreen(
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val focusManager = LocalFocusManager.current

    var selectedCategory by remember { mutableStateOf(FoodCategory.ALL) }
    var searchQuery by remember { mutableStateOf("") }
    
    // Map of selected whole food IDs to their quantity in grams
    var selectedFoodItemsMap by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
    
    // Dialog state for editing portion
    var activePortionItem by remember { mutableStateOf<WholeFoodItem?>(null) }
    var portionInputText by remember { mutableStateOf("") }

    val allFoods = remember { WholeFoodsRepository.foods }

    // Filter foods by category and search
    val filteredFoods = remember(selectedCategory, searchQuery, language) {
        allFoods.filter { food ->
            val matchesCategory = when (selectedCategory) {
                FoodCategory.ALL -> true
                else -> food.category == selectedCategory
            }
            val matchesSearch = if (searchQuery.isBlank()) true else {
                val q = searchQuery.trim().lowercase()
                food.nameAr.lowercase().contains(q) ||
                food.nameEn.lowercase().contains(q) ||
                food.nameFr.lowercase().contains(q)
            }
            matchesCategory && matchesSearch
        }
    }

    // Convert map to SelectedFoodItem list for calculations
    val selectedList = remember(selectedFoodItemsMap) {
        selectedFoodItemsMap.mapNotNull { (id, grams) ->
            val food = allFoods.find { it.id == id }
            if (food != null && grams > 0) SelectedFoodItem(food, grams) else null
        }
    }

    // Dynamic Real-time Calculations
    val totalCalories = remember(selectedList) { selectedList.sumOf { it.calories } }
    val totalProtein = remember(selectedList) { selectedList.sumOf { it.protein.toDouble() }.toFloat() }
    val totalCarbs = remember(selectedList) { selectedList.sumOf { it.carbs.toDouble() }.toFloat() }
    val totalFat = remember(selectedList) { selectedList.sumOf { it.fat.toDouble() }.toFloat() }
    val totalGrams = remember(selectedList) { selectedList.sumOf { it.quantityGrams } }

    val totalMacroGrams = (totalProtein + totalCarbs + totalFat).coerceAtLeast(1f)
    val proteinPercent = ((totalProtein / totalMacroGrams) * 100).roundToInt()
    val carbsPercent = ((totalCarbs / totalMacroGrams) * 100).roundToInt()
    val fatPercent = ((totalFat / totalMacroGrams) * 100).roundToInt()

    // Portion editing dialog
    if (activePortionItem != null) {
        val food = activePortionItem!!
        val currentGrams = selectedFoodItemsMap[food.id] ?: food.defaultServingGrams

        Dialog(onDismissRequest = { activePortionItem = null }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, colors.primaryAccent.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                color = colors.cardBackgroundOpaque
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = food.emoji, fontSize = 42.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = food.localizedName(language),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${LanguageManager.adjustQuantity(language)} (${LanguageManager.gramsUnit(language)})",
                        fontSize = 13.sp,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Direct input field
                    OutlinedTextField(
                        value = portionInputText,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() } && input.length <= 4) {
                                portionInputText = input
                            }
                        },
                        label = { Text(LanguageManager.gramsUnit(language)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_portion_grams"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primaryAccent,
                            unfocusedBorderColor = colors.cardBorder,
                            focusedTextColor = colors.textPrimary,
                            unfocusedTextColor = colors.textPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quick portion chips (50g, 100g, 150g, 200g, 250g, 300g)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val quickChips = listOf(50, 100, 150, 200, 250, 300)
                        items(quickChips) { g ->
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        1.dp,
                                        if (portionInputText == g.toString()) colors.primaryAccent else colors.cardBorder,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { portionInputText = g.toString() },
                                color = if (portionInputText == g.toString()) colors.primaryAccent.copy(alpha = 0.2f) else colors.surface
                            ) {
                                Text(
                                    text = "$g ${LanguageManager.gramsUnit(language)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (portionInputText == g.toString()) colors.primaryAccent else colors.textPrimary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Live macros for this portion
                    val typedGrams = portionInputText.toIntOrNull() ?: currentGrams
                    val portionCals = ((food.caloriesPer100g * typedGrams) / 100f).toInt()
                    val portionP = (food.proteinPer100g * typedGrams) / 100f
                    val portionC = (food.carbsPer100g * typedGrams) / 100f
                    val portionF = (food.fatPer100g * typedGrams) / 100f

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp)),
                        color = colors.surface.copy(alpha = 0.7f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "$portionCals", fontWeight = FontWeight.Bold, color = colors.primaryAccent, fontSize = 14.sp)
                                Text(text = "kcal", fontSize = 10.sp, color = colors.textSecondary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "${String.format("%.1f", portionP)}g", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50), fontSize = 14.sp)
                                Text(text = LanguageManager.macroProtein(language), fontSize = 10.sp, color = colors.textSecondary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "${String.format("%.1f", portionC)}g", fontWeight = FontWeight.Bold, color = Color(0xFF2196F3), fontSize = 14.sp)
                                Text(text = LanguageManager.macroCarbs(language), fontSize = 10.sp, color = colors.textSecondary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "${String.format("%.1f", portionF)}g", fontWeight = FontWeight.Bold, color = Color(0xFFFF9800), fontSize = 14.sp)
                                Text(text = LanguageManager.macroFats(language), fontSize = 10.sp, color = colors.textSecondary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Remove button
                        OutlinedButton(
                            onClick = {
                                val newMap = selectedFoodItemsMap.toMutableMap()
                                newMap.remove(food.id)
                                selectedFoodItemsMap = newMap
                                activePortionItem = null
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935))
                        ) {
                            Text(text = "✕", fontWeight = FontWeight.Bold)
                        }

                        // Save portion button
                        Button(
                            onClick = {
                                val grams = portionInputText.toIntOrNull() ?: food.defaultServingGrams
                                if (grams > 0) {
                                    val newMap = selectedFoodItemsMap.toMutableMap()
                                    newMap[food.id] = grams
                                    selectedFoodItemsMap = newMap
                                }
                                activePortionItem = null
                            },
                            modifier = Modifier
                                .weight(2f)
                                .testTag("btn_confirm_portion"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.primaryAccent,
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = LanguageManager.tapToAddToPlate(language),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("custom_food_calculator_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. Header & Search
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = LanguageManager.customCalculatorTitle(language),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = colors.textPrimary
                )
                Text(
                    text = LanguageManager.customCalculatorSubtitle(language),
                    fontSize = 12.sp,
                    color = colors.textSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(LanguageManager.searchFoodPlaceholder(language), fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = colors.textSecondary)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = colors.textSecondary)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_search_food"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.primaryAccent,
                        unfocusedBorderColor = colors.cardBorder,
                        focusedContainerColor = colors.cardBackgroundOpaque,
                        unfocusedContainerColor = colors.cardBackgroundOpaque,
                        focusedTextColor = colors.textPrimary,
                        unfocusedTextColor = colors.textPrimary
                    )
                )
            }
        }

        // 2. Six Categorized Selection Chips
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val categories = listOf(
                    FoodCategory.ALL to LanguageManager.categoryAll(language),
                    FoodCategory.VEGETABLES to LanguageManager.categoryVegetables(language),
                    FoodCategory.FRUITS to LanguageManager.categoryFruits(language),
                    FoodCategory.MEAT_POULTRY to LanguageManager.categoryMeatPoultry(language),
                    FoodCategory.FISH_SEAFOOD to LanguageManager.categoryFishSeafood(language),
                    FoodCategory.GRAINS_LEGUMES to LanguageManager.categoryGrainsLegumes(language),
                    FoodCategory.SUPPLEMENTS_NUTS to LanguageManager.categorySupplementsNuts(language)
                )

                items(categories) { (cat, label) ->
                    val isSelected = selectedCategory == cat
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(
                                1.5.dp,
                                if (isSelected) colors.primaryAccent else colors.cardBorder,
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedCategory = cat }
                            .testTag("category_chip_${cat.name}"),
                        color = if (isSelected) colors.primaryAccent.copy(alpha = 0.18f) else colors.cardBackgroundOpaque
                    ) {
                        Text(
                            text = label,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) colors.primaryAccent else colors.textPrimary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // 3. Selectable Whole Foods Cards
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                filteredFoods.forEach { food ->
                    val isSelected = selectedFoodItemsMap.containsKey(food.id)
                    val currentGrams = selectedFoodItemsMap[food.id] ?: 0

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .border(
                                if (isSelected) 1.5.dp else 1.dp,
                                if (isSelected) colors.primaryAccent else colors.cardBorder,
                                RoundedCornerShape(18.dp)
                            )
                            .clickable {
                                activePortionItem = food
                                portionInputText = (selectedFoodItemsMap[food.id] ?: food.defaultServingGrams).toString()
                            }
                            .testTag("food_item_${food.id}"),
                        color = if (isSelected) colors.primaryAccent.copy(alpha = 0.08f) else colors.cardBackgroundOpaque
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Food Emoji & Portion Badge
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) colors.primaryAccent.copy(alpha = 0.2f) else colors.surface),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = food.emoji, fontSize = 26.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Details
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = food.localizedName(language),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colors.textPrimary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )

                                    if (isSelected) {
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = colors.primaryAccent
                                        ) {
                                            Text(
                                                text = "$currentGrams ${LanguageManager.gramsUnit(language)}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color.Black,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = food.localizedBenefit(language),
                                    fontSize = 11.sp,
                                    color = colors.textSecondary,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(top = 2.dp)
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // Macros per 100g
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${food.caloriesPer100g} kcal",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colors.primaryAccent
                                    )
                                    Text(
                                        text = "P: ${food.proteinPer100g}g",
                                        fontSize = 11.sp,
                                        color = Color(0xFF4CAF50),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "C: ${food.carbsPer100g}g",
                                        fontSize = 11.sp,
                                        color = Color(0xFF2196F3),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "F: ${food.fatPer100g}g",
                                        fontSize = 11.sp,
                                        color = Color(0xFFFF9800),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Add / Check Icon Button
                            IconButton(
                                onClick = {
                                    activePortionItem = food
                                    portionInputText = (selectedFoodItemsMap[food.id] ?: food.defaultServingGrams).toString()
                                }
                            ) {
                                Icon(
                                    imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.AddCircleOutline,
                                    contentDescription = "Select",
                                    tint = if (isSelected) colors.primaryAccent else colors.textSecondary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. "My Custom Plate" Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = LanguageManager.myMealPlate(language),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )

                    if (selectedList.isNotEmpty()) {
                        TextButton(
                            onClick = { selectedFoodItemsMap = emptyMap() },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFE53935))
                        ) {
                            Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = LanguageManager.clearPlate(language), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                if (selectedList.isEmpty()) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                        color = colors.cardBackgroundOpaque
                    ) {
                        Text(
                            text = LanguageManager.noFoodSelected(language),
                            fontSize = 12.sp,
                            color = colors.textSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                } else {
                    selectedList.forEach { item ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .border(1.dp, colors.cardBorder, RoundedCornerShape(14.dp)),
                            color = colors.cardBackgroundOpaque
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = item.foodItem.emoji, fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.foodItem.localizedName(language),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colors.textPrimary
                                    )
                                    Text(
                                        text = "${item.calories} kcal | P: ${String.format("%.1f", item.protein)}g | C: ${String.format("%.1f", item.carbs)}g | F: ${String.format("%.1f", item.fat)}g",
                                        fontSize = 11.sp,
                                        color = colors.textSecondary
                                    )
                                }

                                // Quick increment/decrement buttons
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = {
                                            val newGrams = (item.quantityGrams - 25).coerceAtLeast(0)
                                            val newMap = selectedFoodItemsMap.toMutableMap()
                                            if (newGrams <= 0) newMap.remove(item.foodItem.id) else newMap[item.foodItem.id] = newGrams
                                            selectedFoodItemsMap = newMap
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Text(text = "–", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = colors.primaryAccent)
                                    }

                                    Text(
                                        text = "${item.quantityGrams}g",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colors.textPrimary,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )

                                    IconButton(
                                        onClick = {
                                            val newGrams = item.quantityGrams + 25
                                            val newMap = selectedFoodItemsMap.toMutableMap()
                                            newMap[item.foodItem.id] = newGrams
                                            selectedFoodItemsMap = newMap
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Text(text = "+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = colors.primaryAccent)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Dynamic Real-Time Total Calories & Macro Summary Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.5.dp, colors.primaryAccent.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
                    .testTag("summary_macro_card"),
                color = colors.cardBackgroundOpaque
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = LanguageManager.foodSummaryHeader(language),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = colors.textPrimary
                        )

                        Text(
                            text = LanguageManager.totalWeightSelected(totalGrams, language),
                            fontSize = 11.sp,
                            color = colors.primaryAccent,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Total Calories Big Banner
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        color = colors.primaryAccent.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = LanguageManager.totalMealCalories(language),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "$totalCalories",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Black,
                                    color = colors.primaryAccent
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = LanguageManager.caloriesUnit(language),
                                    fontSize = 12.sp,
                                    color = colors.textSecondary,
                                    modifier = Modifier.padding(bottom = 3.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Macro distribution percentage bar
                    Text(
                        text = LanguageManager.macroBreakdown(language),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textSecondary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(CircleShape)
                            .background(colors.surface)
                    ) {
                        if (totalMacroGrams > 1f) {
                            Box(
                                modifier = Modifier
                                    .weight(proteinPercent.coerceAtLeast(1).toFloat())
                                    .fillMaxHeight()
                                    .background(Color(0xFF4CAF50))
                            )
                            Box(
                                modifier = Modifier
                                    .weight(carbsPercent.coerceAtLeast(1).toFloat())
                                    .fillMaxHeight()
                                    .background(Color(0xFF2196F3))
                            )
                            Box(
                                modifier = Modifier
                                    .weight(fatPercent.coerceAtLeast(1).toFloat())
                                    .fillMaxHeight()
                                    .background(Color(0xFFFF9800))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 3 Macro metric boxes
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Protein
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .border(1.dp, Color(0xFF4CAF50).copy(alpha = 0.3f), RoundedCornerShape(14.dp)),
                            color = Color(0xFF4CAF50).copy(alpha = 0.1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = LanguageManager.macroProtein(language),
                                    fontSize = 11.sp,
                                    color = colors.textSecondary
                                )
                                Text(
                                    text = "${String.format("%.1f", totalProtein)}g",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF4CAF50)
                                )
                                Text(
                                    text = "$proteinPercent%",
                                    fontSize = 10.sp,
                                    color = colors.textSecondary
                                )
                            }
                        }

                        // Carbs
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .border(1.dp, Color(0xFF2196F3).copy(alpha = 0.3f), RoundedCornerShape(14.dp)),
                            color = Color(0xFF2196F3).copy(alpha = 0.1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = LanguageManager.macroCarbs(language),
                                    fontSize = 11.sp,
                                    color = colors.textSecondary
                                )
                                Text(
                                    text = "${String.format("%.1f", totalCarbs)}g",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF2196F3)
                                )
                                Text(
                                    text = "$carbsPercent%",
                                    fontSize = 10.sp,
                                    color = colors.textSecondary
                                )
                            }
                        }

                        // Fats
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .border(1.dp, Color(0xFFFF9800).copy(alpha = 0.3f), RoundedCornerShape(14.dp)),
                            color = Color(0xFFFF9800).copy(alpha = 0.1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = LanguageManager.macroFats(language),
                                    fontSize = 11.sp,
                                    color = colors.textSecondary
                                )
                                Text(
                                    text = "${String.format("%.1f", totalFat)}g",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFFF9800)
                                )
                                Text(
                                    text = "$fatPercent%",
                                    fontSize = 10.sp,
                                    color = colors.textSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
