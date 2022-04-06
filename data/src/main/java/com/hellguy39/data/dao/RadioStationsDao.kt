package com.hellguy39.data.dao

import androidx.room.*
import com.hellguy39.data.models.RadioStationDb

@Dao
interface RadioStationsDao {

    @Query("SELECT * FROM RadioStationDb")
    suspend fun getAllRadioStations(): List<RadioStationDb>

    @Query("SELECT * FROM RadioStationDb WHERE name LIKE '%' || :query || '%' ")
    suspend fun getRadioStationsWithQuery(query: String = ""): List<RadioStationDb>

    @Query("SELECT * FROM RadioStationDb WHERE id = :id")
    fun getRadioStationById(id: Int): RadioStationDb

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRadioStation(radioStationDb: RadioStationDb)

    @Update
    suspend fun updateRadioStation(radioStationDb: RadioStationDb)

    @Delete
    suspend fun deleteRadioStations(radioStationDb: RadioStationDb)

}