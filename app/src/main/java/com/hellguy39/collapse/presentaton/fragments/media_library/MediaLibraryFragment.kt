package com.hellguy39.collapse.presentaton.fragments.media_library

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.transition.platform.MaterialFadeThrough
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

        reenterTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MediaLibraryFragmentBinding.bind(view)

        setupListeners()
    }

    private fun setupListeners() {
        binding.cardAllTracks.setOnClickListener(this)
        binding.cardFavourites.setOnClickListener(this)
        binding.cardArtists.setOnClickListener(this)
        binding.cardPlaylists.setOnClickListener(this)
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { permission ->
        if (permission == true) {
            (activity as MainActivity).initSetupMediaLibraryDataViewModel()
        }
    }

    private fun showDialogPermission() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Permission")
            .setMessage("Need to get permission to read data from local storage")
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Get it!") { dialog, which ->
                dialog.dismiss()
                requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            .setIcon(R.drawable.ic_baseline_perm_media_24)
            .show()
    }

    private fun isPermissionGranted(): Boolean =
        (ActivityCompat.checkSelfPermission(
            activity as MainActivity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)


    override fun onClick(p0: View?) {

        if (!isPermissionGranted())
            return showDialogPermission()

        when(p0?.id) {
            R.id.cardArtists -> {
                navigateToArtists()
            }
            R.id.cardAllTracks -> {
                navigateToAllTracks()
            }
            R.id.cardPlaylists -> {
                navigateToPlaylists()
            }
            R.id.cardFavourites -> {
                navigateToFavourites()
            }
        }
    }

    private fun navigateToAllTracks() = findNavController().navigate(
        MediaLibraryFragmentDirections.actionMediaLibraryFragmentToTrackListFragment(
            Playlist(
                name = "All tracks",
                type = PlaylistType.AllTracks
            )
        )
    )

    private fun navigateToPlaylists() = findNavController().navigate(
        MediaLibraryFragmentDirections.actionMediaLibraryFragmentToPlaylistFragment()
    )

    private fun navigateToFavourites() = findNavController().navigate(
        MediaLibraryFragmentDirections.actionMediaLibraryFragmentToTrackListFragment(
            Playlist(
                name = "Favourites",
                type = PlaylistType.Favourites
            )
        )
    )

    private fun navigateToArtists() = findNavController().navigate(
        MediaLibraryFragmentDirections.actionMediaLibraryFragmentToArtistsListFragment()
    )
}