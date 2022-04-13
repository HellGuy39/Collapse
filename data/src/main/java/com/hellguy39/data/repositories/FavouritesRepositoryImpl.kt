package com.hellguy39.data.repositories

import com.hellguy39.data.dao.FavouritesDao
import com.hellguy39.data.models.TrackDb
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.repositories.FavouritesRepository

class FavouritesRepositoryImpl(
    private val favouritesDao: FavouritesDao
    ): FavouritesRepository {

    override suspend fun getAllFavouriteTracks(): List<Track> {
        return toReturnableList(favouritesDao.getAllFavouriteTracks())
    }

    override suspend fun insertFavouriteTrack(track: Track) {
        favouritesDao.insertFavouriteTrack(trackDb = TrackDb().toDbModel(track))
    }

    override suspend fun deleteFavouriteTrack(track: Track) {
        favouritesDao.deleteFavouriteTrack(trackDb = TrackDb().toDbModel(track))
    }

    private fun toReturnableList(list: List<TrackDb>): List<Track> {
        val returnableList = mutableListOf<Track>()

        for (n in list.indices) {
            returnableList.add(list[n].toDefaultModel())
        }

        return returnableList
    }
}