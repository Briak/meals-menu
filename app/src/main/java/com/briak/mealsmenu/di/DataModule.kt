package com.briak.mealsmenu.di

import com.briak.mealsmenu.data.repository.categories.CategoriesRepositoryImpl
import com.briak.mealsmenu.data.repository.meals.MealsRepositoryImpl
import com.briak.mealsmenu.domain.categories.CategoriesRepository
import com.briak.mealsmenu.domain.meals.MealsRepository
import org.koin.dsl.module

object DataModule {

    val module = module {
        single<CategoriesRepository> { CategoriesRepositoryImpl(get()) }
        single<MealsRepository> { MealsRepositoryImpl(get()) }
    }

}