package com.briak.mealsmenu.di

import com.briak.mealsmenu.presentation.screens.favourites.FavouritesViewModel
import com.briak.mealsmenu.presentation.screens.mealdetails.MealDetailsViewModel
import com.briak.mealsmenu.presentation.screens.overview.OverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule {
    val module =
        module {
            viewModel { OverviewViewModel(get(), get()) }
            viewModel { MealDetailsViewModel(get()) }
            viewModel { FavouritesViewModel(get()) }
        }
}
