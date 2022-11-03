package com.hellguy39.collapse.presentaton.view_holders

//import android.view.View
//import androidx.recyclerview.widget.RecyclerView
//import com.hellguy39.collapse.databinding.ArtistItemBinding
//import com.hellguy39.collapse.presentaton.adapters.ArtistsAdapter
//import com.hellguy39.domain.models.Playlist
//
//class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//    private val binding = ArtistItemBinding.bind(itemView)
//
//    fun onBind(
//        playlist: Playlist,
//        listener: ArtistsAdapter.OnArtistListener,
//        position: Int
//    ) {
//        binding.root.setOnClickListener {
//            listener.onArtistClick(position)
//        }
//        binding.tvArtistName.text = playlist.name
//        val itemCount = playlist.tracks.size
//
//        if (itemCount == 1)
//            binding.tvTracksCount.text = "$itemCount track"
//        else if (itemCount > 1)
//            binding.tvTracksCount.text = "$itemCount tracks"
//
//    }
//}