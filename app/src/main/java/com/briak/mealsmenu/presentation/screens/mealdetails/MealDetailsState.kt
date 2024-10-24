package com.briak.mealsmenu.presentation.screens.mealdetails

import com.briak.mealsmenu.domain.meals.MealModel

data class MealDetailsState(
    val mealModel: MealModel? = null,
    val loading: Boolean = true,
    val error: Throwable? = null,
) {
    val uiModel: MealDetailsContentUiModel =
        kotlin.run {
            MealDetailsContentUiModel.create(
                mealModel = mealModel,
                loading = loading,
                error = error,
            )
        }
}
