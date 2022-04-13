package com.hellguy39.collapse.presentaton.adapters

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.R
import com.hellguy39.collapse.presentaton.view_holders.TrackViewHolder
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.GetImageBitmapUseCase
import com.hellguy39.domain.usecases.favourites.FavouriteTracksUseCases
import com.hellguy39.domain.utils.PlaylistType

class TracksAdapter(
    private val trackList: List<Track>,
    private val resources: Resources,
    private val listener: OnTrackListener,
    private val context: Context,
    private val getImageBitmapUseCase: GetImageBitmapUseCase,
    private val favouriteTracksUseCases: FavouriteTracksUseCases,
    private val playlistType: Enum<PlaylistType>,
): RecyclerView.Adapter<TrackViewHolder>() {

    private var playingItem = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent,false)
        return TrackViewHolder(
            v = itemView,
            getImageBitmapUseCase = getImageBitmapUseCase,
            favouriteTracksUseCases = favouriteTracksUseCases,
            context = context
        )
    }

    override fun onBindViewHolder(
        holder: TrackViewHolder,
        position: Int
    ) {
        holder.bind(
            track = trackList[position],
            position = position,
            listener = listener,
            type = playlistType,
            isPlaying = playingItem == position
        )
    }

    override fun getItemCount(): Int = trackList.size

    interface OnTrackListener {
        fun onTrackClick(track: Track, position: Int)
        fun onAddToFavourites(track: Track)
        fun onDeleteFromPlaylist(track: Track, position: Int)
    }

    fun updatePlayingItem(pos: Int) {

        val previousItem = playingItem
        playingItem = pos

        if (previousItem != -1) {
            notifyItemChanged(previousItem)
        }

        notifyItemChanged(pos)
    }

}