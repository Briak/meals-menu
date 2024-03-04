package com.briak.mealsmenu.domain.categories

import kotlinx.coroutines.flow.Flow

class CategoriesInteractor(
    private val categoriesRepository: CategoriesRepository,
) {

    suspend fun getCategories(): List<CategoryModel> = categoriesRepository.get()

    fun observeCategories(): Flow<List<CategoryModel>> = categoriesRepository.observe()

}