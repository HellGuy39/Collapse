package com.hellguy39.domain.repositories

import com.hellguy39.domain.models.Artist
import com.hellguy39.domain.models.Track

interface TracksRepository {
    suspend fun getAllTracks(): List<Track>
    suspend fun getAllTracksByArtist(artist: String): List<Track>
    suspend fun getAllArtists(): List<Artist>
}