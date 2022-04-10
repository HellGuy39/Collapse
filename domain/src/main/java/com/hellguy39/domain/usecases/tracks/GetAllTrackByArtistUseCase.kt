package com.hellguy39.domain.usecases.tracks

import com.hellguy39.domain.models.Track
import com.hellguy39.domain.repositories.TracksRepository

class GetAllTrackByArtistUseCase(private val repository: TracksRepository) {
    suspend operator fun invoke(artist: String): List<Track> {
        return repository.getAllTracksByArtist(artist = artist)
    }
}