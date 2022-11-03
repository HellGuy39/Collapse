package com.hellguy39.collapse.core.domain.wrapper

import com.hellguy39.collapse.core.domain.usecase.song.GetAllSongsUseCase

data class SongUseCaseWrapper(
    val getAllSongsUseCase: GetAllSongsUseCase,
)
