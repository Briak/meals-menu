package com.briak.mealsmenu.presentation.screens.overview

import com.briak.mealsmenu.domain.categories.CategoryModel
import com.briak.mealsmenu.domain.meals.MealModel

data class OverviewState(
    val categoryModels: List<CategoryModel>? = null,
    val selectedCategoryId: String? = null,
    val meals: List<MealModel>? = null,
    val loading: Boolean = true,
    val mealsLoading: Boolean = false,
    val categoriesError: Throwable? = null,
    val mealsError: Throwable? = null,
) {
    val uiModel: OverviewContentUiModel =
        kotlin.run {
            OverviewContentUiModel.create(
                categoryModels = categoryModels ?: emptyList(),
                selectedCategoryId = selectedCategoryId ?: categoryModels?.getOrNull(0)?.id,
                mealModels = meals ?: emptyList(),
                loading = loading,
                mealsLoading = mealsLoading,
                categoriesError = categoriesError,
                mealsError = mealsError,
            )
        }
}
