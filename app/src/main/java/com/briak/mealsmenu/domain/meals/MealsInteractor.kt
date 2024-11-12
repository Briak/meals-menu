package com.briak.mealsmenu.domain.meals

import kotlinx.coroutines.flow.Flow

class MealsInteractor(
    private val mealsRepository: MealsRepository,
) {
    suspend fun getMealsForCategory(categoryName: String) = mealsRepository.getForCategory(categoryName)

    suspend fun getMealDetails(mealId: String): MealModel = mealsRepository.getDetails(mealId)

    suspend fun addMealToFavourites(mealModel: MealModel) = mealsRepository.addToFavourites(mealModel)

    suspend fun removeMealFromFavourites(mealId: String) = mealsRepository.removeFromFavourites(mealId)

    fun observeFavouriteMeals(): Flow<List<MealModel>> = mealsRepository.observeFavourites()

    suspend fun putCurrentMeal(mealModel: MealModel) = mealsRepository.putCurrent(mealModel)

    fun observeCurrentMeal(): Flow<MealModel?> = mealsRepository.observeCurrent()

    suspend fun getCurrentMeal(): MealModel? = mealsRepository.getCurrent()
}
