package com.briak.mealsmenu.data.storage.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(
    tableName = "favourite_meals",
)
data class FavouriteMealEntity(
    @PrimaryKey val id: String,
    val name: String,
    val iconUrl: String,
)
