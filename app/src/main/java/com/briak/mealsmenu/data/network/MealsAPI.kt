package com.briak.mealsmenu.data.network

import com.briak.mealsmenu.data.network.response.CategoriesResponse
import com.briak.mealsmenu.data.network.response.MealsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealsAPI {
    @GET("v1/1/categories.php")
    suspend fun getCategories(): CategoriesResponse

    @GET("v1/1/filter.php")
    suspend fun getCategoryMeals(
        @Query("c") categoryName: String,
    ): MealsResponse

    @GET("v1/1/lookup.php")
    suspend fun getMealDetails(
        @Query("i") mealId: String,
    ): MealsResponse
}
