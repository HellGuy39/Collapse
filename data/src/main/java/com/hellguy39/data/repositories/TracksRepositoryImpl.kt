package com.hellguy39.data.repositories

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.hellguy39.data.mappers.isContainArtist
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.repositories.TracksRepository
import com.hellguy39.domain.utils.PlaylistType

class TracksRepositoryImpl(
    private val context: Context
): TracksRepository {

    companion object {
        val contentUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection: Array<String> = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
            //MediaStore.Audio.GenresColumns.NAME,
            MediaStore.Audio.Media.YEAR
        )

        const val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        const val ITEM_NOT_CONTAINS = -1
    }

    override suspend fun getAllTracks(): List<Track> {
        val audioDataList: MutableList<Track> = mutableListOf()

        val cursor = initCursor() ?: return emptyList()

        while (cursor.moveToNext())
            audioDataList.add(getTrackMetadata(cursor))

        cursor.close()

        return audioDataList
    }

    override suspend fun getAllTracksByArtist(artist: String): List<Track> {
        val audioDataList: MutableList<Track> = mutableListOf()

        val cursor = initCursor() ?: return emptyList()

        while (cursor.moveToNext()) {

            val track = getTrackMetadata(cursor)

            if (track.artist == artist) {
                audioDataList.add(track)
            }
        }

        cursor.close()

        return audioDataList
    }

    override suspend fun getAllArtists(): List<Playlist> {

        val artistList = mutableListOf<Playlist>()

        val cursor = initCursor() ?: return emptyList()

        while (cursor.moveToNext()) {

            val track = getTrackMetadata(cursor)
            val pos = artistList.isContainArtist(track.artist)

            if (pos == ITEM_NOT_CONTAINS) {
                artistList.add(
                    Playlist(
                        name = track.artist,
                        tracks = mutableListOf(track),
                        type = PlaylistType.Artist
                    )
                )
            } else {
                artistList[pos].tracks.add(track)
            }
        }

        cursor.close()

        return artistList
    }

    private fun initCursor(): Cursor? = context.contentResolver?.query(
        contentUri,
        projection,
        selection,
        null,
        null
    )

    private fun getTrackMetadata(cursor: Cursor): Track {
        return Track(
            name = cursor.getString(0),
            path = cursor.getString(1),
            duration = cursor.getLong(2),
            artist = cursor.getString(3),
            //genre = cursor.getString(4),
            year = cursor.getInt(4)
        )
    }
}