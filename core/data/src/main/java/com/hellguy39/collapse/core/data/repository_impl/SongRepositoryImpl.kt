package com.hellguy39.collapse.core.data.repository_impl

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.hellguy39.collapse.core.data.ContentUri
import com.hellguy39.collapse.core.data.Projections
import com.hellguy39.collapse.core.data.Selections
import com.hellguy39.collapse.core.data.columns.SongColumns
import com.hellguy39.collapse.core.data.columns.getSongColumns
import com.hellguy39.collapse.core.domain.repository.SongRepository
import com.hellguy39.collapse.core.model.Song

class SongRepositoryImpl(
    private val context: Context
): SongRepository {

    // Show only videos that are at least 5 minutes in duration.
    //val selection = "${MediaStore.Video.Media.DURATION} >= ?"
//    val selectionArgs = arrayOf(
//        TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES).toString()
//    )

    // Display videos in alphabetical order based on their display name.
//    val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"

    override suspend fun getAllSongs(): List<Song> {
        val list = mutableListOf<Song>()

        val query = context.contentResolver.query(
            ContentUri.externalSongs,
            Projections.songProjection,
            Selections.isMusic,
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
}