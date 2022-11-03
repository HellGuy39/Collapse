package com.hellguy39.collapse.artists.detail

import com.hellguy39.collapse.core.model.Artist
import com.hellguy39.collapse.core.model.Song

data class ArtistDetailFragmentState(
    val artist: Artist?,
    val songs: List<Song>
)
