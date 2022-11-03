package com.hellguy39.collapse.songs.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hellguy39.collapse.core.model.Song
import com.hellguy39.collapse.core.ui.adapter.SongListAdapter
import com.hellguy39.collapse.core.ui.extensions.doOnApplyWindowInsets
import com.hellguy39.collapse.core.ui.extensions.setVerticalLayoutManager
import com.hellguy39.collapse.mediaplayer.PlaybackService
import com.hellguy39.collapse.songs.R
import com.hellguy39.collapse.songs.databinding.FragmentSongListBinding
import com.hellguy39.collapse.songs.util.repeatOnLifecycleStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SongListFragment : Fragment(), SongListAdapter.Callback {

    private var binding: FragmentSongListBinding? = null
    private val songsViewModel: SongFragmentViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Motion().run {
//            setTransition(
//                fragmentTransition = FragmentTransition.Enter,
//                type = TransitionType.FadeThought
//            )
//            setTransition(
//                fragmentTransition = FragmentTransition.Reenter,
//                type = TransitionType.FadeThought
//            )
//        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongListBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

        repeatOnLifecycleStarted {
            launch {
                songsViewModel.uiState.collect {
                    it.songs.let { songs ->
                        if (songs.isEmpty()) {
                            setContentVisible(false)
                        } else {
                            setContentVisible(true)
                            binding?.listContent?.run {
                                rvSongs.adapter = SongListAdapter(
                                    songs,
                                    this@SongListFragment
                                )
                                tvSongsCount.text = "${songs.count()} Songs"
                                btnSort.text = "Name"
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupUI() {
        binding?.run {
            appBarLayout.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
                view.updatePadding(
                    top = rect.top + windowInsetsCompat
                        .getInsets(WindowInsetsCompat.Type.systemBars()).top
                )
                windowInsetsCompat
            }



            listContent.run {
                rvSongs.setVerticalLayoutManager()
//                scrollContentLayout.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
//                    view.updatePadding(
//                        top = rect.top + windowInsetsCompat
//                            .getInsets(WindowInsetsCompat.Type.systemBars()).top,
//                    )
//                    windowInsetsCompat
//                }
            }
            emptyContent.run {
                root.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
                    view.updatePadding(
                        top = rect.top + windowInsetsCompat
                            .getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    )
                    windowInsetsCompat
                }

                ivEmptyContent.setImageDrawable(ResourcesCompat.getDrawable(
                        resources,
                        com.hellguy39.collapse.core.ui.R.drawable.ic_baseline_queue_music_24,
                        null
                    )
                )
                emptyContent.tvTitle.text = getString(R.string.title_no_songs)
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

    override fun onSongClick(playlist: List<Song>, startPosition: Int) {
        //(requireActivity() as MainActivity).bindService(playlist)
        PlaybackService.bindService(
            requireActivity() as AppCompatActivity,
            playlist,
            startPosition
        )

//        PlaybackService.bindService(
//            requireActivity() as AppCompatActivity,
//            object : ServiceConnection {
//                override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
////                    val service = (p1 as PlaybackService.ServiceBinder).getPlaybackService()
////
////                    val mediaController = MediaControllerCompat(
////                        this,
////                        service.mediaSession?.token
////                    )
//                }
//
//                override fun onServiceDisconnected(p0: ComponentName?) {
//
//                }
//
//            }
//        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = SongListFragment()
    }
}