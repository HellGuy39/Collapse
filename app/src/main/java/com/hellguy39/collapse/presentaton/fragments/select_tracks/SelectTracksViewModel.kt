package com.hellguy39.collapse.presentaton.fragments.select_tracks

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
class SelectTracksViewModel @Inject constructor(
    private val getAllTracksUseCase: GetAllTracksUseCase
    ): ViewModel() {
        private val trackListLiveData = MutableLiveData<List<Track>>()

        init {
            updateTrackList()
        }

        fun getTrackList(): LiveData<List<Track>> {
            return trackListLiveData
        }

        fun updateTrackList(args: String? = "") {
            fetchTrackList(args)
        }

        private fun fetchTrackList(args: String?) = viewModelScope.launch(Dispatchers.IO) {
            val tracks = getAllTracksUseCase.invoke(args = args)
            withContext(Dispatchers.Main) {
                trackListLiveData.value = tracks
            }
        }

}