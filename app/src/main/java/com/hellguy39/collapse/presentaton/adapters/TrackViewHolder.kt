package com.hellguy39.collapse.presentaton.adapters

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.TrackItemBinding
import com.hellguy39.domain.models.Track

class TrackViewHolder(v: View): RecyclerView.ViewHolder(v) {

    private val binding = TrackItemBinding.bind(v)

    fun bind(
        track: Track,
        resources: Resources,
        position: Int,
        listener: TrackListAdapter.OnTrackListener
    ) {

        binding.tvTrackName.text = track.name//.ifEmpty { "Unknown" }
        binding.tvAuthor.text = track.artist//.ifEmpty { "Unknown" }

        val bytes = track.embeddedPicture

        if (bytes != null)
            binding.ivTrackImage.setImageBitmap(BitmapFactory.decodeByteArray(track.embeddedPicture, 0, bytes.size))
        else
            binding.ivTrackImage.setImageResource(R.drawable.ic_round_audiotrack_24)

        binding.root.setOnClickListener {
            listener.onTrackClick(position)
        }

    }
}