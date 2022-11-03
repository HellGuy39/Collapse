package com.hellguy39.collapse.songs.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.collapse.core.domain.usecase.song.GetAllSongsUseCase
import com.hellguy39.collapse.core.domain.wrapper.ArtistUseCaseWrapper
import com.hellguy39.collapse.core.domain.wrapper.SongUseCaseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SongFragmentViewModel @Inject constructor(
    private val songUseCaseWrapper: SongUseCaseWrapper
): ViewModel() {

    private val _uiState = MutableStateFlow(SongListFragmentState(listOf()))
    val uiState = _uiState.asStateFlow()

    init {
        fetchSongs()
    }

    fun reloadData() {
        fetchSongs()
    }

    private fun fetchSongs() = viewModelScope.launch(Dispatchers.IO) {

        val songs = songUseCaseWrapper.getAllSongsUseCase.invoke()

        withContext(Dispatchers.Main) {
            _uiState.compareAndSet(
                _uiState.value,
                _uiState.value.copy(
                    songs = songs
                )
            )
        }
    }

}