package com.hellguy39.collapse.presentaton.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.domain.models.Artist
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.favourites.FavouriteTracksUseCases
import com.hellguy39.domain.usecases.playlist.PlaylistUseCases
import com.hellguy39.domain.usecases.tracks.GetAllTracksUseCase
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
    private val allArtistsLiveData = MutableLiveData<List<Artist>>()

    init {
        initSetup()
    }

    fun initSetup() = viewModelScope.launch(Dispatchers.IO) {
        fetchAllTrackList()
        fetchAllPlaylists()
        fetchAllFavouriteTracks()
        fetchAllArtists()
    }

    fun getAllTracks(): LiveData<List<Track>> = allTracksLiveData

    fun getAllPlaylists(): LiveData<List<Playlist>> = allPlaylistsLiveData

    fun getAllFavouriteTracks(): LiveData<List<Track>> = allFavouriteTracksLiveData

    fun getAllArtists(): LiveData<List<Artist>> = allArtistsLiveData

    fun getTrackListFromArtist(artist: Artist): List<Track> {
        val artists = getAllArtists().value ?: return listOf()
        val index = artists.indexOf(artist)

        return if (index != -1) {
            artists[index].trackList
        } else
            listOf()
    }

    private fun updatePlaylists() = viewModelScope.launch(Dispatchers.IO) {
        fetchAllPlaylists()
    }

    private fun updateFavouriteTracks() = viewModelScope.launch(Dispatchers.IO) {
        fetchAllFavouriteTracks()
    }

    fun addToFavourites(track: Track) = viewModelScope.launch(Dispatchers.IO) {
        favouriteTracksUseCase.addFavouriteTrackUseCase.invoke(track = track)
        updateFavouriteTracks()
    }

    fun deleteFromPlaylist(track: Track, playlist: Playlist) = viewModelScope.launch(Dispatchers.IO) {
        val updatedList = mutableListOf<Track>()
        updatedList.addAll(playlist.tracks)
        updatedList.remove(track)
        playlist.tracks = updatedList
        playlistUseCases.updatePlaylistUseCase.invoke(playlist)
    }

    fun addNewPlaylist(playlist: Playlist) = viewModelScope.launch(Dispatchers.IO) {
        playlistUseCases.addPlaylistUseCase.invoke(playlist = playlist)
        updatePlaylists()
    }

    fun deletePlaylist(playlist: Playlist) = viewModelScope.launch(Dispatchers.IO) {
        playlistUseCases.deletePlaylistUseCase.invoke(playlist = playlist)
        updatePlaylists()
    }

    private suspend fun fetchAllTrackList() {
        val tracks = getAllTracksUseCase.invoke()

        updateValue(allTracksLiveData, tracks)
    }

    private suspend fun fetchAllPlaylists() {
        val playlists = playlistUseCases.getAllPlaylistsUseCase.invoke()

        updateValue(allPlaylistsLiveData, playlists)
    }

    private suspend fun fetchAllFavouriteTracks()  {
        val favouriteTracks = favouriteTracksUseCase.getAllFavouriteTracksUseCase.invoke()

        updateValue(allFavouriteTracksLiveData, favouriteTracks)
    }

    private suspend fun fetchAllArtists()  {
        val tracks = allTracksLiveData.value
        val artists = mutableListOf<Artist>()
        val artistsName = mutableListOf<String>()

        if (tracks != null) {
            for (n in tracks.indices) {
                if (!artistsName.contains(tracks[n].artist)) {
                    val newList = mutableListOf(tracks[n])
                    artistsName.add(tracks[n].artist)
                    artists.add(Artist(name = tracks[n].artist, trackList = newList))
                } else {
                    for (n2 in artists.indices) {
                        if (artists[n2].name == tracks[n].artist) {
                            artists[n2].trackList.add(tracks[n])
                        }
                    }
                }
            }
        }

        updateValue(allArtistsLiveData, artists)
    }

    private suspend fun updateValue(
        variable: MutableLiveData<*>,
        value: Any
    ) = withContext(Dispatchers.Main) {
        variable.value = value
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

    fun searchWithQueryInArtists(query: String = "", artists: List<Artist>?): List<Artist> {
        if (artists.isNullOrEmpty())
            return listOf()

        val returnableList = mutableListOf<Artist>()

        for (n in artists.indices) {
            if (artists[n].name.contains(query, true)) {
                returnableList.add(artists[n])
            }
        }

        return returnableList
    }

    fun searchWithQueryInPlaylists(query: String = "", playlists: List<Playlist>?): List<Playlist> {
        if (playlists.isNullOrEmpty())
            return listOf()

        val returnableList = mutableListOf<Playlist>()

        for (n in playlists.indices) {
            if (playlists[n].name.contains(query, true)) {
                returnableList.add(playlists[n])
            }
        }

        return returnableList
    }
}