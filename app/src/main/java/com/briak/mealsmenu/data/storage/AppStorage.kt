package com.briak.mealsmenu.data.storage

import android.content.Context
import com.briak.mealsmenu.data.storage.db.GeneralRoom
import com.briak.mealsmenu.data.storage.db.GeneralRoomFactory

class AppStorage(
    context: Context,
) {
    private val room: GeneralRoom = GeneralRoomFactory.create(context)
    val favouriteMealDao = room.favoriteMealDao()

    fun clear() {
        room.clearAllTables()
    }
}
