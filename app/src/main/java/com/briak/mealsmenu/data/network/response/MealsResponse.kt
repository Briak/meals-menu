package com.briak.mealsmenu.data.network.response

import com.briak.mealsmenu.data.network.dto.MealDto
import com.google.gson.annotations.SerializedName

data class MealsResponse(
    @SerializedName("meals") val meals: List<MealDto>,
)
