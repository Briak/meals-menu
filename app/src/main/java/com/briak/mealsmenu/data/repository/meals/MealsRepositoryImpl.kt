package com.briak.mealsmenu.data.repository.meals

import com.briak.mealsmenu.data.network.MealsAPI
import com.briak.mealsmenu.data.storage.AppStorage
import com.briak.mealsmenu.domain.meals.MealModel
import com.briak.mealsmenu.domain.meals.MealNotFoundException
import com.briak.mealsmenu.domain.meals.MealsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MealsRepositoryImpl(
    private val api: MealsAPI,
    private val appStorage: AppStorage,
) : MealsRepository {
    private val currentMealFlow = MutableStateFlow<MealModel?>(null)
    private val currentMealMutex = Mutex()

    override suspend fun getForCategory(categoryName: String): List<MealModel> {
        val response = api.getCategoryMeals(categoryName)
        val dtos = response.meals
        return dtos.map { dto -> MealsMapper.mapMealFromDto(dto, false) }
    }

    override suspend fun getDetails(mealId: String): MealModel {
        val response = api.getMealDetails(mealId)
        val dto = response.meals.firstOrNull() ?: throw MealNotFoundException()
        val entity = appStorage.favouriteMealDao.getInternal(mealId)
        val favourite = entity != null
        return MealsMapper.mapMealFromDto(dto, favourite)
    }

    override suspend fun addToFavourites(model: MealModel) {
        val entity = MealsMapper.mapEntityFromMeal(model)
        appStorage.favouriteMealDao.putInternal(entity)
        currentMealFlow.update { meal -> meal?.copy(favourite = true) }
    }

    override suspend fun removeFromFavourites(mealId: String) {
        appStorage.favouriteMealDao.deleteInternal(mealId)
        currentMealFlow.update { meal -> meal?.copy(favourite = false) }
    }

    override fun observeFavourites(): Flow<List<MealModel>> =
        appStorage.favouriteMealDao
            .observeAll()
            .mapLatest { entities -> entities.map { entity -> MealsMapper.mapMealFromEntity(entity) } }
            .distinctUntilChanged()

    override suspend fun putCurrent(mealModel: MealModel) =
        currentMealMutex.withLock {
            currentMealFlow.emit(mealModel)
        }

    override fun observeCurrent(): Flow<MealModel?> = currentMealFlow

    override suspend fun getCurrent(): MealModel? = currentMealFlow.value
}
