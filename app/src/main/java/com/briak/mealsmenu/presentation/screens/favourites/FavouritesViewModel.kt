package com.briak.mealsmenu.presentation.screens.favourites

import com.briak.mealsmenu.domain.meals.MealsInteractor
import com.briak.mealsmenu.presentation.common.BaseViewModel
import com.briak.mealsmenu.presentation.screens.overview.meals.MealUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class FavouritesViewModel(
    private val mealsInteractor: MealsInteractor,
) : BaseViewModel() {
    private val stateFlow = MutableStateFlow(FavouritesState())

    val screenDataFlow = stateFlow.map { it.uiModel }.distinctUntilChanged()

    fun subscribe() {
        mealsInteractor
            .observeFavouriteMeals()
            .onEach { models ->
                stateFlow.update { it.copy(meals = models, loading = false) }
            }.catch { error ->
                stateFlow.update { state -> state.copy(loading = false, error = error) }
            }.launchAndManageJob()
    }

    fun unsubscribe() {
        disposeJobs()
    }

    fun reload() =
        launch {
            try {
                stateFlow.update { state -> state.copy(error = null, loading = true) }
                // TODO update state
            } catch (error: Throwable) {
                stateFlow.update { state -> state.copy(loading = false, error = error) }
            }
        }

    fun selectMeal(meal: MealUiModel) =
        launch {
            mealsInteractor.putCurrentMeal(meal.model)
        }
}
