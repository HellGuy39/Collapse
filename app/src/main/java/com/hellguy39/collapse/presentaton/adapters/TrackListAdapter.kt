package com.hellguy39.collapse.presentaton.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.R
import com.hellguy39.domain.models.Track

class TrackListAdapter(
    private val trackList: List<Track>,
    private val resources: Resources,
    private val listener: OnTrackListener
): RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent,false)
        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: TrackViewHolder,
        position: Int
    ) {
        holder.bind(trackList[position], resources, position, listener)
    }

    override fun getItemCount(): Int = trackList.size

    interface OnTrackListener {
        fun onTrackClick(pos: Int)
    }
}