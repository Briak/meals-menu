package com.briak.mealsmenu.domain.categories

import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {

    suspend fun get(): List<CategoryModel>
    fun observe(): Flow<List<CategoryModel>>

}