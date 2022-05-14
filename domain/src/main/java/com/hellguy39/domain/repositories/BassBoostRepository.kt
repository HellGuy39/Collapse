package com.hellguy39.domain.repositories

interface BassBoostRepository {

    fun getStrength(): Short

    fun saveStrength(strength: Short)

    fun getIsEnabled(): Boolean

    fun saveIsEnabled(enabled: Boolean)

}