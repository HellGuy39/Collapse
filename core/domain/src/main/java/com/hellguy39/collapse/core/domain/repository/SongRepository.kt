package com.hellguy39.collapse.core.domain.repository

import com.hellguy39.collapse.core.model.Song

interface SongRepository {

    suspend fun getAllSongs(): List<Song>

}