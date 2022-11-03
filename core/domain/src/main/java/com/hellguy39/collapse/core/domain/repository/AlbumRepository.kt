package com.hellguy39.collapse.core.domain.repository

import com.hellguy39.collapse.core.model.Album
import com.hellguy39.collapse.core.model.Song

interface AlbumRepository {

    suspend fun getAllAlbums(): List<Album>

    suspend fun getAllSongsOfAlbum(id: Long): List<Song>

    suspend fun getAlbumById(id: Long): Album

}