package com.hellguy39.data.repositories

import android.content.SharedPreferences
import android.util.Log
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.models.ServiceContentWrapper
import com.hellguy39.domain.repositories.SavedServiceStateRepository
import com.hellguy39.domain.usecases.playlist.GetPlaylistByIdUseCase
import com.hellguy39.domain.usecases.radio.GetRadioStationByIdUseCase
import com.hellguy39.domain.utils.PlayerType
import com.hellguy39.domain.utils.PlaylistType

private const val PLAYLIST_ID = "playlist_id"
private const val RADIO_STATION_ID = "radio_station_id"
private const val PLAYER_POSITION = "player_position"
private const val POSITION = "position"
private const val PLAYER_TYPE = "player_type"
private const val PLAYLIST_TYPE = "playlist_type"

class SavedServiceStateRepositoryImpl(
   private val prefs: SharedPreferences,
): SavedServiceStateRepository {
    override suspend fun getSavedState(): ServiceContentWrapper {

    val radioStationId = prefs.getInt(RADIO_STATION_ID, -1)
    val radioStation = if (radioStationId == -1) null else RadioStation(id = radioStationId)

    val playlistId = prefs.getInt(PLAYLIST_ID, -1)
    val playlist = if (playlistId == -1) null else Playlist(
        id = playlistId,
        type = when(prefs.getString(PLAYLIST_TYPE, PlaylistType.Undefined.name)) {
            PlaylistType.AllTracks.name -> PlaylistType.AllTracks
            PlaylistType.Favourites.name -> PlaylistType.Favourites
            PlaylistType.Custom.name -> PlaylistType.Custom
            PlaylistType.Artist.name -> PlaylistType.Artist
            else -> PlaylistType.Undefined
        }
    )

    return ServiceContentWrapper(
        type = when(prefs.getString(PLAYER_TYPE, PlayerType.Undefined.name)) {
            PlayerType.LocalTrack.name -> PlayerType.LocalTrack
            PlayerType.Radio.name -> PlayerType.Radio
            else -> PlayerType.Undefined
        },
        playlist = playlist,
        radioStation = radioStation,
        position = prefs.getInt(POSITION, 0),
        playerPosition = prefs.getLong(PLAYER_POSITION, 0),
        fromSavedState = true,
        )
    }


    override suspend fun insertSavedState(serviceContent: ServiceContentWrapper) {

        prefs.edit().apply {
            this.putInt(PLAYLIST_ID, serviceContent.playlist?.id ?: -1)
            this.putInt(RADIO_STATION_ID, serviceContent.radioStation?.id ?: -1)
            this.putLong(PLAYER_POSITION, serviceContent.playerPosition)
            this.putInt(POSITION, serviceContent.position)
            this.putString(PLAYER_TYPE, serviceContent.type.name)
            this.putString(PLAYLIST_TYPE, serviceContent.playlist?.type?.name ?: PlaylistType.Undefined.name)
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