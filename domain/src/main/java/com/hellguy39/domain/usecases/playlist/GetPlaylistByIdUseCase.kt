package com.hellguy39.domain.usecases.playlist

import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.repositories.PlaylistsRepository

class GetPlaylistByIdUseCase(
    private val playlistsRepository: PlaylistsRepository
) {
    suspend operator fun invoke(id: Int): Playlist {
        return playlistsRepository.getPlaylistById(id)
    }
}