package com.hellguy39.data.repositories

import com.hellguy39.data.dao.RadioStationsDao
import com.hellguy39.data.mappers.toRadioStation
import com.hellguy39.data.mappers.toRadioStationEntity
import com.hellguy39.data.mappers.toRadioStationList
import com.hellguy39.data.models.RadioStationEntity
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.repositories.RadioStationsRepository

class RadioStationsRepositoryImpl(
    private val radioStationsDao: RadioStationsDao
    ): RadioStationsRepository {

    override suspend fun getAllRadioStations(): List<RadioStation> {
        return radioStationsDao.getAllRadioStations().toRadioStationList()
    }

    override suspend fun getAllRadioStationsWithQuery(query: String): List<RadioStation> {
        return radioStationsDao.getRadioStationsWithQuery(query = query).toRadioStationList()
    }

    override suspend fun getRadioStationById(id: Int): RadioStation {
        return radioStationsDao.getRadioStationById(id = id).toRadioStation()
    }

    override suspend fun insertRadioStation(radioStation: RadioStation) {
        radioStationsDao.insertRadioStation(radioStation.toRadioStationEntity())
    }

    override suspend fun updateRadioStation(radioStation: RadioStation) {
        radioStationsDao.updateRadioStation(radioStation.toRadioStationEntity())
    }

    override suspend fun deleteRadioStations(radioStation: RadioStation) {
        radioStationsDao.deleteRadioStations(radioStation.toRadioStationEntity())
    }
}