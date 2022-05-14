package com.hellguy39.data.dao

import androidx.room.*
import com.hellguy39.data.models.RadioStationEntity

@Dao
interface RadioStationsDao {

    @Query("SELECT * FROM RadioStationEntity")
    suspend fun getAllRadioStations(): List<RadioStationEntity>

    @Query("SELECT * FROM RadioStationEntity WHERE name LIKE '%' || :query || '%' ")
    suspend fun getRadioStationsWithQuery(query: String = ""): List<RadioStationEntity>

    @Query("SELECT * FROM RadioStationEntity WHERE id = :id")
    fun getRadioStationById(id: Int): RadioStationEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRadioStation(radioStationDb: RadioStationEntity)

    @Update
    suspend fun updateRadioStation(radioStationDb: RadioStationEntity)

    @Delete
    suspend fun deleteRadioStations(radioStationDb: RadioStationEntity)

}