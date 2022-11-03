package com.hellguy39.collapse.presentaton.view_holders

//import android.view.View
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.resource.bitmap.RoundedCorners
//import com.bumptech.glide.request.RequestOptions
//import com.hellguy39.collapse.R
//import com.hellguy39.collapse.databinding.PlaylistItemGridViewBinding
//import com.hellguy39.collapse.databinding.PlaylistItemListViewBinding
//import com.hellguy39.collapse.presentaton.adapters.PlaylistsAdapter
//import com.hellguy39.domain.utils.LayoutType
//import com.hellguy39.collapse.utils.toBitmap
//import com.hellguy39.domain.models.Playlist

//class PlaylistViewHolder(private val v: View): RecyclerView.ViewHolder(v) {
//
//    fun onBind(
//        playlist: Playlist,
//        listener: PlaylistsAdapter.OnPlaylistListener,
//        layoutType: LayoutType
//    ) {
//        if (layoutType == LayoutType.Grid) {
//            val bindingGridItem = PlaylistItemGridViewBinding.bind(v)
//
//            bindingGridItem.tvTittle.text = playlist.name
//
//            bindingGridItem.root.setOnClickListener {
//                listener.onPlaylistClick(playlist = playlist, it)
//            }
//
//            Glide.with(bindingGridItem.root)
//                .load(playlist.picture?.toBitmap())
//                .placeholder(R.drawable.ic_round_queue_music_24)
//                .into(bindingGridItem.ivPlaylistCover)
//
//        } else {
//            val bindingListItem = PlaylistItemListViewBinding.bind(v)
//
//            bindingListItem.tvTittle.text = playlist.name
//
//            bindingListItem.root.setOnClickListener {
//                listener.onPlaylistClick(playlist = playlist, it)
//            }
//
//            Glide.with(bindingListItem.root)
//                .load(playlist.picture?.toBitmap())
//                .placeholder(R.drawable.ic_round_queue_music_24)
//                .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
//                .into(bindingListItem.ivPlaylistCover)
//        }
//
//    }
//
//
////    private fun updateCardBackground(bitmap: Bitmap) {
////        Palette.from(bitmap).generate { palette ->
////            if (palette != null) {
////                palette.vibrantSwatch?.let {
////                    binding.root.backgroundTintList = ColorStateList.valueOf(it.rgb)
////                    binding.tvTittle.setTextColor(it.titleTextColor)
////                }
////            }
////        }
////    }
//}