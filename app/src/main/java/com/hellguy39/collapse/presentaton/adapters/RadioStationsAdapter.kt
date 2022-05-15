package com.hellguy39.collapse.presentaton.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.R
import com.hellguy39.collapse.presentaton.view_holders.RadioStationViewHolder
import com.hellguy39.domain.models.RadioStation

class RadioStationsAdapter(
    private val stations: List<RadioStation>,
    private val listener: OnRadioStationListener,
    private val context: Context
): RecyclerView.Adapter<RadioStationViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RadioStationViewHolder {
        return RadioStationViewHolder(
            v = LayoutInflater.from(parent.context)
                .inflate(R.layout.radio_item,parent,false),
            context = context
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
        fun onStationDelete(radioStation: RadioStation)
        fun onStationEdit(radioStation: RadioStation)
    }
}