package com.hellguy39.collapse.artists.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.artists.R
import com.hellguy39.collapse.artists.databinding.ItemArtistBinding
import com.hellguy39.collapse.core.model.Artist
import com.hellguy39.collapse.core.ui.extensions.setImageAsync
import com.hellguy39.collapse.core.ui.extensions.setImageTest

class ArtistListAdapter(
    private val dataSet: List<Artist>,
    private val callback: Callback
) : RecyclerView.Adapter<ArtistListAdapter.ArtistViewHolder>() {

    interface Callback {
        fun onArtistClick(artist: Artist, view: View)
    }

    inner class ArtistViewHolder(
        private val binding: ItemArtistBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(artist: Artist) {

            binding.run {
                tvTitle.text = artist.artist

                ivArtistCover.setImageTest()
                root.transitionName = "${root.context.getString(R.string.shared_artist_transition)}${artist.id}"

                //ivArtistCover.setArtistImage()//setImageAsync(artist.contentUri)
                root.setOnClickListener {
                    callback.onArtistClick(artist, it)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArtistViewHolder = ArtistViewHolder(
        ItemArtistBinding.bind(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_artist, parent, false)
        )
    )

    override fun onBindViewHolder(
        holder: ArtistViewHolder,
        position: Int
    ) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int = dataSet.size

    companion object {
        const val SPAN_COUNT = 2
    }
}