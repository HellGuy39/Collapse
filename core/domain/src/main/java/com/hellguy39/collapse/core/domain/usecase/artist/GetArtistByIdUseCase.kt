package com.hellguy39.collapse.core.domain.usecase.artist

import com.hellguy39.collapse.core.domain.repository.ArtistRepository
import com.hellguy39.collapse.core.model.Artist

class GetArtistByIdUseCase(
    private val repository: ArtistRepository
) {

    suspend operator fun invoke(id: Long): Artist {
        return repository.getArtistById(id)
    }

}