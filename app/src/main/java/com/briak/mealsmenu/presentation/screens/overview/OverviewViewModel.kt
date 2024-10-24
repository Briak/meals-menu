package com.briak.mealsmenu.presentation.screens.overview

import com.briak.mealsmenu.domain.categories.CategoriesInteractor
import com.briak.mealsmenu.domain.meals.MealsInteractor
import com.briak.mealsmenu.presentation.common.BaseViewModel
import com.briak.mealsmenu.presentation.screens.overview.categories.CategoryUiModel
import com.briak.mealsmenu.presentation.screens.overview.meals.MealUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class OverviewViewModel(
    private val categoriesInteractor: CategoriesInteractor,
    private val mealsInteractor: MealsInteractor,
) : BaseViewModel() {
    private val stateFlow = MutableStateFlow(OverviewState())
    private val selectCategoryMutex = Mutex()

    val screenDataFlow = stateFlow.map { it.uiModel }.distinctUntilChanged()

    fun subscribe() {
        categoriesInteractor
            .observeCategories()
            .onEach { models ->
                if (stateFlow.value.categoryModels != null) return@onEach
                val state =
                    stateFlow.updateAndGet { it.copy(categoryModels = models, loading = false) }
                selectCategory(state.uiModel.categories.getOrNull(0))
            }.catch { error ->
                stateFlow.update { state -> state.copy(loading = false, categoriesError = error) }
            }.launchAndManageJob()
    }

    fun unsubscribe() {
        disposeJobs()
    }

    fun reloadCategories() {
    }

    fun selectCategory(category: CategoryUiModel?) =
        launch {
            try {
                val oldState = stateFlow.value
                if (category == null || category.id == oldState.selectedCategoryId) return@launch

                stateFlow.update { state ->
                    state.copy(
                        selectedCategoryId = category.id,
                        meals = emptyList(),
                        mealsLoading = true,
                    )
                }
                selectCategoryMutex.withLock {
                    // handle errors
                    val meals = mealsInteractor.getMealsForCategory(category.name)
                    stateFlow.update { state -> state.copy(meals = meals, mealsLoading = false) }
                }
            } catch (error: Throwable) {
                stateFlow.update { state -> state.copy(mealsLoading = false, mealsError = error) }
            }
        }

    fun reloadMeals() {
    }

    fun selectMeal(meal: MealUiModel) =
        launch {
            mealsInteractor.putCurrentMeal(meal.model)
        }
}
