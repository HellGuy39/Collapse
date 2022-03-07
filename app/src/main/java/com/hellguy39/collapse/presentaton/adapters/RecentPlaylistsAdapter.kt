package com.hellguy39.collapse.presentaton.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.R
import com.hellguy39.data.models.Playlist

class RecentPlaylistsAdapter(
    private val playlistList: List<Playlist>,
    private val resources: Resources
): RecyclerView.Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_item,parent,false)

        return PlaylistViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: PlaylistViewHolder,
        position: Int,
    ) {
        val playlist: Playlist = playlistList[position]
        holder.bind(playlist, resources)
    }

    override fun getItemCount(): Int = playlistList.size

}
