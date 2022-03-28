package com.hellguy39.collapse.presentaton.fragments.create_playlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.playlist.PlaylistUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePlaylistViewModel @Inject constructor(
    private val playlistUseCases: PlaylistUseCases
) : ViewModel() {

    private val tracksLiveData = MutableLiveData<List<Track>>()

    fun addNewPlaylist(playlist: Playlist) = viewModelScope.launch(Dispatchers.IO) {
        playlistUseCases.addPlaylistUseCase.invoke(playlist = playlist)
    }

    fun updateTrackList(receivedTracks: List<Track>) {
        tracksLiveData.value = receivedTracks
    }

    fun getTrackList(): LiveData<List<Track>> = tracksLiveData

}