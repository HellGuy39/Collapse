package com.hellguy39.domain.repositories

import com.hellguy39.domain.models.Playlist

interface PlaylistsRepository {

    suspend fun getAllPlaylists(): List<Playlist>

    suspend fun getPlaylistById(id: Int): Playlist

    suspend fun insertPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

}