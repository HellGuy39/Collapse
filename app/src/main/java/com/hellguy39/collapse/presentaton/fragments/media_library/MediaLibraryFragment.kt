package com.hellguy39.collapse.presentaton.fragments.media_library

import android.Manifest
import android.animation.LayoutTransition
import android.content.pm.PackageManager
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.platform.MaterialFade
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.MediaLibraryFragmentBinding
import com.hellguy39.collapse.presentaton.activities.main.MainActivity
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.usecases.GetImageBitmapUseCase
import com.hellguy39.domain.utils.PlaylistType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MediaLibraryFragment : Fragment(R.layout.media_library_fragment), View.OnClickListener {

    companion object {
        fun newInstance() = MediaLibraryFragment()
    }

    @Inject
    lateinit var getImageBitmapUseCase: GetImageBitmapUseCase

    private lateinit var binding: MediaLibraryFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X,true)
        reenterTransition = MaterialFadeThrough()//MaterialSharedAxis(MaterialSharedAxis.X,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MediaLibraryFragmentBinding.bind(view)

        binding.btnGetPermission.setOnClickListener {
            requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        binding.layoutCardPermission.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        checkPermission()
    }


    private fun showCardPermission(b: Boolean) {
        TransitionManager.beginDelayedTransition(binding.layoutCardPermission, AutoTransition())
        if (b) {
            binding.cardPermission.visibility = View.VISIBLE
        } else {
            binding.cardPermission.visibility = View.GONE
        }
    }

    private fun enableMediaLibraryCards(b: Boolean) {
        if (b) {
            binding.cardAllTracks.setOnClickListener(this)
            binding.cardFavourites.setOnClickListener(this)
            binding.cardArtists.setOnClickListener(this)
            binding.cardPlaylists.setOnClickListener(this)
        } else {
            binding.cardAllTracks.setOnClickListener(null)
            binding.cardFavourites.setOnClickListener(null)
            binding.cardArtists.setOnClickListener(null)
            binding.cardPlaylists.setOnClickListener(null)
        }
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { permission ->
        if (permission == true)
        {
            checkPermission()
            (activity as MainActivity).initSetupMediaLibraryDataViewModel()
        }
    }

    private fun checkPermission() {
        if (isPermissionGranted()) {
            showCardPermission(false)
            enableMediaLibraryCards(true)
        } else {
            showCardPermission(true)
            enableMediaLibraryCards(false)
        }
    }

    private fun isPermissionGranted() : Boolean  {
        return (ActivityCompat.checkSelfPermission(
                activity as MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.cardArtists -> {
                findNavController().navigate(
                    MediaLibraryFragmentDirections.actionMediaLibraryFragmentToArtistsListFragment()
                )
            }
            R.id.cardAllTracks -> {
                findNavController().navigate(
                    MediaLibraryFragmentDirections.actionMediaLibraryFragmentToTrackListFragment(
                        Playlist(
                            name = "All tracks",
                            type = PlaylistType.AllTracks
                        )
                    )
                )
            }
            R.id.cardPlaylists -> {
                findNavController().navigate(
                    MediaLibraryFragmentDirections.actionMediaLibraryFragmentToPlaylistFragment()
                )
            }
            R.id.cardFavourites -> {
                findNavController().navigate(
                    MediaLibraryFragmentDirections.actionMediaLibraryFragmentToTrackListFragment(
                        Playlist(
                            name = "Favourites",
                            type = PlaylistType.Favourites
                        )
                    )
                )
            }
        }
    }
}