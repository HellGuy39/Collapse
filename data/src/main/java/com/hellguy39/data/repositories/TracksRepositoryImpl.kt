package com.hellguy39.data.repositories

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.hellguy39.domain.models.Artist
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.repositories.TracksRepository

class TracksRepositoryImpl(
    private val context: Context
): TracksRepository {

    override suspend fun getAllTracks(): List<Track> {
        val audioDataList: MutableList<Track> = mutableListOf()

        val cursor = initCursor() ?: return emptyList()

        while (cursor.moveToNext()) {

            val track = Track(
                name = cursor.getString(0),
                path = cursor.getString(1),
                artist = cursor.getString(3)
            )

            audioDataList.add(track)
        }

        cursor.close()

        return audioDataList
    }

    override suspend fun getAllTracksByArtist(artist: String): List<Track> {
        val audioDataList: MutableList<Track> = mutableListOf()

        val cursor = initCursor() ?: return emptyList()

        while (cursor.moveToNext()) {

            val track = Track(
                name = cursor.getString(0),
                path = cursor.getString(1),
                artist = cursor.getString(3)
            )

            if (track.artist == artist) {
                audioDataList.add(track)
            }
        }

        cursor.close()

        return audioDataList
    }

    override suspend fun getAllArtists(): List<Artist> {

        val artistList = mutableListOf<Artist>()

        val cursor = initCursor() ?: return emptyList()

        while (cursor.moveToNext()) {

            val track = Track(
                name = cursor.getString(0),
                path = cursor.getString(1),
                artist = cursor.getString(3)
            )

            val pos = isContainArtist(artistList, track.artist)

            if (pos != -1) {
                artistList[pos].trackList.add(track)
            } else {
                artistList.add(Artist(name = track.artist, trackList = mutableListOf(track)))
            }

        }

        cursor.close()

        return artistList
    }

    private fun isContainArtist(artistList: List<Artist>, artist: String): Int {
        for (n in artistList.indices) {
            if (artistList[n].name == artist) {
                return n
            }
        }
        return -1
    }

    private fun initCursor(): Cursor? {
        val contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection: Array<String> = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        return context.contentResolver?.query(
            contentUri,
            projection,
            selection,
            null,
            null
        )
    }
}