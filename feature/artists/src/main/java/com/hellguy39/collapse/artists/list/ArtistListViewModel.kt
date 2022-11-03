package com.hellguy39.collapse.artists.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.collapse.core.domain.wrapper.ArtistUseCaseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistListViewModel @Inject constructor(
    private val artistUseCaseWrapper: ArtistUseCaseWrapper
): ViewModel() {

    private val _uiListState = MutableStateFlow(ArtistListFragmentState(listOf()))
    val uiListState = _uiListState.asStateFlow()

    init {
        fetchArtists()
    }

    fun reloadData() {
        fetchArtists()
    }

    private fun fetchArtists() = viewModelScope.launch(Dispatchers.IO) {
        val artists = artistUseCaseWrapper.getAllArtistsUseCase.invoke()

        _uiListState.compareAndSet(
            _uiListState.value,
            _uiListState.value.copy(
                artists = artists
            )
        )
    }


}