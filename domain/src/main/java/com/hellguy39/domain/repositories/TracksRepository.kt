package com.hellguy39.domain.repositories

import com.hellguy39.domain.models.Track

interface TracksRepository {

    suspend fun insertTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    suspend fun getAllTracks(): List<Track>
}