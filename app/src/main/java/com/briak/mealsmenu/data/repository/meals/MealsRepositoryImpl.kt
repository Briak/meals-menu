package com.briak.mealsmenu.data.repository.meals

import com.briak.mealsmenu.data.network.MealsAPI
import com.briak.mealsmenu.domain.meals.MealModel
import com.briak.mealsmenu.domain.meals.MealsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MealsRepositoryImpl(
    private val api: MealsAPI,
) : MealsRepository {

    private val currentMealFlow = MutableStateFlow<MealModel?>(null)
    private val currentMealMutex = Mutex()

    override suspend fun getForCategory(categoryName: String): List<MealModel> {
        val response = api.getCategoryMeals(categoryName)
        val dtos = response.meals
        return dtos.map { dto -> MealsMapper.mapMealFromDto(dto) }
    }

    override suspend fun getDetails(mealId: String): MealModel {
        val response = api.getMealDetails(mealId)
        val dto = response.meals.firstOrNull() ?: throw RuntimeException("Meal isn't found")
        return MealsMapper.mapMealFromDto(dto)
    }

    override suspend fun putCurrent(mealModel: MealModel) = currentMealMutex.withLock {
        currentMealFlow.emit(mealModel)
    }

    override fun observeCurrent(): Flow<MealModel?> = currentMealFlow

}