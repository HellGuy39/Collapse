package com.hellguy39.domain.usecases.tracks

import com.hellguy39.domain.models.Track
import com.hellguy39.domain.repositories.TracksRepository

class GetTracksByArtistUseCase(private val tracksRepository: TracksRepository) {
    suspend operator fun invoke(artist: String): List<Track> {
        return tracksRepository.getAllTracksByArtist(artist = artist)
    }
}