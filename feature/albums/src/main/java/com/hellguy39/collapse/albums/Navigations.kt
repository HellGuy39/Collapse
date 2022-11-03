package com.hellguy39.collapse.albums

import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.hellguy39.collapse.albums.list.AlbumListFragment
import com.hellguy39.collapse.albums.list.AlbumListFragmentDirections

fun AlbumListFragment.navigateToDetail(albumId: Long) {
    val directions = AlbumListFragmentDirections.actionAlbumListFragmentToAlbumDetailFragment(albumId)
    val extras = FragmentNavigatorExtras()
    findNavController().navigate(directions, extras)
}