package com.hellguy39.collapse.presentaton.fragments.create_playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hellguy39.domain.models.Track

class CreatePlaylistViewModel: ViewModel() {

    private val picture = MutableLiveData<ByteArray>()

    private val selectedTracks = MutableLiveData<List<Track>>()

    fun setPicture(bytes: ByteArray) {
        picture.value = bytes
    }

    fun getPicture(): LiveData<ByteArray> = picture

    fun getSelectedTracks(): LiveData<List<Track>> = selectedTracks

    fun setSelectedTracks(tracks: List<Track>) {
        selectedTracks.value = tracks
    }

}