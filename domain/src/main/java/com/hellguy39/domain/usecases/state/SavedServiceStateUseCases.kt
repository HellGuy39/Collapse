package com.hellguy39.domain.usecases.state

data class SavedServiceStateUseCases(
    val getSavedServiceStateUseCase: GetSavedServiceStateUseCase,
    val insertSavedServiceStateUseCase: InsertSavedServiceStateUseCase
)
