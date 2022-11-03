package com.hellguy39.collapse.presentaton.view_models

//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.hellguy39.domain.models.Playlist
//import com.hellguy39.domain.models.Track
//
//class PlaylistSharedViewModel: ViewModel() {
//
//    private var pictureLiveData = MutableLiveData<ByteArray>()
//
//    private var selectedTracksLiveData = MutableLiveData<MutableList<Track>>()
//
//    fun clearData() {
//        pictureLiveData.value = byteArrayOf()
//        selectedTracksLiveData.value = mutableListOf()
//    }
//
//    fun updateExistingPlaylist(playlist: Playlist) {
//        playlist.picture?.let { pictureLiveData.value = it }
//        selectedTracksLiveData.value = playlist.tracks.ifEmpty { mutableListOf() }
//    }
//
//    fun updatePicture(byteArray: ByteArray) {
//        pictureLiveData.value = byteArray
//    }
//
//    fun addTrack(track: Track) {
//        selectedTracksLiveData.value?.add(track)
//    }
//
//    fun removeTrack(track: Track) {
//        selectedTracksLiveData.value?.remove(track)
//    }
//
//    fun getPicture(): LiveData<ByteArray> = pictureLiveData
//
//    fun getSelectedTracks(): LiveData<MutableList<Track>> = selectedTracksLiveData
//
//}