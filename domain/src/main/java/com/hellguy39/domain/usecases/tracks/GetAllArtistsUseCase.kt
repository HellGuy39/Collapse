package com.hellguy39.domain.usecases.tracks

import com.hellguy39.domain.models.Artist
import com.hellguy39.domain.repositories.TracksRepository

class GetAllArtistsUseCase(private val repository: TracksRepository) {
    suspend operator fun invoke() : List<Artist> {
        return repository.getAllArtists()
    }
}