package com.hellguy39.domain.usecases.favourites

import com.hellguy39.domain.models.Track
import com.hellguy39.domain.repositories.FavouritesRepository

class AddFavouriteTrackUseCase(private val favouritesRepository: FavouritesRepository) {
    suspend operator fun invoke(track: Track) {
        favouritesRepository.insertFavouriteTrack(track = track)
    }
}