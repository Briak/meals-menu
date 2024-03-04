package com.briak.mealsmenu.domain.meals

import kotlinx.coroutines.flow.Flow

interface MealsRepository {

    suspend fun getForCategory(categoryName: String): List<MealModel>
    suspend fun getDetails(mealId: String): MealModel
    suspend fun putCurrent(mealModel: MealModel)
    fun observeCurrent(): Flow<MealModel?>

}