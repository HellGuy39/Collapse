package com.hellguy39.domain.usecases.playlist

data class PlaylistUseCases(
    val addPlaylistUseCase: AddPlaylistUseCase,
    val deletePlaylistUseCase: DeletePlaylistUseCase,
    val updatePlaylistUseCase: UpdatePlaylistUseCase,
    val getAllPlaylistsUseCase: GetAllPlaylistsUseCase,
    val getPlaylistByIdUseCase: GetPlaylistByIdUseCase
)
