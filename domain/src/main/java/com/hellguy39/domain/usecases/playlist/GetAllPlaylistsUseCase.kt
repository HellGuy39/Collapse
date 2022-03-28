package com.hellguy39.domain.usecases.playlist

import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.repositories.PlaylistsRepository

class GetAllPlaylistsUseCase(
    private val playlistsRepository: PlaylistsRepository
) {
    suspend operator fun invoke(): List<Playlist> {
        return playlistsRepository.getAllPlaylists()
    }
}