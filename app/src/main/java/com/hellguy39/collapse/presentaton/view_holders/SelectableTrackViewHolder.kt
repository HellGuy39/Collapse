package com.hellguy39.collapse.presentaton.view_holders

import android.util.Log
import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.SelectableTrackItemBinding
import com.hellguy39.collapse.presentaton.adapters.SelectableTracksAdapter
import com.hellguy39.collapse.utils.formatForDisplaying
import com.hellguy39.collapse.utils.getImageOfTrackByPath
import com.hellguy39.domain.models.Track

class SelectableTrackViewHolder(
    v: View,
): RecyclerView.ViewHolder(v) {

    private val binding = SelectableTrackItemBinding.bind(v)

    fun onBind(
        track: Track,
        listener: SelectableTracksAdapter.OnSelectableTrackListener,
        position: Int,
        isSelected: Boolean
    ) {
        binding.tvTrackName.text = track.name.formatForDisplaying()
        binding.tvAuthor.text = track.artist.formatForDisplaying()

        Glide.with(binding.root)
            .load(getImageOfTrackByPath(track.path))
            .placeholder(R.drawable.ic_round_audiotrack_24)
            .error(R.drawable.ic_round_audiotrack_24)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
            .into(binding.ivTrackImage)

        binding.cbSelect.setOnClickListener {
            if ((it as CheckBox).isChecked)
                listener.onSelectTrack(track)
            else
                listener.onUnselectTrack(track)
        }

        binding.root.setOnClickListener {
            if (!binding.cbSelect.isChecked) {
                binding.cbSelect.isChecked = true
                listener.onSelectTrack(track)
            } else {
                binding.cbSelect.isChecked = false
                listener.onUnselectTrack(track)
            }
        }

        if (isSelected)
            binding.cbSelect.isChecked = true
    }
}