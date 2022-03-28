package com.hellguy39.data.repositories

import com.hellguy39.data.dao.PlaylistsDao
import com.hellguy39.data.models.PlaylistDb
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.repositories.PlaylistsRepository

class PlaylistsRepositoryImpl(
    private val playlistsDao: PlaylistsDao
): PlaylistsRepository {
    override suspend fun getAllPlaylists(): List<Playlist> {
        return toReturnableList(playlistsDao.getAllPlaylists())
    }

    override suspend fun getPlaylistById(id: Int): Playlist {
        return playlistsDao.getPlaylistById(id = id).toDefaultModel()
    }

    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistsDao.insertPlaylist(playlistDb = PlaylistDb().toDbModel(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistsDao.deletePlaylist(playlistDb = PlaylistDb().toDbModel(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistsDao.updatePlaylist(playlistDb = PlaylistDb().toDbModel(playlist))
    }

    private fun toReturnableList(list: List<PlaylistDb>): List<Playlist> {
        val returnableList = mutableListOf<Playlist>()

        for (n in list.indices) {
            returnableList.add(list[n].toDefaultModel())
        }

        return returnableList
    }
}