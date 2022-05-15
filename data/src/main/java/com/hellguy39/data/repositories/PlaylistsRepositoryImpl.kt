package com.hellguy39.data.repositories

import com.hellguy39.data.dao.PlaylistsDao
import com.hellguy39.data.mappers.toPlaylist
import com.hellguy39.data.mappers.toPlaylistEntity
import com.hellguy39.data.mappers.toPlaylistList
import com.hellguy39.data.models.PlaylistEntity
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.repositories.PlaylistsRepository

class PlaylistsRepositoryImpl(
    private val playlistsDao: PlaylistsDao
): PlaylistsRepository {
    override suspend fun getAllPlaylists(): List<Playlist> {
        return playlistsDao.getAllPlaylists().toPlaylistList()
    }

    override suspend fun getPlaylistById(id: Int): Playlist {
        return playlistsDao.getPlaylistById(id = id).toPlaylist()
    }

    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistsDao.insertPlaylist(playlistEntity = playlist.toPlaylistEntity())
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistsDao.deletePlaylist(playlistEntity = playlist.toPlaylistEntity())
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistsDao.updatePlaylist(playlistEntity = playlist.toPlaylistEntity())
    }
}