package com.hellguy39.domain.usecases.favourites

data class FavouriteTracksUseCases(
    val addFavouriteTrackUseCase: AddFavouriteTrackUseCase,
    val deleteFavouriteTrackUseCase: DeleteFavouriteTrackUseCase,
    val getAllFavouriteTracksUseCase: GetAllFavouriteTracksUseCase,
    val deleteFromFavouritesWithoutIdUseCase: DeleteFromFavouritesWithoutIdUseCase,
    val isTrackFavouriteUseCase: IsTrackFavouriteUseCase
)
