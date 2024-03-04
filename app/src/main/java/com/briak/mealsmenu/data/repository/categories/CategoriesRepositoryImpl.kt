package com.briak.mealsmenu.data.repository.categories

import com.briak.mealsmenu.data.network.MealsAPI
import com.briak.mealsmenu.domain.categories.CategoriesRepository
import com.briak.mealsmenu.domain.categories.CategoryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CategoriesRepositoryImpl(
    private val api: MealsAPI,
): CategoriesRepository {

    private val categoriesFlow = MutableStateFlow<List<CategoryModel>>(listOf())
    private val categoriesMutex = Mutex()

    override suspend fun get(): List<CategoryModel> {
        val response = api.getCategories()
        val dtos = response.categories
        val models = dtos.map { dto -> CategoriesMapper.mapFromDto(dto) }
        categoriesMutex.withLock { categoriesFlow.emit(models) }
        return models
    }

    override fun observe(): Flow<List<CategoryModel>> = categoriesFlow

}