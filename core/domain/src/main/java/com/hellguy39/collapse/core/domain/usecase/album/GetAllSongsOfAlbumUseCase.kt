package com.hellguy39.collapse.core.domain.usecase.album

import com.hellguy39.collapse.core.domain.repository.AlbumRepository
import com.hellguy39.collapse.core.model.Album
import com.hellguy39.collapse.core.model.Song

class GetAllSongsOfAlbumUseCase(
    private val repository: AlbumRepository
) {

    suspend operator fun invoke(id: Long): List<Song> {
        return repository.getAllSongsOfAlbum(id)
    }

}