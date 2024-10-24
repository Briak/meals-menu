package com.briak.mealsmenu.presentation.screens.mealdetails

import com.briak.mealsmenu.domain.meals.MealNotFoundException
import com.briak.mealsmenu.domain.meals.MealsInteractor
import com.briak.mealsmenu.presentation.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class MealDetailsViewModel(
    private val mealsInteractor: MealsInteractor,
) : BaseViewModel() {
    private val stateFlow = MutableStateFlow(MealDetailsState())

    val screenDataFlow = stateFlow.map { it.uiModel }.distinctUntilChanged()

    fun subscribe() {
        mealsInteractor
            .observeCurrentMeal()
            .onEach { meal ->
                if (meal != null) loadMealDetails(meal.id)
            }.launchAndManageJob()
    }

    fun unsubscribe() {
        disposeJobs()
    }

    fun reload() =
        launch {
            try {
                val mealId = mealsInteractor.getCurrentMeal()?.id ?: throw MealNotFoundException()
                stateFlow.update { state -> state.copy(loading = true, error = null) }
                loadMealDetails(mealId)
            } catch (error: Throwable) {
                stateFlow.update { state -> state.copy(loading = false, error = error) }
            }
        }

    private fun loadMealDetails(mealId: String) =
        launch {
            try {
                val meal = mealsInteractor.getMealDetails(mealId)
                stateFlow.update { state ->
                    state.copy(
                        mealModel = meal,
                        loading = false,
                        error = null,
                    )
                }
            } catch (error: Throwable) {
                stateFlow.update { state -> state.copy(loading = false, error = error) }
            }
        }
}
