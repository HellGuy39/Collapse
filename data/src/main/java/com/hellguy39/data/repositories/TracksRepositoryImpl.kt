package com.hellguy39.data.repositories

import android.util.Log
import com.hellguy39.data.db.TracksDao
import com.hellguy39.data.models.TrackDatabase
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.repositories.TracksRepository

class TracksRepositoryImpl(
    private val tracksDao: TracksDao
): TracksRepository {
    override suspend fun insertTrack(track: Track) {
        val trackDatabase = TrackDatabase()
        trackDatabase.toDatabaseEntity(track)
        tracksDao.insertTrack(track = trackDatabase)
    }

    override suspend fun deleteTrack(track: Track) {
        val trackDatabase = TrackDatabase()
        trackDatabase.toDatabaseEntity(track)
        tracksDao.deleteTrack(track = trackDatabase)
    }

    override suspend fun getAllTracks(): List<Track> {
        val trackDatabaseList = tracksDao.getAllTracks()
        val trackList: MutableList<Track> = mutableListOf()
        for (n in trackDatabaseList.indices) {
            trackList.add(trackDatabaseList[n].toDefaultModel())
        }
        return trackList
    }

}