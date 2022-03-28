package com.hellguy39.domain.usecases.playlist

import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.repositories.PlaylistsRepository

class AddPlaylistUseCase(
    private val playlistsRepository: PlaylistsRepository
) {
    suspend operator fun invoke(playlist: Playlist) {
        playlistsRepository.insertPlaylist(playlist = playlist)
    }
}