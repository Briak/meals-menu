package com.briak.mealsmenu.domain.meals

import kotlinx.coroutines.flow.Flow

class MealsInteractor(
    private val mealsRepository: MealsRepository,
) {

    suspend fun getMealsForCategory(categoryName: String): List<MealModel> =
        mealsRepository.getForCategory(categoryName)

    suspend fun getMealDetails(mealId: String): MealModel = mealsRepository.getDetails(mealId)

    suspend fun putCurrentMeal(mealModel: MealModel) = mealsRepository.putCurrent(mealModel)

    fun observeCurrentMeal(): Flow<MealModel?> = mealsRepository.observeCurrent()

}