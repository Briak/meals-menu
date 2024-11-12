package com.briak.mealsmenu.data.storage.db

import android.content.Context
import androidx.room.Room

object GeneralRoomFactory {
    fun create(context: Context): GeneralRoom =
        Room
            .databaseBuilder(context, GeneralRoom::class.java, "general.db")
            .fallbackToDestructiveMigration()
            .build()
}
