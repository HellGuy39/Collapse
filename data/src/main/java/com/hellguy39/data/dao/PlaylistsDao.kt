package com.hellguy39.data.dao

import androidx.room.*
import com.hellguy39.data.models.PlaylistDb

@Dao
interface PlaylistsDao {

    @Query("SELECT * FROM PlaylistDb")
    suspend fun getAllPlaylists(): List<PlaylistDb>

    @Query("SELECT * FROM PlaylistDb WHERE id = :id")
    suspend fun getPlaylistById(id: Int): PlaylistDb

    @Insert
    suspend fun insertPlaylist(playlistDb: PlaylistDb)

    @Delete
    suspend fun deletePlaylist(playlistDb: PlaylistDb)

    @Update
    suspend fun updatePlaylist(playlistDb: PlaylistDb)

}