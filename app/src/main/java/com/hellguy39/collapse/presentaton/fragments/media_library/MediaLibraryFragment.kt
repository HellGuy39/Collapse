package com.hellguy39.collapse.presentaton.fragments.media_library

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.MediaLibraryFragmentBinding
import com.hellguy39.collapse.presentaton.activities.main.MainActivity
import com.hellguy39.collapse.presentaton.adapters.TrackListAdapter
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.collapse.presentaton.services.ServiceContentWrapper
import com.hellguy39.domain.models.Track
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class MediaLibraryFragment : Fragment(R.layout.media_library_fragment) {

    companion object {
        fun newInstance() = MediaLibraryFragment()
    }

    private val _viewModel: MediaLibraryViewModel by viewModels()
    private lateinit var _binding: MediaLibraryFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = MediaLibraryFragmentBinding.bind(view)
        //setObservers()

        val tracks = fetchLocalTracks()

        _binding.rvTracks.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = TrackListAdapter(trackList = tracks, resources = resources)
        }

        PlayerService.startService(requireContext(), contentWrapper = ServiceContentWrapper(
            0,
            tracks
        ))

        /*Log.d("DEBUG", "LIST: $tracks")

        val player = ExoPlayer.Builder(requireContext()).build()
        val mediaItem = MediaItem.fromUri(tracks[0].path)

        player.addMediaItem(mediaItem)

        player.prepare()
        player.play()*/
    }

    private fun setObservers() {
        /*_viewModel.getTracksList().observe(viewLifecycleOwner) {
            _binding.rvTracks.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = TrackListAdapter(trackList = it, resources = resources)
            }

            loadTrack()
            //PlayerService.startService(requireContext(), contentWrapper = ServiceContentWrapper(0, it))

        }*/
    }

    private val checkPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { permission ->
        if (permission == true)
        {
            loadTrack()
        }
        else
        {

        }
    }

    private fun fetchLocalTracks(): MutableList<Track> {
        val audioDataList: MutableList<Track> = mutableListOf()

        val contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection: Array<String> = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        val cursor = context?.contentResolver?.query(
            contentUri,
            projection,
            selection,
            null,
            null
        ) ?: return mutableListOf()

        while (cursor.moveToNext()) {
            audioDataList.add(Track(
                name = cursor.getString(0),
                path = cursor.getString(1),
            ))
        }

        cursor.close()

        return audioDataList
    }

    private fun loadTrack() {
        if (ActivityCompat.checkSelfPermission(activity as MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {

        } else {
            checkPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    }

    private fun navigateToTrack(track: Track) = findNavController()
        .navigate(MediaLibraryFragmentDirections.actionMediaLibraryFragmentToTrackFragment(track))

}