package com.hellguy39.collapse.core.domain.wrapper

import com.hellguy39.collapse.core.domain.usecase.artist.GetAllArtistsUseCase
import com.hellguy39.collapse.core.domain.usecase.artist.GetAllSongsOfArtistUseCase
import com.hellguy39.collapse.core.domain.usecase.artist.GetArtistByIdUseCase

data class ArtistUseCaseWrapper(
    val getAllArtistsUseCase: GetAllArtistsUseCase,
    val getAllSongsOfArtistUseCase: GetAllSongsOfArtistUseCase,
    val getArtistByIdUseCase: GetArtistByIdUseCase
)
