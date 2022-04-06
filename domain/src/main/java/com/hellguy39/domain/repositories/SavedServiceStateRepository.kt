package com.hellguy39.domain.repositories

import android.app.Service
import com.hellguy39.domain.models.ServiceContentWrapper

interface SavedServiceStateRepository {

    suspend fun getSavedState() : ServiceContentWrapper

    suspend fun insertSavedState(serviceContent: ServiceContentWrapper)

}