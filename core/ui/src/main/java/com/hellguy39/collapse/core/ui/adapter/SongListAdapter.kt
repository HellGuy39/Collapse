package com.hellguy39.collapse.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.core.model.Song
import com.hellguy39.collapse.core.ui.R
import com.hellguy39.collapse.core.ui.databinding.ItemSongBinding
import com.hellguy39.collapse.core.ui.extensions.formatToTimeString
import com.hellguy39.collapse.core.ui.extensions.setImageAsync

class SongListAdapter(
    private val dataSet: List<Song>,
    private val callback: Callback
) : RecyclerView.Adapter<SongListAdapter.SongViewHolder>() {

    interface Callback {
        fun onSongClick(playlist: List<Song>, startPosition: Int)
    }

    inner class SongViewHolder(
        private val binding: ItemSongBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song, position: Int) {
            binding.run {
                tvTitle.text = song.title
                tvArtist.text = song.artist
                tvDuration.text = song.duration.formatToTimeString()
                ivSongCover.setImageAsync(song.contentUri)
                root.setOnClickListener {
                    callback.onSongClick(dataSet, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SongViewHolder = SongViewHolder(
        ItemSongBinding.bind(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_song, parent, false)
        )
    )

    override fun onBindViewHolder(
        holder: SongViewHolder,
        position: Int
    ) {
        holder.bind(dataSet[position], position)
    }

    override fun getItemCount(): Int = dataSet.size
}