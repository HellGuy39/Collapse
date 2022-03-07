package com.hellguy39.collapse.presentaton.adapters

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.databinding.TrackItemBinding
import com.hellguy39.domain.models.Track

class TrackViewHolder(v: View): RecyclerView.ViewHolder(v) {

    private val _binding = TrackItemBinding.bind(v)

    fun bind(track: Track, resources: Resources) {

    }
}