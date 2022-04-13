package com.hellguy39.domain.usecases.radio

data class RadioStationUseCases(
    val addRadioStationUseCase: AddRadioStationUseCase,
    val editRadioStationUseCase: EditRadioStationUseCase,
    val deleteRadioStationUseCase: DeleteRadioStationUseCase,
    val getAllRadioStationsUseCase: GetAllRadioStationsUseCase,
    val getRadioStationByIdUseCase: GetRadioStationByIdUseCase,
    val getRadioStationsWithQueryUseCase: GetRadioStationsWithQueryUseCase
)
