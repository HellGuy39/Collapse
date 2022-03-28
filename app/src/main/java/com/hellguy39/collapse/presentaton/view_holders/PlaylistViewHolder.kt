package com.hellguy39.collapse.presentaton.view_holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.databinding.PlaylistItemBinding
import com.hellguy39.collapse.presentaton.adapters.PlaylistsAdapter
import com.hellguy39.domain.models.Playlist

class PlaylistViewHolder(v: View): RecyclerView.ViewHolder(v) {

    private val binding = PlaylistItemBinding.bind(v)

    fun onBind(playlist: Playlist, listener: PlaylistsAdapter.OnPlaylistListener) {

        binding.tvTittle.text = playlist.name

        binding.root.setOnClickListener {
            listener.onPlaylistClick(playlist = playlist)
        }

    }

}