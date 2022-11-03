
package com.hellguy39.collapse.core.domain.usecase.song

import com.hellguy39.collapse.core.model.Song
import com.hellguy39.collapse.core.domain.repository.SongRepository

class GetAllSongsUseCase(
    private val repository: SongRepository
) {

    suspend operator fun invoke(): List<Song> {
        return repository.getAllSongs()
    }

}