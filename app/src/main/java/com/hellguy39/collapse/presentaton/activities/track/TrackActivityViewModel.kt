package com.hellguy39.collapse.presentaton.activities.track

//import androidx.lifecycle.ViewModel
//import com.hellguy39.domain.usecases.favourites.FavouriteTracksUseCases
//import com.hellguy39.domain.usecases.playlist.PlaylistUseCases
//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject

//@HiltViewModel
//class TrackActivityViewModel @Inject constructor(
//    private val favouriteTracksUseCases: FavouriteTracksUseCases,
//    private val playlistUseCases: PlaylistUseCases
//): ViewModel() {

//    fun deleteFromFavourites(track: Track) = viewModelScope.launch(Dispatchers.IO) {
//        val list = favouriteTracksUseCases.getAllFavouriteTracksUseCase.invoke().toMutableList()
//
//        for (n in list.indices) {
//            if (isFavourite(list[n], track)) {
//                list.remove(list[n])
//                favouriteTracksUseCases.deleteFromFavouritesWithoutIdUseCase.invoke(track)
//            }
//        }
//    }
//
//    fun addToFavourites(track: Track) = viewModelScope.launch(Dispatchers.IO){
//        favouriteTracksUseCases.addFavouriteTrackUseCase.invoke(track = track)
//    }
//
//    fun deleteFromPlaylist(track: Track, pos: Int) = viewModelScope.launch(Dispatchers.IO){
//        val type = PlayerService.getServiceContent().type
//        val playlist = PlayerService.getServiceContent().playlist
//
//        when (type) {
//            PlaylistType.Favourites -> {
//                favouriteTracksUseCases.deleteFavouriteTrackUseCase.invoke(track)
//            }
//            else -> {
//
//                if (playlist != null) {
//
//                    if (playlist.tracks.size == 1) {
//                        playlist.tracks = mutableListOf()
//                    } else {
//                        playlist.tracks.removeAt(pos)
//                    }
//
//                    playlistUseCases.updatePlaylistUseCase.invoke(playlist)
//                }
//            }
//        }
//    }
//
//    private fun isFavourite(track1: Track, track2: Track): Boolean {
//        return (track1.path == track2.path &&
//                track1.artist == track2.artist &&
//                track1.name == track2.name)
//
//    }

//}