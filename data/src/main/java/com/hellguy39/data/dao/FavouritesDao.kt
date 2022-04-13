package com.hellguy39.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hellguy39.data.models.TrackDb

@Dao
interface FavouritesDao {

    @Query("SELECT * FROM TrackDb")
    suspend fun getAllFavouriteTracks(): List<TrackDb>

    @Insert
    suspend fun insertFavouriteTrack(trackDb: TrackDb)

    @Delete
    suspend fun deleteFavouriteTrack(trackDb: TrackDb)

}