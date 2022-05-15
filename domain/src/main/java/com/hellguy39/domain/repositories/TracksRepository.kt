package com.hellguy39.domain.repositories

import com.hellguy39.domain.models.*

interface TracksRepository {
    suspend fun getAllTracks(): List<Track>
    suspend fun getAllTracksByArtist(artist: String): List<Track>
    suspend fun getAllArtists(): List<Playlist>
    suspend fun getAllPodcasts(): List<Podcast>
    suspend fun getAllAudioBooks(): List<AudioBook>
    suspend fun getAllRecordings(): List<Recording>
    suspend fun getAllRingtones(): List<Ringtone>
}