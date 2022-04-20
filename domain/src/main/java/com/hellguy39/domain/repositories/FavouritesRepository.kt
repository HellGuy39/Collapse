package com.hellguy39.domain.repositories

import com.hellguy39.domain.models.Track

interface FavouritesRepository {

    suspend fun getAllFavouriteTracks(): List<Track>

    suspend fun insertFavouriteTrack(track: Track)

    suspend fun deleteFavouriteTrack(track: Track)

    suspend fun isTrackFavourite(track: Track): Boolean

    suspend fun deleteFromFavouritesWithoutId(track: Track)
}