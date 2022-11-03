package com.hellguy39.collapse.albums.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.collapse.core.domain.wrapper.AlbumUseCaseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val albumUseCaseWrapper: AlbumUseCaseWrapper
): ViewModel() {

    private val _uiState = MutableStateFlow(AlbumDetailFragmentState(null, listOf()))
    val uiState = _uiState.asStateFlow()

    fun fetchData(albumId: Long) = viewModelScope.launch(Dispatchers.IO) {
        val songs = albumUseCaseWrapper.getAllSongsOfAlbumUseCase.invoke(albumId)
        val album = albumUseCaseWrapper.getAlbumByIdUseCase.invoke(albumId)

        withContext(Dispatchers.Main) {
            _uiState.update { it.copy(album = album, songs = songs) }
        }
    }

}