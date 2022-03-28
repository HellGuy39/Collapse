package com.hellguy39.collapse.presentaton.view_holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.SelectableTrackItemBinding
import com.hellguy39.collapse.presentaton.adapters.SelectableTracksAdapter
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.GetImageBitmapUseCase

class SelectableTrackViewHolder(
    v: View,
): RecyclerView.ViewHolder(v) {

    private val binding = SelectableTrackItemBinding.bind(v)

    fun onBind(
        track: Track,
        getImageBitmapUseCase: GetImageBitmapUseCase,
        listener: SelectableTracksAdapter.OnSelectableTrackListener,
        position: Int
    ) {
        binding.tvTrackName.text = track.name//.ifEmpty { "Unknown" }
        binding.tvAuthor.text = track.artist//.ifEmpty { "Unknown" }

        val bitmap = getImageBitmapUseCase.invoke(track.path)

        if (bitmap != null)
            binding.ivTrackImage.setImageBitmap(bitmap)
        else
            binding.ivTrackImage.setImageResource(R.drawable.ic_round_audiotrack_24)

        binding.cbSelect.setOnCheckedChangeListener { compoundButton, b ->
            if (b)
                listener.onSelectTrack(position = position)
            else
                listener.onUnselectTrack(position = position)
        }
    }

}