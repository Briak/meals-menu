package com.briak.mealsmenu.di

import com.briak.mealsmenu.domain.categories.CategoriesInteractor
import com.briak.mealsmenu.domain.meals.MealsInteractor
import org.koin.dsl.module

object DomainModule {

    val module = module {
        single { CategoriesInteractor(get()) }
        single { MealsInteractor(get()) }
    }

}