package com.hellguy39.domain.usecases.favourites

import com.hellguy39.domain.models.Track
import com.hellguy39.domain.repositories.FavouritesRepository

class GetAllFavouriteTracksUseCase(private val favouritesRepository: FavouritesRepository) {
    suspend operator fun invoke(): List<Track> {
        return favouritesRepository.getAllFavouriteTracks()
    }
}