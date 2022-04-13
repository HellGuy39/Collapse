package com.hellguy39.domain.usecases.radio

import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.repositories.RadioStationsRepository

class EditRadioStationUseCase(
    private val radioStationsRepository: RadioStationsRepository
) {
    suspend operator fun invoke(radioStation: RadioStation) {
        radioStationsRepository.updateRadioStation(radioStation = radioStation)
    }
}