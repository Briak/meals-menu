package com.briak.mealsmenu.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable object Overview

@Serializable object MealDetails

sealed class OverviewNavigationEvent {
    data object OnBackPressed : OverviewNavigationEvent()

    data object OnMealClicked : OverviewNavigationEvent()
}
