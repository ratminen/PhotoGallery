package com.example.photogallery.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePhotoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(photo: FavoritePhoto)

    @Query("SELECT * FROM favorite_photos")
    fun getAll(): Flow<List<FavoritePhoto>>

    @Query("DELETE FROM favorite_photos")
    suspend fun deleteAll()
}