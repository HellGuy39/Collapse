package com.hellguy39.collapse.presentaton.fragments.media_library

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.AddTrackUseCase
import com.hellguy39.domain.usecases.GetAllTracksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MediaLibraryViewModel @Inject constructor(

) : ViewModel() {

    /*private val trackListLiveData = MutableLiveData<List<Track>>()

    init {
        fetchTrackList()
    }

    fun getTracksList(): LiveData<List<Track>> {
        return trackListLiveData
    }

    fun addTrack(track: Track) = viewModelScope.launch(Dispatchers.IO) {
        addTrackUseCase.invoke(track)
        fetchTrackList()
    }

    private fun fetchTrackList() = viewModelScope.launch(Dispatchers.IO) {
        val tracks = getAllTracksUseCase.invoke()
        withContext(Dispatchers.Main) {
            trackListLiveData.value = tracks
        }
    }*/
}