package com.briak.mealsmenu.data.network.response

import com.briak.mealsmenu.data.network.dto.CategoryDto
import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
    @SerializedName("categories") val categories: List<CategoryDto>,
)
