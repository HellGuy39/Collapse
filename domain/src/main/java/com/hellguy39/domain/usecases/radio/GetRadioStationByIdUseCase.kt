package com.hellguy39.domain.usecases.radio

import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.repositories.RadioStationsRepository

class GetRadioStationByIdUseCase(
    private val radioStationsRepository: RadioStationsRepository
    ) {
    suspend operator fun invoke(id: Int) : RadioStation{
        return radioStationsRepository.getRadioStationById(id = id)
    }
}