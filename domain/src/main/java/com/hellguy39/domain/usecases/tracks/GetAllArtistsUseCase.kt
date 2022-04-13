package com.hellguy39.domain.usecases.tracks

import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.repositories.TracksRepository

class GetAllArtistsUseCase(private val repository: TracksRepository) {
    suspend operator fun invoke() : List<Playlist> {
        return repository.getAllArtists()
    }
}