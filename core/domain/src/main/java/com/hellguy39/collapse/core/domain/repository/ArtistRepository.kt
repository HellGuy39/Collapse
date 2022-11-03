package com.hellguy39.collapse.core.domain.repository

import com.hellguy39.collapse.core.model.Artist
import com.hellguy39.collapse.core.model.Song

interface ArtistRepository {

    suspend fun getAllArtists(): List<Artist>

    suspend fun getAllSongsOfArtist(id: Long): List<Song>

    suspend fun getArtistById(id: Long): Artist

}