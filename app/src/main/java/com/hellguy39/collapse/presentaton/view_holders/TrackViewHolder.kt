package com.hellguy39.collapse.presentaton.view_holders

import android.content.Context
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.TrackItemBinding
import com.hellguy39.collapse.presentaton.adapters.TracksAdapter
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.GetImageBitmapUseCase
import com.hellguy39.domain.utils.PlaylistType

class TrackViewHolder(
    v: View,
    private val getImageBitmapUseCase: GetImageBitmapUseCase,
    private val context: Context
): RecyclerView.ViewHolder(v) {

    private val binding = TrackItemBinding.bind(v)

    fun bind(
        track: Track,
        position: Int,
        type: Enum<PlaylistType>,
        listener: TracksAdapter.OnTrackListener,
        isPlaying: Boolean
    ) {

        binding.tvTrackName.text = track.name//.ifEmpty { "Unknown" }
        binding.tvAuthor.text = track.artist//.ifEmpty { "Unknown" }

        val bitmap = getImageBitmapUseCase.invoke(track.path)

        if (bitmap != null)
            binding.ivTrackImage.setImageBitmap(bitmap)
        else
            binding.ivTrackImage.setImageResource(R.drawable.ic_round_audiotrack_24)

        binding.root.setOnClickListener {
            listener.onTrackClick(track, position)
        }

        binding.ibMore.setOnClickListener {
            listener.onTrackMenuClick(track = track, position = position, playlistType = type)
        }

        if(isPlaying) {
            binding.root.setBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.gray_25, null))
        } else {
            binding.root.setBackgroundColor(0)
        }

    }
}