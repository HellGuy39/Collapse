package com.hellguy39.domain.usecases

import com.hellguy39.domain.models.Track
import com.hellguy39.domain.repositories.TracksRepository

class AddTrackUseCase(private val tracksRepository: TracksRepository) {
    suspend operator fun invoke(track: Track) {
        tracksRepository.insertTrack(track = track)
    }
}