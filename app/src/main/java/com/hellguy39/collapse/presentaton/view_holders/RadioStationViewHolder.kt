package com.hellguy39.collapse.presentaton.view_holders

import android.content.Context
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.RadioItemBinding
import com.hellguy39.collapse.presentaton.adapters.RadioStationsAdapter
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.usecases.ConvertByteArrayToBitmapUseCase

class RadioStationViewHolder(
    v: View,
    private val convertByteArrayToBitmapUseCase: ConvertByteArrayToBitmapUseCase,
    private val context: Context
): RecyclerView.ViewHolder(v){

    private val binding = RadioItemBinding.bind(v)

    fun bind(
        radioStation: RadioStation,
        listener: RadioStationsAdapter.OnRadioStationListener,
        position: Int
    ) {

        val bytes = radioStation.picture

        if (bytes != null) {
            val bitmap = convertByteArrayToBitmapUseCase.invoke(bytes)
            binding.ivStationCover.setImageBitmap(bitmap)
        } else
            binding.ivStationCover.setImageResource(R.drawable.ic_round_radio_24)

        binding.tvTittle.text = radioStation.name

        binding.root.setOnClickListener {
            listener.onStationClick(position)
        }

        binding.ibMore.setOnClickListener {
            showMenu(
                v = it,
                menuRes = R.menu.radio_station_item_menu,
                listener = listener,
                radioStation = radioStation
            )
        }

    }

    private fun showMenu(
        v: View,
        @MenuRes menuRes: Int,
        listener: RadioStationsAdapter.OnRadioStationListener,
        radioStation: RadioStation
    ) {
        val popup = PopupMenu(context,v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when(menuItem.itemId) {
                R.id.edit -> {
                    listener.onStationEdit(radioStation = radioStation)
                    true
                }
                R.id.delete -> {
                    listener.onStationDelete(radioStation = radioStation)
                    true
                }
                else -> false
            }
        }
        popup.setOnDismissListener {

        }

        popup.show()
    }

}