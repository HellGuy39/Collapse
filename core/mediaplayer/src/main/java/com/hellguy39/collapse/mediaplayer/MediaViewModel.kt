package com.hellguy39.collapse.mediaplayer

import androidx.lifecycle.ViewModel
import com.hellguy39.collapse.core.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MediaViewModel: ViewModel() {

    private val _song = MutableStateFlow<Song?>(null)
    val song = _song.asStateFlow()

    fun setSong(song: Song) {
        _song.update { song }
    }

}