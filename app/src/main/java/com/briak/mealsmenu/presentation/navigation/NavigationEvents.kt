package com.briak.mealsmenu.presentation.navigation

import com.briak.mealsmenu.R
import kotlinx.serialization.Serializable

// Bottom navigation root screens

@Serializable
object OverviewScreens

@Serializable
object FavouritesScreens

@Serializable
sealed class BottomScreens<T>(
    val name: String,
    val icon: Int,
    val route: T,
) {
    @Serializable
    data object Overview : BottomScreens<OverviewScreens>(
        name = "Overview",
        icon = R.drawable.ic_menu_book,
        route = OverviewScreens,
    )

    @Serializable
    data object Favourites : BottomScreens<FavouritesScreens>(
        name = "Favourites",
        icon = R.drawable.ic_favorite,
        route = FavouritesScreens,
    )
}

// Overview screens

@Serializable
object MealDetails

sealed class OverviewNavigationEvent {
    data object OnBackPressed : OverviewNavigationEvent()

    data object OnMealClicked : OverviewNavigationEvent()
}

// Favourites screens

sealed class FavouritesNavigationEvent {
    data object OnBackPressed : FavouritesNavigationEvent()
}
