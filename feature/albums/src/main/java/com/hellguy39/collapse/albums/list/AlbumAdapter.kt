package com.hellguy39.collapse.albums.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.albums.R
import com.hellguy39.collapse.albums.databinding.ItemAlbumBinding
import com.hellguy39.collapse.core.model.Album
import com.hellguy39.collapse.core.ui.extensions.setImageAsync

class AlbumAdapter(
    private val callback: Callback,
    private val dataSet: List<Album>
): RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    interface Callback {
        fun onAlbumClick(album: Album, view: View)
    }

    inner class AlbumViewHolder(
        private val binding: ItemAlbumBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {

            binding.run {
                tvAlbumName.text = album.album
                tvArtistName.text = album.artist
                ivAlbumCover.setImageAsync(album.contentUri)

                root.setOnClickListener {
                    callback.onAlbumClick(album, binding.root)
                }
            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumViewHolder {
        return AlbumViewHolder(
            ItemAlbumBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_album, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int = dataSet.size

}