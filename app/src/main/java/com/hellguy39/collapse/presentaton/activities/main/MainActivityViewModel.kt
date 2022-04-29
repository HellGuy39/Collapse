package com.hellguy39.collapse.presentaton.activities.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.ServiceContentWrapper
import com.hellguy39.domain.usecases.favourites.FavouriteTracksUseCases
import com.hellguy39.domain.usecases.playlist.PlaylistUseCases
import com.hellguy39.domain.usecases.state.SavedServiceStateUseCases
import com.hellguy39.domain.usecases.tracks.TracksUseCases
import com.hellguy39.domain.utils.PlayerType
import com.hellguy39.domain.utils.PlaylistType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val savedServiceStateUseCases: SavedServiceStateUseCases,
    private val tracksUseCases: TracksUseCases,
    private val favouriteTracksUseCases: FavouriteTracksUseCases,
    private val playlistUseCases: PlaylistUseCases
): ViewModel() {

    fun checkServiceSavedState(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        val savedState = savedServiceStateUseCases.getSavedServiceStateUseCase.invoke()

        when(savedState.playerType) {
            PlayerType.LocalTrack -> {
                val playlistId = savedState.playlistId
                val artistName = savedState.artistName
                val position = savedState.position
                val playerPosition = savedState.playerPosition

                when(savedState.playlistType) {
                    PlaylistType.AllTracks -> {
                        val trackList = tracksUseCases.getAllTracksUseCase.invoke()
                        startPlayer(receivedPlaylist = Playlist(
                            tracks = trackList.toMutableList(),
                            name = "All tracks",
                            type = PlaylistType.AllTracks
                        ),
                            position = position,
                            playerPosition = playerPosition,
                            skipPauseClick = true,
                            startWithShuffle = false,
                            context = context
                        )
                    }
                    PlaylistType.Favourites -> {
                        val favourites = favouriteTracksUseCases.getAllFavouriteTracksUseCase.invoke()
                        startPlayer(receivedPlaylist = Playlist(
                            tracks = favourites.toMutableList(),
                            name = "All tracks",
                            type = PlaylistType.AllTracks
                        ),
                            position = position,
                            playerPosition = playerPosition,
                            skipPauseClick = true,
                            startWithShuffle = false,
                            context = context
                        )
                    }
                    PlaylistType.Artist -> {
                        if (artistName != null) {
                            val artistTrackList = tracksUseCases.getAllTrackByArtistUseCase.invoke(artistName)
                            startPlayer(receivedPlaylist = Playlist(
                                tracks = artistTrackList.toMutableList(),
                                name = artistName,
                                type = PlaylistType.Artist
                            ),
                                position = position,
                                playerPosition = playerPosition,
                                skipPauseClick = true,
                                startWithShuffle = false,
                                context = context
                            )
                        }
                    }
                    PlaylistType.Custom -> {
                        if (playlistId != null) {
                            val playlist = playlistUseCases.getPlaylistByIdUseCase.invoke(id = playlistId)
                            startPlayer(
                                receivedPlaylist = playlist,
                                position = position,
                                skipPauseClick = true,
                                startWithShuffle = false,
                                playerPosition = playerPosition,
                                context = context
                            )
                        }
                    }
                    PlaylistType.Undefined -> {

                    }
                    else -> {}
                }

            }
            PlayerType.Radio -> {

            }
            PlayerType.Undefined -> {

            }
            else -> {}
        }
    }

    private suspend fun startPlayer(
        receivedPlaylist: Playlist,
        position: Int,
        startWithShuffle: Boolean = false,
        skipPauseClick: Boolean = false,
        playerPosition: Long,
        context: Context
    ) = withContext(Dispatchers.Main) {
        if (receivedPlaylist.tracks.size == 0) {
            return@withContext
        }
        PlayerService.startService(context, ServiceContentWrapper(
            type = PlayerType.LocalTrack,
            position = position,
            playlist = receivedPlaylist,
            playerPosition = playerPosition,
            startWithShuffle = startWithShuffle,
            skipPauseClick = skipPauseClick,
            fromSavedState = true
            )
        )
    }
}