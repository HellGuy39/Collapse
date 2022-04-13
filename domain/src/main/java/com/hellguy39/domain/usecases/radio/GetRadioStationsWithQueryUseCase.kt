package com.hellguy39.domain.usecases.radio

import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.repositories.RadioStationsRepository

class GetRadioStationsWithQueryUseCase(
    private val radioStationsRepository: RadioStationsRepository
    ) {

    suspend operator fun invoke(query: String) : List<RadioStation> {
        return radioStationsRepository.getAllRadioStationsWithQuery(query = query)
    }
}