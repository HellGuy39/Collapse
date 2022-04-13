package com.hellguy39.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedTracks(
    var trackList: MutableList<Track>
): Parcelable
