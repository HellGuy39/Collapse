package com.hellguy39.collapse.presentaton.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.databinding.RadioItemBinding
import com.hellguy39.domain.models.RadioStation

class RadioStationViewHolder(v: View): RecyclerView.ViewHolder(v){

    private val binding = RadioItemBinding.bind(v)

    fun bind(
        radioStation: RadioStation,
        listener: RadioStationListAdapter.OnRadioStationListener,
        position: Int
    ) {

        binding.tvStationName.text = radioStation.name

        binding.root.setOnClickListener {
            listener.onStationClick(position)
        }

    }

}