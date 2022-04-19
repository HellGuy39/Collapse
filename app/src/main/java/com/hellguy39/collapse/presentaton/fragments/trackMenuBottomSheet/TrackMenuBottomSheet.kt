package com.hellguy39.collapse.presentaton.fragments.trackMenuBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.TrackMenuBottomSheetBinding
import com.hellguy39.collapse.presentaton.fragments.track_list.TrackMenuEvents
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.GetImageBitmapUseCase
import com.hellguy39.domain.usecases.favourites.IsTrackFavouriteUseCase
import com.hellguy39.domain.utils.PlaylistType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackMenuBottomSheet(
    private val track: Track,
    private val position:Int,
    private val playlistType: Enum<PlaylistType>,
    private val listener: TrackMenuEvents,
    private val getImageBitmapUseCase: GetImageBitmapUseCase,
    private val isTrackFavouriteUseCase: IsTrackFavouriteUseCase
): BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: TrackMenuBottomSheetBinding
    private var isFavourite: Boolean = false

    companion object {
        const val TRACK_MENU_TAG = "track_menu_tag"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.track_menu_bottom_sheet, container, false)
        binding = TrackMenuBottomSheetBinding.bind(rootView)
        onBind()
        return rootView
    }

    private fun onBind() {
        setupHeader()
        setupMenu()
        isTrackFavourite(track)
    }

    private fun setupHeader() {
        val bitmap = getImageBitmapUseCase.invoke(track.path)

        if (bitmap != null)
            binding.ivCover.setImageBitmap(bitmap)
        else
            binding.ivCover.setImageResource(R.drawable.ic_round_audiotrack_24)

        binding.tvTrackName.text = track.name
        binding.tvArtist.text = track.artist
    }

    private fun setupMenu() {
        when(playlistType) {
            PlaylistType.Custom -> {}
            PlaylistType.Favourites -> {
                binding.cardIsFavourite.isVisible = false
            }
            PlaylistType.AllTracks -> {
                binding.cardDeleteFromPlaylist.isVisible = false
            }
            PlaylistType.Artist -> {
                binding.cardDeleteFromPlaylist.isVisible = false
            }
        }
        binding.cardDeleteFromPlaylist.setOnClickListener(this)
        binding.cardIsFavourite.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            binding.cardDeleteFromPlaylist.id -> {
                listener.onDeleteFromPlaylist(track, position)
                this.dismiss()
            }
            binding.cardIsFavourite.id -> {
                if (isFavourite) {
                    isFavourite = false
                    listener.onDeleteFromFavourites(track)
                    setIsFavouriteCardUI(isFavourite)
                } else {
                    isFavourite = true
                    listener.onAddToFavourites(track)
                    setIsFavouriteCardUI(isFavourite)
                }
            }
        }
    }

    private fun setIsFavouriteCardUI(isFavourite: Boolean) {
        if (isFavourite) {
            binding.ivFavourites.setImageResource(R.drawable.ic_round_favorite_24)
            binding.tvAddToFavorites.text = "Delete from favourites"
        } else {
            binding.ivFavourites.setImageResource(R.drawable.ic_round_favorite_border_24)
            binding.tvAddToFavorites.text = "Add to favourites"
        }
    }

    private fun isTrackFavourite(track: Track) = CoroutineScope(Dispatchers.IO).launch {
        isFavourite = isTrackFavouriteUseCase.invoke(track)
        withContext(Dispatchers.Main) {
            setIsFavouriteCardUI(isFavourite)
        }
    }
}