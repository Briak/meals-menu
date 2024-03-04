package com.briak.mealsmenu.di

import com.google.gson.GsonBuilder
import org.koin.dsl.module

object CommonModule {

    val module = module {
        single {
            val gsonBuilder = GsonBuilder()
            val gson = gsonBuilder
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSZ")
                .create()
            gson
        }
    }

}