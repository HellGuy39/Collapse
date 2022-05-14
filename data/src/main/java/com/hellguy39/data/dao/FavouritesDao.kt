package com.hellguy39.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hellguy39.data.models.TrackEntity

@Dao
interface FavouritesDao {

    @Query("SELECT * FROM TrackEntity")
    suspend fun getAllFavouriteTracks(): List<TrackEntity>

    @Insert
    suspend fun insertFavouriteTrack(trackEntity: TrackEntity)

    @Delete
    suspend fun deleteFavouriteTrack(trackEntity: TrackEntity)

}