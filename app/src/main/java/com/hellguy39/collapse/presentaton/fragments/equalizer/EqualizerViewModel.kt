package com.hellguy39.collapse.presentaton.fragments.equalizer

//import androidx.lifecycle.ViewModel
//import com.hellguy39.domain.models.EqualizerSettings
//import com.hellguy39.domain.usecases.eq_settings.EqualizerSettingsUseCases
//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject
//
//@HiltViewModel
//class EqualizerViewModel @Inject constructor(
//    private val equalizerSettingsUseCases: EqualizerSettingsUseCases
//) : ViewModel() {
//
//    fun saveEqualizerSettings(equalizerSettings: EqualizerSettings) {
//        equalizerSettingsUseCases.saveEqualizerSettingsUseCase.invoke(equalizerSettings = equalizerSettings)
//    }
//
//    fun getEqualizerSettings(): EqualizerSettings {
//        return equalizerSettingsUseCases.getEqualizerSettings.invoke()
//    }
//
//    fun savePreset(preset: Int) {
//        equalizerSettingsUseCases.saveEqPresetUseCase.invoke(preset = preset)
//    }
//
//    fun saveEqSwitch(isEnabled: Boolean) {
//        equalizerSettingsUseCases.saveEqSwitchUseCase.invoke(isEnabled = isEnabled)
//    }
//
//    fun saveBassBoostSwitch(isEnabled: Boolean) {
//        equalizerSettingsUseCases.saveBassBoostSwitchUseCase.invoke(isEnabled = isEnabled)
//    }
//
//    fun saveVirtualizerSwitch(isEnabled: Boolean) {
//        equalizerSettingsUseCases.saveVirtualizerSwitchUseCase.invoke(isEnabled = isEnabled)
//    }
//
//    fun saveBandsLevel(levels: List<Short>) {
//        equalizerSettingsUseCases.saveEqBandsLevelUseCase.invoke(levels = levels)
//    }
//
//    fun saveBassBoostValue(bass: Float) {
//        equalizerSettingsUseCases.saveBassBoostValueUseCase.invoke(bass = bass)
//    }
//
//    fun saveVirtualizerValue(virtualize: Float) {
//        equalizerSettingsUseCases.saveVirtualizerValueUseCase.invoke(virtualize = virtualize)
//    }
//
//}
