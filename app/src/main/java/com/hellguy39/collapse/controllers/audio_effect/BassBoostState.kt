package com.hellguy39.collapse.controllers.audio_effect

//class BassBoostState(
//    private val bassBoostUseCases: BassBoostUseCases
//) {
//
//    private val isEnabledLiveData = MutableLiveData<Boolean>()
//    private val bassStrengthLiveData = MutableLiveData<Short>()
//
//    fun getIsEnabled(): LiveData<Boolean> = isEnabledLiveData
//
//    fun getBassStrength(): LiveData<Short> = bassStrengthLiveData
//
//    fun updateIsEnabled(enabled: Boolean) {
//        isEnabledLiveData.value = enabled
//        saveIsEnabled()
//    }
//
//    fun updateBassStrength(strength: Short) {
//        bassStrengthLiveData.value = strength
//        saveBassStrength()
//    }
//
//    fun loadSavedState() {
//        isEnabledLiveData.value = bassBoostUseCases.getIsBassBoostEnabledUseCase.invoke()
//        bassStrengthLiveData.value = bassBoostUseCases.getBassBoostStrengthUseCase.invoke()
//    }
//
//    private fun saveIsEnabled() {
//        isEnabledLiveData.value?.let {
//            bassBoostUseCases.saveIsBassBoostEnabledUseCase.invoke(it)
//        }
//    }
//
//    private fun saveBassStrength() {
//        bassStrengthLiveData.value?.let {
//            bassBoostUseCases.saveBassBoostStrengthUseCase.invoke(it)
//        }
//    }
//}