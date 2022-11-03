package com.hellguy39.collapse.artists

import android.view.View
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.hellguy39.collapse.artists.list.ArtistListFragment
import com.hellguy39.collapse.artists.list.ArtistListFragmentDirections
import com.hellguy39.collapse.core.model.Artist

internal fun ArtistListFragment.navigateToDetails(
    artist: Artist, view: View
) {
    val directions = ArtistListFragmentDirections
        .actionArtistListFragmentToArtistDetailFragment(artist.id ?: 0)

    val extras = FragmentNavigatorExtras(
        view to getString(R.string.shared_artist_transition)
    )



//    Motion().run {
//        setTransition(FragmentTransition.Exit, TransitionType.Hold)
//    }

    findNavController().navigate(directions, extras)
}