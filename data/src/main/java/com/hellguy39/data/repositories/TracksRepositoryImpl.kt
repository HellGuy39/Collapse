package com.hellguy39.data.repositories

import android.content.Context
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.repositories.TracksRepository

class TracksRepositoryImpl(
    private val context: Context
): TracksRepository {

    override suspend fun getAllTracks(): List<Track> {
        val audioDataList: MutableList<Track> = mutableListOf()
        val mmr = MediaMetadataRetriever()
        val contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection: Array<String> = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        val cursor = context.contentResolver?.query(
            contentUri,
            projection,
            selection,
            null,
            null
        ) ?: return mutableListOf()

        while (cursor.moveToNext()) {
            mmr.setDataSource(cursor.getString(1))
            audioDataList.add(Track(
                name = cursor.getString(0),
                path = cursor.getString(1),
                artist = cursor.getString(3),
                embeddedPicture = mmr.embeddedPicture
            ))
        }

        cursor.close()

        return audioDataList
    }
}