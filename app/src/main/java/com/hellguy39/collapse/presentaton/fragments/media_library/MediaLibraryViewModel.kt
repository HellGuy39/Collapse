package com.hellguy39.collapse.presentaton.fragments.media_library

import androidx.lifecycle.ViewModel
import com.hellguy39.domain.usecases.tracks.GetAllTracksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MediaLibraryViewModel @Inject constructor(
    private val getAllTracksUseCase: GetAllTracksUseCase
) : ViewModel() {

}