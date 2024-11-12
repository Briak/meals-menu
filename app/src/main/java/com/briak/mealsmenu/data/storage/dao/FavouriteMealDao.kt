package com.briak.mealsmenu.data.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.briak.mealsmenu.data.storage.entity.FavouriteMealEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FavouriteMealDao {
    @Query("SELECT * FROM favourite_meals")
    abstract fun observeAll(): Flow<List<FavouriteMealEntity>>

    suspend fun clear() {
        deleteAll()
    }

    @Query("SELECT * FROM favourite_meals WHERE id = :id")
    abstract suspend fun getInternal(id: String): FavouriteMealEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun putInternal(entity: FavouriteMealEntity): Long

    @Query("DELETE FROM favourite_meals WHERE id = :id")
    abstract suspend fun deleteInternal(id: String)

    @Query("DELETE FROM favourite_meals")
    protected abstract suspend fun deleteAll()
}
