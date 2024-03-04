package com.briak.mealsmenu.domain.meals

data class MealModel(
    val id: String,
    val name: String,
    val iconUrl: String,
    val categoryName: String?,
    val areaName: String?,
    val instructions: String?,
    val tags: String?,
    val youtubeUrl: String?,
    val sourceUrl: String?,
    val ingredients: List<IngredientModel>?,
)
