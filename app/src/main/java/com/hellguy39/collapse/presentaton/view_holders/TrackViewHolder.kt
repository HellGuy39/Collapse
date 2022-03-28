package com.hellguy39.collapse.presentaton.view_holders

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.TrackItemBinding
import com.hellguy39.collapse.presentaton.adapters.TracksAdapter
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.GetImageBitmapUseCase

class TrackViewHolder(
    v: View,
    private val getImageBitmapUseCase: GetImageBitmapUseCase
): RecyclerView.ViewHolder(v) {

    private val binding = TrackItemBinding.bind(v)

    fun bind(
        track: Track,
        resources: Resources,
        position: Int,
        listener: TracksAdapter.OnTrackListener
    ) {

        binding.tvTrackName.text = track.name//.ifEmpty { "Unknown" }
        binding.tvAuthor.text = track.artist//.ifEmpty { "Unknown" }

        val bitmap = getImageBitmapUseCase.invoke(track.path)

        if (bitmap != null)
            binding.ivTrackImage.setImageBitmap(bitmap)
        else
            binding.ivTrackImage.setImageResource(R.drawable.ic_round_audiotrack_24)

        binding.root.setOnClickListener {
            listener.onTrackClick(position)
        }

    }
}