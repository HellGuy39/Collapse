package com.hellguy39.collapse.presentaton.view_models

//@HiltViewModel
//class MediaViewModel @Inject constructor(
//    private val getAllArtistsUseCase: GetAllArtistsUseCase,
//    private val getAllAlbumsUseCase: GetAllAlbumsUseCase
//) : ViewModel() {
//
////    private val _songs = MutableStateFlow<List<Song>>(listOf())
////    val songs = _songs.asStateFlow()
//
//    private val _artists = MutableStateFlow<List<com.hellguy39.collapse.core.model.Artist>>(listOf())
//    val artists = _artists.asStateFlow()
//
//    private val _albums = MutableStateFlow<List<com.hellguy39.collapse.core.model.Album>>(listOf())
//    val albums = _albums.asStateFlow()
//
//    init {
//        //fetchSongs()
//        fetchArtists()
//        fetchAlbums()
//    }
//
////    private fun fetchSongs() = viewModelScope.launch {
////        _songs.compareAndSet(
////            _songs.value,
////            getAllSongsUseCase.invoke()
////        )
////    }
//
//    private fun fetchArtists() = viewModelScope.launch {
//        _artists.compareAndSet(
//            _artists.value,
//            getAllArtistsUseCase.invoke()
//        )
//    }
//
//    private fun fetchAlbums() = viewModelScope.launch {
//        _albums.compareAndSet(
//            _albums.value,
//            getAllAlbumsUseCase.invoke()
//        )
//    }
//
//
//}