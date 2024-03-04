package com.briak.mealsmenu

import android.app.Application
import com.briak.mealsmenu.di.CommonModule
import com.briak.mealsmenu.di.DataModule
import com.briak.mealsmenu.di.DomainModule
import com.briak.mealsmenu.di.NetworkModule
import com.briak.mealsmenu.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber


class MealsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        setupLogging()
        initKoin()
    }

    private fun setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@MealsApplication)
            modules(
                CommonModule.module,
                NetworkModule.module,
                DataModule.module,
                DomainModule.module,
                ViewModelModule.module,
            )
        }
    }
}