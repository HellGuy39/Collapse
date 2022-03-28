package com.hellguy39.collapse.presentaton.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.R
import com.hellguy39.collapse.presentaton.view_holders.SelectableTrackViewHolder
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.GetImageBitmapUseCase

class SelectableTracksAdapter(
    private val tracks: List<Track>,
    private val getImageBitmapUseCase: GetImageBitmapUseCase,
    private val listener: OnSelectableTrackListener
): RecyclerView.Adapter<SelectableTrackViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectableTrackViewHolder {
        return SelectableTrackViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.selectable_track_item, parent, false))
    }

    override fun onBindViewHolder(
        holder: SelectableTrackViewHolder, position: Int
    ) {
        holder.onBind(
            track = tracks[position],
            getImageBitmapUseCase = getImageBitmapUseCase,
            position = position,
            listener = listener
        )
    }

    override fun getItemCount(): Int = tracks.size

    fun getTrackByPosition(position: Int): Track = tracks[position]

    interface OnSelectableTrackListener {
        fun onSelectTrack(position: Int)
        fun onUnselectTrack(position: Int)
    }
}