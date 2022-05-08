package com.hellguy39.collapse.presentaton.view_holders

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.view.View
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.PlaylistItemBinding
import com.hellguy39.collapse.presentaton.adapters.PlaylistsAdapter
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.usecases.ConvertByteArrayToBitmapUseCase

class PlaylistViewHolder(v: View): RecyclerView.ViewHolder(v) {

    private val binding = PlaylistItemBinding.bind(v)

    fun onBind(
        playlist: Playlist,
        listener: PlaylistsAdapter.OnPlaylistListener,
        convertByteArrayToBitmapUseCase: ConvertByteArrayToBitmapUseCase
    ) {
        binding.tvTittle.text = playlist.name

        binding.root.setOnClickListener {
            listener.onPlaylistClick(playlist = playlist, it)
        }

        val bytes = playlist.picture

        if (bytes != null) {
            val bitmap = convertByteArrayToBitmapUseCase.invoke(bytes)
            binding.ivPlaylistCover.setImageBitmap(bitmap)
            //updateCardBackground(bitmap)
        } else
            binding.ivPlaylistCover.setImageResource(R.drawable.ic_round_queue_music_24)

    }


    private fun updateCardBackground(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            if (palette != null) {
                palette.vibrantSwatch?.let {
                    binding.root.backgroundTintList = ColorStateList.valueOf(it.rgb)
                    binding.tvTittle.setTextColor(it.titleTextColor)
                }
            }
        }
    }
}