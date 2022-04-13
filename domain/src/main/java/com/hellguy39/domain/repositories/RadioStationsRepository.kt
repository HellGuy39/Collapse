package com.hellguy39.domain.repositories

import com.hellguy39.domain.models.RadioStation

interface RadioStationsRepository {

    suspend fun getAllRadioStations(): List<RadioStation>

    suspend fun getAllRadioStationsWithQuery(query: String = ""): List<RadioStation>

    suspend fun getRadioStationById(id: Int): RadioStation

    suspend fun insertRadioStation(radioStation: RadioStation)

    suspend fun updateRadioStation(radioStation: RadioStation)

    suspend fun deleteRadioStations(radioStation: RadioStation)
}