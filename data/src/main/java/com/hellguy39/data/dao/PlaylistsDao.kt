package com.hellguy39.data.dao

import androidx.room.*
import com.hellguy39.data.models.PlaylistEntity

@Dao
interface PlaylistsDao {

    @Query("SELECT * FROM PlaylistEntity")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM PlaylistEntity WHERE id = :id")
    suspend fun getPlaylistById(id: Int): PlaylistEntity

    @Insert
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Update
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)

}