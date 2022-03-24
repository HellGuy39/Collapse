package com.hellguy39.collapse.presentaton.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.R
import com.hellguy39.domain.models.RadioStation

class RadioStationListAdapter(
    private val stations: List<RadioStation>,
    private val listener: OnRadioStationListener
): RecyclerView.Adapter<RadioStationViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RadioStationViewHolder {
        return RadioStationViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.radio_item,parent,false)
        )
    }

    override fun onBindViewHolder(
        holder: RadioStationViewHolder,
        position: Int
    ) {
        holder.bind(
            radioStation = stations[position],
            listener = listener,
            position = position
        )
    }

    override fun getItemCount(): Int = stations.size

    interface OnRadioStationListener {
        fun onStationClick(pos: Int)
    }
}