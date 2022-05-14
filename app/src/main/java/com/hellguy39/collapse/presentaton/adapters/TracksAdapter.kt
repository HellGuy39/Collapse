package com.hellguy39.collapse.presentaton.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.R
import com.hellguy39.collapse.presentaton.view_holders.TrackViewHolder
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.utils.PlaylistType

class TracksAdapter(
    private val trackList: List<Track>,
    private val listener: OnTrackListener,
    private val context: Context,
    private val playlistType: Enum<PlaylistType>,
): RecyclerView.Adapter<TrackViewHolder>() {

    companion object {
        const val NO_PLAYING_ITEM = -1
    }

    private var playingItem = NO_PLAYING_ITEM

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent,false)
        return TrackViewHolder(
            v = itemView,
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
        fun onTrackMenuClick(track: Track, position: Int, playlistType: Enum<PlaylistType>)
        fun onTrackClick(track: Track, position: Int)
    }

    fun updatePlayingItem(pos: Int) {

        val previousItem = playingItem
        playingItem = pos

        if (previousItem != NO_PLAYING_ITEM) {
            notifyItemChanged(previousItem)
        }

        notifyItemChanged(pos)
    }

}