package com.hellguy39.domain.usecases.state

import com.hellguy39.domain.models.ServiceContentWrapper
import com.hellguy39.domain.repositories.SavedServiceStateRepository

class InsertSavedServiceStateUseCase(private val repository: SavedServiceStateRepository) {
    suspend operator fun invoke(serviceContentWrapper: ServiceContentWrapper) {
        repository.insertSavedState(serviceContent = serviceContentWrapper)
    }
}