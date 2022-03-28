package com.hellguy39.collapse.presentaton.fragments.media_library

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.MediaLibraryFragmentBinding
import com.hellguy39.collapse.presentaton.activities.main.MainActivity
import com.hellguy39.collapse.presentaton.adapters.TrackListAdapter
import com.hellguy39.collapse.presentaton.fragments.playlists.PlaylistsFragmentDirections
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.ServiceContentWrapper
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.GetImageBitmapUseCase
import com.hellguy39.domain.utils.PlayerType
import com.hellguy39.domain.utils.PlaylistType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MediaLibraryFragment : Fragment(R.layout.media_library_fragment), View.OnClickListener/*, TrackListAdapter.OnTrackListener*/ {

    companion object {
        fun newInstance() = MediaLibraryFragment()
    }

    @Inject
    lateinit var getImageBitmapUseCase: GetImageBitmapUseCase

    private lateinit var searchView: SearchView
    private val viewModel: MediaLibraryViewModel by viewModels()
    private lateinit var binding: MediaLibraryFragmentBinding
    private var tracks: MutableList<Track> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MediaLibraryFragmentBinding.bind(view)

//        tracks.clear()
//
//        binding.rvTracks.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            adapter = TrackListAdapter(
//                trackList = tracks,
//                resources = resources,
//                listener = this@MediaLibraryFragment,
//                getImageBitmapUseCase = getImageBitmapUseCase
//            )
//        }

/*        val searchItem = binding.topAppBar.menu.findItem(R.id.search)

        searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                viewModel.updateTrackList(p0)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                viewModel.updateTrackList(p0)
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
        }*/

        binding.cardAllTracks.setOnClickListener(this)
        binding.cardFavourites.setOnClickListener(this)
        binding.cardArtists.setOnClickListener(this)
        binding.cardPlaylists.setOnClickListener(this)

        binding.btnGetPermission.setOnClickListener {
            requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        checkPermission()
    }

//    private fun setObservers() {
//        viewModel.getTrackList().observe(viewLifecycleOwner) { receivedTracks ->
//            updateRvTracks(receivedTracks)
//        }
//        PlayerService.getContentPosition().observe(viewLifecycleOwner) {
//
//        }
//    }

    /*private fun updateRvTracks(receivedTracks: List<Track>) {

        clearRecyclerView()

        for (n in receivedTracks.indices) {
            tracks.add(receivedTracks[n])
        }

        val position = binding.rvTracks.adapter?.itemCount ?: 0
        binding.rvTracks.adapter?.notifyItemInserted(position)
    }*/

    private fun showCardPermission(b: Boolean) {
        if (b) {
            binding.cardPermission.visibility = View.VISIBLE
        } else {
            binding.cardPermission.visibility = View.GONE
        }
    }

    private fun showMediaLibraryCards(b: Boolean) {
        if (b) {
            binding.cardPlaylists.visibility = View.VISIBLE
            binding.cardArtists.visibility = View.VISIBLE
            binding.cardFavourites.visibility = View.VISIBLE
            binding.cardAllTracks.visibility = View.VISIBLE
        } else {
            binding.cardPlaylists.visibility = View.GONE
            binding.cardArtists.visibility = View.GONE
            binding.cardFavourites.visibility = View.GONE
            binding.cardAllTracks.visibility = View.GONE
        }
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { permission ->
        if (permission == true)
        {
            checkPermission()
        }
        /*else
        {
            Toast.makeText(requireContext(), "Access denied", Toast.LENGTH_LONG).show()
        }*/
    }

    private fun checkPermission() {
        if (isPermissionGranted()) {
            showCardPermission(false)
            showMediaLibraryCards(true)
        } else {
            showCardPermission(true)
            showMediaLibraryCards(false)
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
                findNavController()
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
                findNavController().navigate(R.id.action_mediaLibraryFragment_to_playlistFragment)
            }
            R.id.cardFavourites -> {
                findNavController()
            }
        }
    }

    /*override fun onTrackClick(pos: Int) {
        PlayerService.startService(requireContext(), ServiceContentWrapper(
            type = PlayerType.LocalTrack,
            position = pos,
            trackList = tracks,
            )
        )
    }
*/
    /*private fun clearRecyclerView() {
        val size = binding.rvTracks.adapter?.itemCount
        tracks.clear()
        binding.rvTracks.adapter?.notifyItemRangeRemoved(0, size ?: 0)
    }
*/
}