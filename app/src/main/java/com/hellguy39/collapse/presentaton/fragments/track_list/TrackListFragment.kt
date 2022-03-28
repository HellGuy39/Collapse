package com.hellguy39.collapse.presentaton.fragments.track_list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.TrackListFragmentBinding
import com.hellguy39.collapse.presentaton.adapters.TrackListAdapter
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
class TrackListFragment : Fragment(R.layout.track_list_fragment),TrackListAdapter.OnTrackListener {

    companion object {
        fun newInstance() = TrackListFragment()
    }

    @Inject
    lateinit var getImageBitmapUseCase: GetImageBitmapUseCase

    private lateinit var viewModel: TrackListViewModel
    private lateinit var binding: TrackListFragmentBinding

    private val args: TrackListFragmentArgs by navArgs()
    private lateinit var playlist: Playlist
    private var tracks = mutableListOf<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[TrackListViewModel::class.java]
        playlist = args.playlist
        tracks = playlist.tracks.toMutableList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TrackListFragmentBinding.bind(view)
        binding.rvTrackList.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvTrackList.adapter = TrackListAdapter(
            trackList = tracks,
            resources = resources,
            listener = this,
            getImageBitmapUseCase = getImageBitmapUseCase
        )
        binding.collapseToolbar.title = playlist.name

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        update()
    }

    private fun update() {
        if (playlist.type == PlaylistType.AllTracks) {
            viewModel.updateTrackList()
            viewModel.getTrackList().observe(viewLifecycleOwner) {
                for (n in it.indices) {
                    tracks.add(it[n])
                }
                binding.rvTrackList.adapter?.notifyItemInserted(tracks.size)
            }
        }
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