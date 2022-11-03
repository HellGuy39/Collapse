package com.hellguy39.collapse.albums.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.hellguy39.collapse.albums.databinding.FragmentAlbumDetailBinding
import com.hellguy39.collapse.core.model.Song
import com.hellguy39.collapse.core.ui.adapter.SongListAdapter
import com.hellguy39.collapse.core.ui.extensions.repeatOnLifecycleStarted
import com.hellguy39.collapse.core.ui.extensions.setVerticalLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumDetailFragment : Fragment(), SongListAdapter.Callback {

    private var binding: FragmentAlbumDetailBinding? = null

    private val args: AlbumDetailFragmentArgs by navArgs()

    private val viewModel: AlbumDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        args.let {
            viewModel.fetchData(it.albumId)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumDetailBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

        repeatOnLifecycleStarted {
            launch {
                viewModel.uiState.collect { state ->
                    binding?.run {
                        collapsingToolbarLayout.title = state.album?.album
                    }
                    if (state.songs.isNotEmpty()) {
                        binding?.run {
                            rvSongs.adapter = SongListAdapter(state.songs,this@AlbumDetailFragment)
                        }
                    }
                }
            }
        }

    }

    private fun setupUI() {
        binding?.run {
            rvSongs.setVerticalLayoutManager()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = AlbumDetailFragment()
    }

    override fun onSongClick(playlist: List<Song>, startPosition: Int) {

    }
}