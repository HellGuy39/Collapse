package com.hellguy39.collapse.presentaton.fragments.media_library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.GetAllTracksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MediaLibraryViewModel @Inject constructor(
    private val getAllTracksUseCase: GetAllTracksUseCase
) : ViewModel() {

    private val trackListLiveData = MutableLiveData<List<Track>>()

    fun getTrackList(): LiveData<List<Track>> {
        return trackListLiveData
    }

    fun updateTrackList() {
        fetchTrackList()
    }

    private fun fetchTrackList() = viewModelScope.launch(Dispatchers.IO) {
        val tracks = getAllTracksUseCase.invoke()
        withContext(Dispatchers.Main) {
            trackListLiveData.value = tracks
        }
    }
}