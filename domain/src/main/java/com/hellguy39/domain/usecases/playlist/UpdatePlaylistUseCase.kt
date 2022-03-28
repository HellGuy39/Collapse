package com.hellguy39.domain.usecases.playlist

import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.repositories.PlaylistsRepository

class UpdatePlaylistUseCase(
    private val playlistsRepository: PlaylistsRepository
) {
    suspend operator fun invoke(playlist: Playlist) {
        playlistsRepository.updatePlaylist(playlist = playlist)
    }
}