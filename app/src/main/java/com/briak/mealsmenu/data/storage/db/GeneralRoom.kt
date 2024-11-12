package com.briak.mealsmenu.data.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.briak.mealsmenu.data.storage.dao.FavouriteMealDao
import com.briak.mealsmenu.data.storage.entity.FavouriteMealEntity

@Database(
    entities = [
        FavouriteMealEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class GeneralRoom : RoomDatabase() {
    abstract fun favoriteMealDao(): FavouriteMealDao
}
