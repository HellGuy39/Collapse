package com.hellguy39.domain.repositories

import android.app.Service
import com.hellguy39.domain.models.SavedState
import com.hellguy39.domain.models.ServiceContentWrapper

interface SavedServiceStateRepository {

    suspend fun getSavedState() : SavedState

    suspend fun insertSavedState(savedState: SavedState)

}