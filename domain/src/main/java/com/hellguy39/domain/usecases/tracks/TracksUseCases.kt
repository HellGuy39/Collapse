package com.hellguy39.domain.usecases.tracks

data class TracksUseCases(
    val getAllTracksUseCase: GetAllTracksUseCase,
    val getTracksByArtistUseCase: GetTracksByArtistUseCase,
    val getAllArtistsUseCase: GetAllArtistsUseCase,
    val getAllTrackByArtistUseCase: GetAllTrackByArtistUseCase
)
