package com.briak.mealsmenu.domain.meals

data class MealModel(
    val id: String,
    val name: String,
    val iconUrl: String,
    val categoryName: String? = null,
    val areaName: String? = null,
    val instructions: String? = null,
    val tags: String? = null,
    val youtubeUrl: String? = null,
    val sourceUrl: String? = null,
    val ingredients: List<IngredientModel>? = null,
    val favourite: Boolean = false,
)
