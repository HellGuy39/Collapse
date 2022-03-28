package com.hellguy39.domain.repositories

import com.hellguy39.domain.models.Track

interface TracksRepository {
    suspend fun getAllTracks(args: String?): List<Track>
}