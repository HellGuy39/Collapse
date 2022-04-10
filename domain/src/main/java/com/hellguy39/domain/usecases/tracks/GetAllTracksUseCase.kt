package com.hellguy39.domain.usecases.tracks

import com.hellguy39.domain.models.Track
import com.hellguy39.domain.repositories.TracksRepository

class GetAllTracksUseCase(private val repository: TracksRepository) {
    suspend operator fun invoke(): List<Track> {
        return repository.getAllTracks()
    }
}