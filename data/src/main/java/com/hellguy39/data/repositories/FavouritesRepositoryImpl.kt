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

    override suspend fun isTrackFavourite(track: Track): Boolean {
        val favourites = favouritesDao.getAllFavouriteTracks()
        for (n in favourites.indices) {
            if (isFavourite(favourites[n], track)) {
                return true
            }
        }
        return false
    }

    override suspend fun deleteFromFavouritesWithoutId(track: Track) {
        val favourites = favouritesDao.getAllFavouriteTracks()
        for (n in favourites.indices) {
            if (isFavourite(favourites[n], track)) {
                favouritesDao.deleteFavouriteTrack(favourites[n])
            }
        }
    }

    private fun toReturnableList(list: List<TrackDb>): List<Track> {
        val returnableList = mutableListOf<Track>()

        for (n in list.indices) {
            returnableList.add(list[n].toDefaultModel())
        }

        return returnableList
    }
    private fun isFavourite(trackDb: TrackDb, track: Track): Boolean {
        return (trackDb.path == track.path &&
                trackDb.artist == track.artist &&
                trackDb.name == track.name)

    }
}