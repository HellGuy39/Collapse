package com.hellguy39.domain.usecases.favourites

import com.hellguy39.domain.models.Track
import com.hellguy39.domain.repositories.FavouritesRepository

class DeleteFromFavouritesWithoutIdUseCase(private val repository: FavouritesRepository) {
    suspend operator fun invoke(track: Track) {
        repository.deleteFromFavouritesWithoutId(track)
    }
}