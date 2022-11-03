package com.hellguy39.collapse.albums.detail

import com.hellguy39.collapse.core.model.Album
import com.hellguy39.collapse.core.model.Song

data class AlbumDetailFragmentState(
    val album: Album?,
    val songs: List<Song>
)