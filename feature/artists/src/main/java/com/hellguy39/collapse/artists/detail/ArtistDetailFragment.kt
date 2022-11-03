package com.hellguy39.collapse.artists.detail

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.hellguy39.collapse.artists.R
import com.hellguy39.collapse.artists.databinding.FragmentArtistDetailBinding
import com.hellguy39.collapse.core.model.Song
import com.hellguy39.collapse.core.ui.adapter.SongListAdapter
import com.hellguy39.collapse.core.ui.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArtistDetailFragment : Fragment(), SongListAdapter.Callback {

    private var binding: FragmentArtistDetailBinding? = null

    private val artistDetailViewModel: ArtistDetailViewModel by viewModels()

    private val args: ArtistDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        args.let {
            artistDetailViewModel.fetchArtistData(it.artistId)
        }

//        Motion().run {
//            setContainerTransformSharedElementTransition(
//                sharedElementTransition = SharedElementTransition.Enter,
//            )
//        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = container?.id ?: 0
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(
                requireActivity().theme.getColorByResId(
                    com.google.android.material.R.attr.colorSurface
                )
            )
            duration = 300L
        }

        binding = FragmentArtistDetailBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()

        repeatOnLifecycleStarted {
            launch {
                artistDetailViewModel.uiDetailState.collect {
                    it.artist?.let {
                        binding?.run {
                            collapsingToolbarLayout.title = it.artist

                            //tvArtistName.text = it.artist

                            ivArtist.setImageDrawable(
                                ResourcesCompat.getDrawable(
                                    resources,
                                    com.hellguy39.collapse.core.ui.R.drawable.test_artist_image,
                                    null
                                )
                            )
                        }
                    }
                    if (it.songs.isNotEmpty()) {
                        binding?.run {
                            rvSongs.adapter = SongListAdapter(it.songs,this@ArtistDetailFragment)

                            if (it.songs.size > 1) {
                                tvDescription.text = getString(
                                    com.hellguy39.collapse.core.ui.R.string.subtitle_songs_count,
                                    it.songs.size.toString(),
                                    it.songs.toTotalDateString()
                                )
                            } else {
                                tvDescription.text = getString(
                                    com.hellguy39.collapse.core.ui.R.string.subtitle_song_count,
                                    it.songs.size.toString(),
                                    it.songs.toTotalDateString()
                                )
                            }

                        }
                    }
                }
            }
        }

    }

    private fun setupUI() {
        binding?.run {
            rvSongs.setVerticalLayoutManager()
            //rvSongs.setVerticalDivider()
            appBarLayout.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
                view.updatePadding(
                    top = rect.top + windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                )
                windowInsetsCompat
            }
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
//            scrollContentLayout.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
//                view.updatePadding(
//                    bottom = rect.bottom + windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).bottom,
//                )
//                windowInsetsCompat
//            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = ArtistDetailFragment()
    }

    override fun onSongClick(playlist: List<Song>, startPosition: Int) {

    }
}