package com.hellguy39.collapse.presentaton.fragments.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.usecases.playlist.PlaylistUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlaylistsViewModel @Inject constructor(
    private val playlistUseCases: PlaylistUseCases
) : ViewModel() {

    private val playlistsLiveData = MutableLiveData<List<Playlist>>()

    init {
        fetchPlaylists()
    }

    fun getPlaylists(): LiveData<List<Playlist>> = playlistsLiveData

    private fun fetchPlaylists() = viewModelScope.launch(Dispatchers.IO) {
        val playlists = playlistUseCases.getAllPlaylistsUseCase.invoke()
        withContext(Dispatchers.Main) {
            playlistsLiveData.value = playlists
        }
    }

}