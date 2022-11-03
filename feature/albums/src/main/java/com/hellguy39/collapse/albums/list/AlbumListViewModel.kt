package com.hellguy39.collapse.albums.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.collapse.core.domain.wrapper.AlbumUseCaseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AlbumListViewModel @Inject constructor(
    private val albumUseCaseWrapper: AlbumUseCaseWrapper
): ViewModel() {

    private val _uiState = MutableStateFlow(AlbumListFragmentState(listOf()))
    val uiState = _uiState.asStateFlow()

    init {
        fetchAlbums()
    }

    fun reloadData() {
        fetchAlbums()
    }

    private fun fetchAlbums() = viewModelScope.launch(Dispatchers.IO) {
        val albums = albumUseCaseWrapper.getAllAlbumsUseCase.invoke()

        withContext(Dispatchers.Main) {
            _uiState.compareAndSet(
                _uiState.value,
                _uiState.value.copy(
                    albums = albums
                )
            )
        }
    }

}