package com.hellguy39.collapse.presentaton.adapters

import android.content.res.Resources
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.PlaylistItemBinding
import com.hellguy39.data.models.Playlist

class PlaylistViewHolder(v: View): RecyclerView.ViewHolder(v) {
    private val _binding = PlaylistItemBinding.bind(v)

    fun bind(playlist: Playlist, resources: Resources) {
        _binding.tvTittle.text = playlist.tittle

        if (playlist.image != null)
            _binding.ivPlaylistCover.setImageBitmap(playlist.image)
        else
            _binding.ivPlaylistCover.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_round_audiotrack_24,
                    null
                )
            )

        _binding.root.setOnClickListener {
            Log.i("EVENT", "CLICKED")
        }
    }
}