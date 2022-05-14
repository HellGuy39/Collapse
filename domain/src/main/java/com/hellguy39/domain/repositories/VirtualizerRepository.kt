package com.hellguy39.domain.repositories

interface VirtualizerRepository {

    fun getStrength(): Short

    fun saveStrength(strength: Short)

    fun getIsEnabled(): Boolean

    fun saveIsEnabled(enabled: Boolean)

}