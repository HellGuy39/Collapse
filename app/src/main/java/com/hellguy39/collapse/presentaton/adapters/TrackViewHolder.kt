package com.hellguy39.collapse.presentaton.adapters

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.TrackItemBinding
import com.hellguy39.domain.models.Track

class TrackViewHolder(v: View): RecyclerView.ViewHolder(v) {

    private val _binding = TrackItemBinding.bind(v)

    fun bind(track: Track, resources: Resources) {

        _binding.tvTrackName.text = track.name.ifEmpty { "Unknown" }
        _binding.tvAuthor.text = track.artist.ifEmpty { "Unknown" }

        val cover = track.embeddedPicture
        if (cover != null) {
            val bitmap = BitmapFactory.decodeByteArray(cover, 0, cover.size)
            _binding.ivTrackImage.setImageBitmap(bitmap)
        } else {
            _binding.ivTrackImage.setImageResource(R.drawable.ic_round_audiotrack_24)
        }
    }
}