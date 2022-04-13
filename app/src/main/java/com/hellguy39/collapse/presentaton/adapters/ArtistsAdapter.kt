package com.hellguy39.collapse.presentaton.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.R
import com.hellguy39.collapse.presentaton.view_holders.ArtistViewHolder
import com.hellguy39.domain.models.Playlist

class ArtistsAdapter(
    private val playlists: List<Playlist>,
    private val listener: OnArtistListener
): RecyclerView.Adapter<ArtistViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArtistViewHolder {
        return ArtistViewHolder(
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.artist_item, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ArtistViewHolder,
        position: Int
    ) {
       holder.onBind(
           playlist = playlists[position],
           position = position,
           listener = listener
       )
    }

    override fun getItemCount(): Int = playlists.size

    interface OnArtistListener {
        fun onArtistClick(position: Int)
    }
}