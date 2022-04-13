package com.hellguy39.domain.usecases.radio

import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.repositories.RadioStationsRepository

class GetAllRadioStationsUseCase (
    private val radioStationsRepository: RadioStationsRepository
    ) {

    suspend operator fun invoke(): List<RadioStation> {
        return radioStationsRepository.getAllRadioStations()
    }
}