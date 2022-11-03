package com.hellguy39.collapse.core.domain.usecase.artist

import com.hellguy39.collapse.core.domain.repository.ArtistRepository
import com.hellguy39.collapse.core.model.Artist
import com.hellguy39.collapse.core.model.Song

class GetAllSongsOfArtistUseCase(
    private val repository: ArtistRepository
) {

    suspend operator fun invoke(id: Long): List<Song> {
        return repository.getAllSongsOfArtist(id)
    }

}