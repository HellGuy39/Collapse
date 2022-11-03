package com.hellguy39.collapse.core.domain.usecase.artist

import com.hellguy39.collapse.core.domain.repository.ArtistRepository
import com.hellguy39.collapse.core.model.Artist

class GetAllArtistsUseCase(
    private val repository: ArtistRepository
) {

    suspend operator fun invoke(): List<Artist> {
        return repository.getAllArtists()
    }

}