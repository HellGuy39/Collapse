package com.hellguy39.domain.usecases.radio

import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.repositories.RadioStationsRepository

class AddRadioStationUseCase(
    private val radioStationsRepository: RadioStationsRepository
) {
    suspend operator fun invoke(radioStation: RadioStation) {
        radioStationsRepository.insertRadioStation(radioStation = radioStation)
    }
}