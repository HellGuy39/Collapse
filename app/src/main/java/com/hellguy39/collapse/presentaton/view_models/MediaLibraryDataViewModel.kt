package com.hellguy39.collapse.presentaton.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.favourites.FavouriteTracksUseCases
import com.hellguy39.domain.usecases.favourites.GetAllFavouriteTracksUseCase
import com.hellguy39.domain.usecases.tracks.GetAllTracksUseCase
import com.hellguy39.domain.usecases.playlist.PlaylistUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MediaLibraryDataViewModel @Inject constructor(
    private val getAllTracksUseCase: GetAllTracksUseCase,
    private val playlistUseCases: PlaylistUseCases,
    private val favouriteTracksUseCase: FavouriteTracksUseCases
) : ViewModel() {

    private val allTracksLiveData = MutableLiveData<List<Track>>()
    private val allPlaylistsLiveData = MutableLiveData<List<Playlist>>()
    private val allFavouriteTracksLiveData = MutableLiveData<List<Track>>()

    init {
        fetchAllTrackList()
        fetchAllPlaylists()
        fetchAllFavouriteTracks()
    }

    fun getAllTracks(): LiveData<List<Track>> = allTracksLiveData

    fun getAllPlaylists(): LiveData<List<Playlist>> = allPlaylistsLiveData

    fun getAllFavouriteTracks(): LiveData<List<Track>> = allFavouriteTracksLiveData

    fun updatePlaylists() {
        fetchAllPlaylists()
    }

    fun updateFavouriteTracks(){
        fetchAllFavouriteTracks()
    }

    fun addToFavourites(track: Track) = viewModelScope.launch(Dispatchers.IO) {
        favouriteTracksUseCase.addFavouriteTrackUseCase.invoke(track = track)
        updateFavouriteTracks()
    }

    fun addNewPlaylist(playlist: Playlist) = viewModelScope.launch(Dispatchers.IO) {
        playlistUseCases.addPlaylistUseCase.invoke(playlist = playlist)
        updatePlaylists()
    }

    fun deletePlaylist(playlist: Playlist) = viewModelScope.launch(Dispatchers.IO) {
        playlistUseCases.deletePlaylistUseCase.invoke(playlist = playlist)
        updatePlaylists()
    }

    private fun fetchAllTrackList() = viewModelScope.launch(Dispatchers.IO) {
        val tracks = getAllTracksUseCase.invoke()
        withContext(Dispatchers.Main) {
            allTracksLiveData.value = tracks
        }
    }

    private fun fetchAllPlaylists() = viewModelScope.launch(Dispatchers.IO) {
        val playlists = playlistUseCases.getAllPlaylistsUseCase.invoke()
        withContext(Dispatchers.Main) {
            allPlaylistsLiveData.value = playlists
        }
    }

    private fun fetchAllFavouriteTracks() = viewModelScope.launch(Dispatchers.IO) {
        val favouriteTracks = favouriteTracksUseCase.getAllFavouriteTracksUseCase.invoke()
        withContext(Dispatchers.Main) {
            allFavouriteTracksLiveData.value = favouriteTracks
        }
    }

    fun searchWithQueryInTrackList(query: String = "", trackList: List<Track>?): List<Track> {
        if (trackList.isNullOrEmpty())
            return listOf()

        val returnableList = mutableListOf<Track>()

        for (n in trackList.indices) {
            if (trackList[n].name.contains(query, true) ||
                trackList[n].artist.contains(query, true)) {

                returnableList.add(trackList[n])
            }
        }

        return returnableList
    }

}