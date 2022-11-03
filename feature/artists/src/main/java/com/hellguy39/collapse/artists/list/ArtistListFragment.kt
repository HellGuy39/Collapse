package com.hellguy39.collapse.artists.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hellguy39.collapse.artists.databinding.FragmentArtistListBinding
import com.hellguy39.collapse.artists.navigateToDetails
import com.hellguy39.collapse.core.model.Artist
import com.hellguy39.collapse.core.ui.extensions.clearAndUpdateDataSet
import com.hellguy39.collapse.core.ui.extensions.doOnApplyWindowInsets
import com.hellguy39.collapse.core.ui.extensions.repeatOnLifecycleStarted
import com.hellguy39.collapse.core.ui.extensions.setGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArtistListFragment : Fragment(), ArtistListAdapter.Callback {

    private var binding: FragmentArtistListBinding? = null
    private val artistListViewModel by activityViewModels<ArtistListViewModel>()

    private var artistList = mutableListOf<Artist>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArtistListBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        setupUI()

        repeatOnLifecycleStarted {
            launch {
                artistListViewModel.uiListState.collect { uiState ->
                    uiState.artists.let { artists ->
                        if (artists.isEmpty()) {
                            setContentVisible(false)
                        } else {
                            if (artists != artistList) {
                                binding?.listContent?.rvArtists?.clearAndUpdateDataSet(
                                    artistList,
                                    artists
                                )
                            }
                            setContentVisible(true)
                        }
                    }
                }
            }
        }

    }

    private fun setupUI() {
        binding?.run {
            listContent.rvArtists.setGridLayoutManager(ArtistListAdapter.SPAN_COUNT)
//            tvSongsCount.text = "${songs.count()} Songs"
//            btnSort.text = "Name"
            listContent.rvArtists.adapter = ArtistListAdapter(
                artistList,
                this@ArtistListFragment
            )

            root.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
                view.updatePadding(
                    top = rect.top + windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                )
                windowInsetsCompat
            }



//            appBarLayout.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
//                view.updatePadding(
//                    top = rect.top + windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).top,
//                )
//                windowInsetsCompat
//            }

//            listContent.scrollContentLayout.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
//                view.updatePadding(
//                    top = rect.top + windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).top,
//                )
//                windowInsetsCompat
//            }
//            emptyContent.root.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
//                view.updatePadding(
//                    top = rect.top + windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).top,
//                )
//                windowInsetsCompat
//            }
        }
    }

    private fun setContentVisible(isVisible: Boolean) {
        if (isVisible) {
            binding?.run {
                if (listContent.root.visibility != View.VISIBLE) {
                    listContent.root.visibility = View.VISIBLE
                    emptyContent.root.visibility = View.GONE
                }
            }
        } else {
            binding?.run {
                if (listContent.root.visibility != View.GONE) {
                    listContent.root.visibility = View.GONE
                    emptyContent.root.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onArtistClick(artist: Artist,  view: View) {
        navigateToDetails(artist, view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = ArtistListFragment()
    }

}