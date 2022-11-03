package com.hellguy39.collapse.artists.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.collapse.core.domain.wrapper.ArtistUseCaseWrapper
import com.hellguy39.collapse.core.model.Artist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val artistUseCaseWrapper: ArtistUseCaseWrapper
): ViewModel() {

    private val _uiDetailState = MutableStateFlow(ArtistDetailFragmentState(null, listOf()))
    val uiDetailState = _uiDetailState.asStateFlow()

    fun fetchArtistData(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        val artist = artistUseCaseWrapper.getArtistByIdUseCase.invoke(id)
        val songs = artistUseCaseWrapper.getAllSongsOfArtistUseCase.invoke(id)

        withContext(Dispatchers.Main) {
            _uiDetailState.compareAndSet(
                _uiDetailState.value,
                _uiDetailState.value.copy(
                    artist = artist,
                    songs = songs
                )
            )
        }
    }

}