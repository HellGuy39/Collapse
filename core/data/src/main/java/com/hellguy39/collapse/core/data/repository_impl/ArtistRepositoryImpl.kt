package com.hellguy39.collapse.core.data.repository_impl

import android.content.Context
import android.provider.MediaStore
import com.hellguy39.collapse.core.data.ContentUri
import com.hellguy39.collapse.core.data.Projections
import com.hellguy39.collapse.core.data.columns.getArtistColumns
import com.hellguy39.collapse.core.data.columns.getSongColumns
import com.hellguy39.collapse.core.domain.repository.ArtistRepository
import com.hellguy39.collapse.core.model.Artist
import com.hellguy39.collapse.core.model.Song

class ArtistRepositoryImpl(
    private val context: Context
): ArtistRepository {

    override suspend fun getAllArtists(): List<Artist> {

        val query = context.contentResolver.query(
            ContentUri.externalArtists,
            Projections.artistProjection,
            null,
            null,
            null
        )

        val list = mutableListOf<Artist>()

        query?.use { cursor ->

            val artistColumns = cursor.getArtistColumns()

            while (cursor.moveToNext()) {
                list.add(artistColumns.getArtist(cursor))
            }
        }
        return list
    }

    override suspend fun getAllSongsOfArtist(id: Long): List<Song> {
        val selection = "${MediaStore.Audio.Media.ARTIST_ID} == $id"

        val query = context.contentResolver.query(
            ContentUri.externalSongs,
            Projections.songProjection,
            selection,
            null,
            null
        )

        val list = mutableListOf<Song>()

        query?.use { cursor ->

            val songColumns = cursor.getSongColumns()

            while (cursor.moveToNext()) {
                list.add(songColumns.getSong(cursor))
            }
        }

        return list
    }

    override suspend fun getArtistById(id: Long): Artist {
        val selection = "${MediaStore.Audio.Artists._ID} == $id"

        val query = context.contentResolver.query(
            ContentUri.externalArtists,
            Projections.artistProjection,
            selection,
            null,
            null
        )

        val list = mutableListOf<Artist>()

        query?.use { cursor ->

            val artistColumns = cursor.getArtistColumns()

            while (cursor.moveToNext()) {
                list.add(artistColumns.getArtist(cursor))
            }
        }
        return list.first()
    }

}