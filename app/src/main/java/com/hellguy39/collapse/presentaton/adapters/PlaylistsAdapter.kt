package com.hellguy39.collapse.presentaton.adapters

//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.hellguy39.collapse.R
//import com.hellguy39.collapse.presentaton.view_holders.PlaylistViewHolder
//import com.hellguy39.domain.utils.LayoutType
//import com.hellguy39.domain.models.Playlist
//
//class PlaylistsAdapter(
//    private val playlists: List<Playlist>,
//    private val listener: OnPlaylistListener,
//    private val layoutType: LayoutType
//): RecyclerView.Adapter<PlaylistViewHolder>() {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
//        return if (layoutType == LayoutType.Grid) {
//            PlaylistViewHolder(
//                LayoutInflater.from(parent.context)
//                    .inflate(R.layout.playlist_item_grid_view, parent, false)
//            )
//        } else {
//            PlaylistViewHolder(
//                LayoutInflater.from(parent.context)
//                    .inflate(R.layout.playlist_item_list_view, parent, false)
//            )
//        }
//    }
//
//    override fun onBindViewHolder(
//        holder: PlaylistViewHolder,
//        position: Int
//    ) {
//        holder.onBind(
//            playlist = playlists[position],
//            listener = listener,
//            layoutType = layoutType
//        )
//    }
//
//    override fun getItemCount(): Int = playlists.size
//
//    interface OnPlaylistListener {
//        fun onPlaylistClick(playlist: Playlist, view: View)
//    }
//
//}