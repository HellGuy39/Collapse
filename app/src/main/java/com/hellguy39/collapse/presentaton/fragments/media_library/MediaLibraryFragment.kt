package com.hellguy39.collapse.presentaton.fragments.media_library

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
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

    private lateinit var searchView: SearchView
    private val viewModel: MediaLibraryViewModel by viewModels()
    private lateinit var binding: MediaLibraryFragmentBinding
    private var tracks: MutableList<Track> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MediaLibraryFragmentBinding.bind(view)

        tracks.clear()

        binding.rvTracks.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = TrackListAdapter(trackList = tracks, resources = resources, this@MediaLibraryFragment)
        }

        val searchItem = binding.topAppBar.menu.findItem(R.id.search)

        searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                return false
            }

        })


        binding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.search -> {
                    true
                }
                R.id.filter -> {
                    true
                }
                else -> false
            }
        }

        setObservers()
        checkPermission()
    }

    private fun setObservers() {
        viewModel.getTrackList().observe(viewLifecycleOwner) { receivedTracks ->
            updateRvTracks(receivedTracks)
        }
        PlayerService.getContentPosition().observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "POS: $it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateRvTracks(receivedTracks: List<Track>) {

        tracks.clear()

        for (n in receivedTracks.indices) {
            tracks.add(receivedTracks[n])
        }

        val position = binding.rvTracks.adapter?.itemCount ?: 0
        binding.rvTracks.adapter?.notifyItemInserted(position)
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
            viewModel.updateTrackList()
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
        PlayerService.startService(requireContext(), ServiceContentWrapper(
            type = PlayerType.LocalTrack,
            position = pos,
            trackList = tracks,
            )
        )
    }

}