package com.hellguy39.collapse.albums.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.activityViewModels
import com.hellguy39.collapse.albums.databinding.FragmentAlbumListBinding
import com.hellguy39.collapse.albums.navigateToDetail
import com.hellguy39.collapse.core.model.Album
import com.hellguy39.collapse.core.ui.extensions.clearAndUpdateDataSet
import com.hellguy39.collapse.core.ui.extensions.doOnApplyWindowInsets
import com.hellguy39.collapse.core.ui.extensions.repeatOnLifecycleStarted
import com.hellguy39.collapse.core.ui.extensions.setGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumListFragment : Fragment(), AlbumAdapter.Callback {

    private var binding: FragmentAlbumListBinding? = null

    private var albumList: MutableList<Album> = mutableListOf()

    private val albumListViewModel: AlbumListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumListBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

        repeatOnLifecycleStarted {
            launch {
                albumListViewModel.uiState.collect {
                    it.albums.let { albums ->
                        if(albums.isNotEmpty()) {

                            binding?.listContent?.rvAlbums?.clearAndUpdateDataSet(
                                albumList,
                                albums
                            )

                            setContentVisible(true)
                        } else {
                            setContentVisible(false)
                        }
                    }
                }
            }
        }
    }

    private fun setupUI() {
        binding?.run {
            listContent.rvAlbums.setGridLayoutManager()
            listContent.rvAlbums.adapter = AlbumAdapter(this@AlbumListFragment, albumList)
            listContent.scrollContentLayout.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
                view.updatePadding(
                    top = rect.top + windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                )
                windowInsetsCompat
            }
            emptyContent.root.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
                view.updatePadding(
                    top = rect.top + windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                )
                windowInsetsCompat
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = AlbumListFragment()
    }

    override fun onAlbumClick(album: Album, view: View) {
        navigateToDetail(album.id ?: return)
    }
}