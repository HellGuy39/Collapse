package com.hellguy39.collapse.presentaton.fragments.trackMenuBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.PlaylistMenuBottomSheetBinding
import com.hellguy39.collapse.databinding.TrackMenuBottomSheetBinding
import com.hellguy39.collapse.presentaton.fragments.track_list.PlaylistMenuEvents
import com.hellguy39.collapse.presentaton.fragments.track_list.TrackMenuEvents
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.ConvertByteArrayToBitmapUseCase
import com.hellguy39.domain.usecases.GetImageBitmapUseCase
import com.hellguy39.domain.usecases.favourites.IsTrackFavouriteUseCase
import com.hellguy39.domain.utils.PlaylistType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistMenuBottomSheet(
   private val convertByteArrayToBitmapUseCase: ConvertByteArrayToBitmapUseCase,
   private val playlist: Playlist,
   private val listener: PlaylistMenuEvents
): BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: PlaylistMenuBottomSheetBinding

    companion object {
        const val PLAYLIST_MENU_TAG = "track_menu_tag"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.playlist_menu_bottom_sheet, container, false)
        binding = PlaylistMenuBottomSheetBinding.bind(rootView)
        onBind()
        return rootView
    }

    private fun onBind() {
        setupHeader()
        setupMenu()
    }

    private fun setupHeader() {
        val bytes = playlist.picture

        if (bytes != null) {
            val bitmap = convertByteArrayToBitmapUseCase.invoke(bytes)
            binding.ivCover.setImageBitmap(bitmap)
        } else
            binding.ivCover.setImageResource(R.drawable.ic_round_audiotrack_24)

        binding.tvPlaylistName.text = playlist.name

        val itemCount = playlist.tracks.size

        if (itemCount == 1)
            binding.tvTracksCount.text = "$itemCount track"
        else if (itemCount > 1)
            binding.tvTracksCount.text = "$itemCount tracks"
    }

    private fun setupMenu() {
        binding.cardDelete.setOnClickListener(this)
        binding.cardEdit.setOnClickListener(this)

        when(playlist.type) {
            PlaylistType.AllTracks -> {
                enablePlaylistMenu(false)
            }
            PlaylistType.Favourites -> {
                enablePlaylistMenu(false)
            }
            PlaylistType.Custom -> {
                enablePlaylistMenu(true)
            }
            PlaylistType.Artist -> {
                enablePlaylistMenu(false)
            }
            else -> {
                enablePlaylistMenu(false)
            }
        }

    }

    private fun enablePlaylistMenu(b: Boolean) {
        if (b) {
            binding.cardWarning.visibility = View.GONE
            binding.cardEdit.visibility = View.VISIBLE
            binding.cardDelete.visibility = View.VISIBLE
        } else {
            binding.cardWarning.visibility = View.VISIBLE
            binding.cardEdit.visibility = View.GONE
            binding.cardDelete.visibility = View.GONE
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            binding.cardDelete.id -> {
                listener.onDeletePlaylist()
                this.dismiss()
            }
            binding.cardEdit.id -> {
                listener.onEditPlaylist()
                this.dismiss()
            }
        }
    }
}