package com.hellguy39.collapse.core.domain.usecase.album

import com.hellguy39.collapse.core.domain.repository.AlbumRepository
import com.hellguy39.collapse.core.model.Album

class GetAlbumByIdUseCase(
    private val repository: AlbumRepository
) {

    suspend operator fun invoke(id: Long): Album {
        return repository.getAlbumById(id)
    }

}