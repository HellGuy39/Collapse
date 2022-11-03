package com.hellguy39.collapse.core.domain.usecase.album

import com.hellguy39.collapse.core.domain.repository.AlbumRepository
import com.hellguy39.collapse.core.model.Album

class GetAllAlbumsUseCase(
    private val repository: AlbumRepository
) {

    suspend operator fun invoke(): List<Album> {
        return repository.getAllAlbums()
    }

}