package com.hellguy39.collapse.core.domain.wrapper

import com.hellguy39.collapse.core.domain.usecase.album.GetAlbumByIdUseCase
import com.hellguy39.collapse.core.domain.usecase.album.GetAllAlbumsUseCase
import com.hellguy39.collapse.core.domain.usecase.album.GetAllSongsOfAlbumUseCase

data class AlbumUseCaseWrapper(
    val getAllAlbumsUseCase: GetAllAlbumsUseCase,
    val getAllSongsOfAlbumUseCase: GetAllSongsOfAlbumUseCase,
    val getAlbumByIdUseCase: GetAlbumByIdUseCase
)
