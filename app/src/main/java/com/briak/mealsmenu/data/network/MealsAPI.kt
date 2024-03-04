package com.briak.mealsmenu.data.network

import com.briak.mealsmenu.data.network.response.CategoriesResponse
import com.briak.mealsmenu.data.network.response.MealsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MealsAPI {

    @GET("v1/1/categories.php")
    suspend fun getCategories(): CategoriesResponse

    @GET("v1/1/filter.php?c={category_name}")
    suspend fun getCategoryMeals(
        @Path("category_name") categoryName: String,
    ): MealsResponse

    @GET("v1/1/lookup.php?i={meal_id}")
    suspend fun getMealDetails(
        @Path("meal_id") mealId: String,
    ): MealsResponse
}