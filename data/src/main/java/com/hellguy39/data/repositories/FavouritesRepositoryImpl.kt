package com.hellguy39.data.repositories

import com.hellguy39.data.dao.FavouritesDao
import com.hellguy39.data.mappers.*
import com.hellguy39.data.models.TrackEntity
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.repositories.FavouritesRepository

class FavouritesRepositoryImpl(
    private val favouritesDao: FavouritesDao
    ): FavouritesRepository {

    override suspend fun getAllFavouriteTracks(): List<Track> {
        return favouritesDao.getAllFavouriteTracks().toTrackList()
    }

    override suspend fun insertFavouriteTrack(track: Track) {
        favouritesDao.insertFavouriteTrack(trackEntity = track.toTrackEntity())
    }

    override suspend fun deleteFavouriteTrack(track: Track) {
        favouritesDao.deleteFavouriteTrack(trackEntity = track.toTrackEntity())
    }

    override suspend fun isTrackFavourite(track: Track): Boolean {
        val favourites = favouritesDao.getAllFavouriteTracks()
        for (n in favourites.indices) {
            if (track.isSameTrackOf(favourites[n]))
                return true
        }
        return false
    }

    override suspend fun deleteFromFavouritesWithoutId(track: Track) {
        val favourites = favouritesDao.getAllFavouriteTracks()
        for (n in favourites.indices) {
            if (track.isSameTrackOf(favourites[n]))
                favouritesDao.deleteFavouriteTrack(favourites[n])
        }
    }
}