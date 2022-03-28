package com.hellguy39.data.repositories

import com.hellguy39.data.dao.RadioStationsDao
import com.hellguy39.data.models.RadioStationDb
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.repositories.RadioStationsRepository

class RadioStationsRepositoryImpl(
    private val radioStationsDao: RadioStationsDao
    ): RadioStationsRepository {

    override suspend fun getAllRadioStations(): List<RadioStation> {
        return createReturnableList(
            radioStationsDao.getAllRadioStations()
        )
    }

    override suspend fun getAllRadioStationsWithQuery(query: String): List<RadioStation> {
        return createReturnableList(
            radioStationsDao.getRadioStationsWithQuery(query = query)
        )
    }

    override suspend fun getRadioStationById(id: Int): RadioStation {
        return radioStationsDao.getRadioStationById(id = id).toDefaultModel()
    }

    override suspend fun insertRadioStation(radioStation: RadioStation) {
        radioStationsDao.insertRadioStation(RadioStationDb().toDbModel(radioStation))
    }

    override suspend fun updateRadioStation(radioStation: RadioStation) {
        radioStationsDao.updateRadioStation(RadioStationDb().toDbModel(radioStation))
    }

    override suspend fun deleteRadioStations(radioStation: RadioStation) {
        radioStationsDao.deleteRadioStations(RadioStationDb().toDbModel(radioStation))
    }

    private fun createReturnableList(fromList: List<RadioStationDb>): List<RadioStation> {
        val returnableList = mutableListOf<RadioStation>()
        for (n in fromList.indices) {
            returnableList.add(fromList[n].toDefaultModel())
        }
        return returnableList
    }
}