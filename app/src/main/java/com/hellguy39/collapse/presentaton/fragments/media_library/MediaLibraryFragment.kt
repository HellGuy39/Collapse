package com.hellguy39.collapse.presentaton.fragments.media_library

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.MediaLibraryFragmentBinding
import com.hellguy39.collapse.presentaton.activities.main.MainActivity
import com.hellguy39.collapse.presentaton.adapters.TrackListAdapter
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.domain.models.ServiceContentWrapper
import com.hellguy39.domain.utils.PlayerType
import com.hellguy39.domain.models.Track
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MediaLibraryFragment : Fragment(R.layout.media_library_fragment), TrackListAdapter.OnTrackListener {

    companion object {
        fun newInstance() = MediaLibraryFragment()
    }

    private val _viewModel: MediaLibraryViewModel by viewModels()
    private lateinit var _binding: MediaLibraryFragmentBinding
    private var tracks: MutableList<Track> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = MediaLibraryFragmentBinding.bind(view)

        tracks.clear()

        _binding.rvTracks.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = TrackListAdapter(trackList = tracks, resources = resources, this@MediaLibraryFragment)
        }

        setObservers()
        checkPermission()
    }

    private fun setObservers() {
        _viewModel.getTrackList().observe(viewLifecycleOwner) { receivedTracks ->
            updateRvTracks(receivedTracks)
        }
    }

    private fun updateRvTracks(receivedTracks: List<Track>) {

        tracks.clear()

        for (n in receivedTracks.indices) {
            tracks.add(receivedTracks[n])
        }

        val position = _binding.rvTracks.adapter?.itemCount ?: 0
        _binding.rvTracks.adapter?.notifyItemInserted(position)
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { permission ->
        if (permission == true)
        {
            checkPermission()
        }
        else
        {
            Toast.makeText(requireContext(), "Access denied", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission() {
        if (isPermissionGranted()) {
            _viewModel.updateTrackList()
        } else {
            requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun isPermissionGranted() : Boolean  {
        return (ActivityCompat.checkSelfPermission(
                activity as MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED)
    }

    override fun onTrackClick(pos: Int) {
        PlayerService.stopService(requireContext())
        PlayerService.startService(requireContext(), ServiceContentWrapper(
            type = PlayerType.LocalTrack,
            position = pos,
            trackList = tracks
        )
        )
    }

}