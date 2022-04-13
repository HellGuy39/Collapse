package com.hellguy39.data.repositories

import android.content.SharedPreferences
import com.hellguy39.domain.models.SavedState
import com.hellguy39.domain.repositories.SavedServiceStateRepository
import com.hellguy39.domain.utils.PlayerType
import com.hellguy39.domain.utils.PlaylistType

private const val PLAYLIST_ID = "playlist_id"
private const val RADIO_STATION_ID = "radio_station_id"
private const val PLAYER_POSITION = "player_position"
private const val POSITION = "position"
private const val PLAYER_TYPE = "player_type"
private const val PLAYLIST_TYPE = "playlist_type"
private const val ARTIST_NAME = "artist_name"

class SavedServiceStateRepositoryImpl(
   private val prefs: SharedPreferences,
): SavedServiceStateRepository {
    override suspend fun getSavedState(): SavedState {

    var radioStationId: Int? = prefs.getInt(RADIO_STATION_ID, -100)
    var playlistId: Int? = prefs.getInt(PLAYLIST_ID, -100)

    if(radioStationId == -100)
        radioStationId = null

    if (playlistId == -100)
        playlistId = null

    val type = when(prefs.getString(PLAYLIST_TYPE, PlaylistType.Undefined.name)) {
        PlaylistType.AllTracks.name -> PlaylistType.AllTracks
        PlaylistType.Favourites.name -> PlaylistType.Favourites
        PlaylistType.Custom.name -> PlaylistType.Custom
        PlaylistType.Artist.name -> PlaylistType.Artist
        else -> PlaylistType.Undefined
    }


    return SavedState(
        playerType = when(prefs.getString(PLAYER_TYPE, PlayerType.Undefined.name)) {
            PlayerType.LocalTrack.name -> PlayerType.LocalTrack
            PlayerType.Radio.name -> PlayerType.Radio
            else -> PlayerType.Undefined
        },
        playlistType = type,
        playlistId = playlistId,
        radioStationId = radioStationId,
        position = prefs.getInt(POSITION, 0),
        playerPosition = prefs.getLong(PLAYER_POSITION, 0),
        artistName = prefs.getString(ARTIST_NAME, null)
        )
    }


    override suspend fun insertSavedState(savedState: SavedState) {

        prefs.edit().apply {
            this.putInt(PLAYLIST_ID, savedState.playlistId ?: -100)
            this.putInt(RADIO_STATION_ID, savedState.radioStationId ?: -100)
            this.putLong(PLAYER_POSITION, savedState.playerPosition)
            this.putInt(POSITION, savedState.position)
            this.putString(PLAYER_TYPE, savedState.playerType.name)
            this.putString(PLAYLIST_TYPE, savedState.playlistType.name)
            this.putString(ARTIST_NAME, savedState.artistName)
        }.apply()
    }
}

//class SavedServiceStateRepositoryImpl(
//    private val dao: SavedServiceStateDao,
//): SavedServiceStateRepository {
//    override suspend fun getSavedState(): ServiceContentWrapper {
//        val stateList = dao.getSavedState()
//
//        if (stateList.isNullOrEmpty())
//            return ServiceContentWrapper(radioStation = null, playlist = null)
//
//        val state: SavedServiceStateDb = dao.getSavedState()[0]
//        return ServiceContentWrapper(
//            type = when(state.type) {
//                PlayerType.Radio.name -> PlayerType.Radio
//                PlayerType.LocalTrack.name -> PlayerType.LocalTrack
//                else -> PlayerType.Undefined
//            },
//            radioStation = if (state.radioStationId != null) RadioStation(id = state.radioStationId!!) else null,
//            playlist = if (state.playlistId != null) Playlist(id = state.playlistId!!) else null,
//            playerPosition = state.playerPosition
//        )
//    }
//
//    override fun insertSavedState(serviceContent: ServiceContentWrapper) {
//        dao.insertSavedState(SavedServiceStateDb(
//            type = serviceContent.type.name,
//            playerPosition = serviceContent.playerPosition,
//            position = serviceContent.position,
//            radioStationId = serviceContent.radioStation?.id,
//            playlistId = serviceContent.playlist?.id
//        ))
//    }
//}