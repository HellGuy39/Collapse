package com.hellguy39.collapse.core.data.repository_impl

import android.content.Context
import android.provider.MediaStore
import com.hellguy39.collapse.core.data.ContentUri
import com.hellguy39.collapse.core.data.Projections
import com.hellguy39.collapse.core.data.columns.getAlbumColumns
import com.hellguy39.collapse.core.data.columns.getSongColumns
import com.hellguy39.collapse.core.domain.repository.AlbumRepository
import com.hellguy39.collapse.core.model.Album
import com.hellguy39.collapse.core.model.Song

class AlbumRepositoryImpl(
    private val context: Context
): AlbumRepository {

//    private val selection = "${MediaStore.Audio.Media.IS_MUSIC} == 1"

    // Show only videos that are at least 5 minutes in duration.
    //val selection = "${MediaStore.Video.Media.DURATION} >= ?"
//    val selectionArgs = arrayOf(
//        TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES).toString()
//    )

    // Display videos in alphabetical order based on their display name.
//    val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"

    override suspend fun getAllAlbums(): List<Album> {

        val query = context.contentResolver.query(
            ContentUri.externalAlbums,
            Projections.albumProjection,
            null,
            null,
            null
        )

        val list = mutableListOf<Album>()

        query?.use { cursor ->

            val albumColumn = cursor.getAlbumColumns()

            while (cursor.moveToNext()) {
                list.add(
                    albumColumn.getAlbum(cursor)
                )
            }
        }
        return list
    }

    override suspend fun getAllSongsOfAlbum(id: Long): List<Song> {
        val selection = "${MediaStore.Audio.Media.ALBUM_ID} == $id"
        val list = mutableListOf<Song>()

        val query = context.contentResolver.query(
            ContentUri.externalSongs,
            Projections.songProjection,
            selection,
            null,
            null
        )

        query?.use { cursor ->
            val songColumns = query.getSongColumns()
            while (cursor.moveToNext()) {
                list.add(songColumns.getSong(cursor))
            }
        }
        return list
    }

    override suspend fun getAlbumById(id: Long): Album {
        val selection = "${MediaStore.Audio.Albums._ID} == $id"

        val query = context.contentResolver.query(
            ContentUri.externalAlbums,
            Projections.albumProjection,
            selection,
            null,
            null
        )

        val list = mutableListOf<Album>()

        query?.use { cursor ->

            val albumColumns = cursor.getAlbumColumns()

            while (cursor.moveToNext()) {
                list.add(albumColumns.getAlbum(cursor))
            }
        }
        return list.first()
    }


}