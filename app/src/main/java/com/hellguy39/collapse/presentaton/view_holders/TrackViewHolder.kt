package com.hellguy39.collapse.presentaton.view_holders

//import android.content.Context
//import android.view.View
//import androidx.core.content.res.ResourcesCompat
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.resource.bitmap.RoundedCorners
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
//import com.bumptech.glide.request.RequestOptions
//import com.hellguy39.collapse.R
//import com.hellguy39.collapse.databinding.TrackItemBinding
//import com.hellguy39.collapse.presentaton.adapters.TracksAdapter
//import com.hellguy39.collapse.utils.formatForDisplaying
//import com.hellguy39.collapse.utils.getImageOfTrackByPath
//import com.hellguy39.domain.models.Track
//import com.hellguy39.domain.utils.PlaylistType
//
//class TrackViewHolder(
//    v: View,
//    private val context: Context
//): RecyclerView.ViewHolder(v) {
//
//    private val binding = TrackItemBinding.bind(v)
//
//    fun bind(
//        track: Track,
//        position: Int,
//        type: Enum<PlaylistType>,
//        listener: TracksAdapter.OnTrackListener,
//        isPlaying: Boolean
//    ) {
//        binding.tvTrackName.text = track.name.formatForDisplaying()
//        binding.tvAuthor.text = track.artist.formatForDisplaying()
//
//        Glide.with(binding.root)
//            .load(getImageOfTrackByPath(track.path))
//            .placeholder(R.drawable.ic_round_audiotrack_24)
//            .error(R.drawable.ic_round_audiotrack_24)
//            //.transition(DrawableTransitionOptions.withCrossFade())
//            .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
//            .into(binding.ivTrackImage)
//
//        binding.root.setOnClickListener {
//            listener.onTrackClick(track, position)
//        }
//
//        binding.ibMore.setOnClickListener {
//            listener.onTrackMenuClick(track = track, position = position, playlistType = type)
//        }
//
//        if(isPlaying) {
//            binding.root.setBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.gray_25, null))
//        } else {
//            binding.root.setBackgroundColor(0)
//        }
//
//    }
//}