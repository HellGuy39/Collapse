package com.hellguy39.collapse.presentaton.view_holders

import android.content.Context
import android.content.res.Resources
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.TrackItemBinding
import com.hellguy39.collapse.presentaton.activities.main.MainActivity
import com.hellguy39.collapse.presentaton.adapters.TracksAdapter
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.GetImageBitmapUseCase
import com.hellguy39.domain.usecases.favourites.FavouriteTracksUseCases

class TrackViewHolder(
    v: View,
    private val getImageBitmapUseCase: GetImageBitmapUseCase,
    private val favouriteTracksUseCases: FavouriteTracksUseCases,
    private val context: Context
): RecyclerView.ViewHolder(v) {

    private val binding = TrackItemBinding.bind(v)

    fun bind(
        track: Track,
        resources: Resources,
        position: Int,
        listener: TracksAdapter.OnTrackListener
    ) {

        binding.tvTrackName.text = track.name//.ifEmpty { "Unknown" }
        binding.tvAuthor.text = track.artist//.ifEmpty { "Unknown" }

        val bitmap = getImageBitmapUseCase.invoke(track.path)

        if (bitmap != null)
            binding.ivTrackImage.setImageBitmap(bitmap)
        else
            binding.ivTrackImage.setImageResource(R.drawable.ic_round_audiotrack_24)

        binding.root.setOnClickListener {
            listener.onTrackClick(track)
        }

        binding.ibMore.setOnClickListener {
            showMenu(it, R.menu.track_item_menu, listener, track)
        }

    }

    private fun showMenu(
        v: View,
        @MenuRes menuRes: Int,
        listener: TracksAdapter.OnTrackListener,
        track: Track
    ) {
        val popup = PopupMenu(context,v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when(menuItem.itemId) {
                R.id.addToFavourites -> {
                    listener.onAddToFavourites(track = track)
                    true
                }
                else -> false
            }
        }
        popup.setOnDismissListener {

        }

        popup.show()
    }
}