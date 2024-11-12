package com.briak.mealsmenu.presentation.screens.favourites

import com.briak.mealsmenu.domain.meals.MealModel

data class FavouritesState(
    val meals: List<MealModel>? = null,
    val loading: Boolean = true,
    val error: Throwable? = null,
) {
    val uiModel: FavouritesContentUiModel =
        kotlin.run {
            FavouritesContentUiModel.create(
                mealModels = meals ?: emptyList(),
                loading = loading,
                error = error,
            )
        }
}
