package com.hellguy39.collapse.presentaton.fragments.settings

//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.hellguy39.domain.models.AppSettings
//import com.hellguy39.domain.usecases.app_settings.AppSettingsUseCases
//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject

//@HiltViewModel
//class SettingsFragmentViewModel @Inject constructor(
//    private val appSettingsUseCases: AppSettingsUseCases
//): ViewModel() {
//
//    private val settingsLiveData = MutableLiveData<AppSettings>(
//        appSettingsUseCases.getAppSettingsUseCase.invoke()
//    )
//
//    fun getSettings(): LiveData<AppSettings> = settingsLiveData
//
//    fun saveIsAnimationsEnabled(enabled: Boolean) {
//        settingsLiveData.value.apply {
//            this?.isAnimationsEnabled = enabled
//        }
//        appSettingsUseCases.saveIsAnimationsEnabledUseCase.invoke(enabled)
//    }
//
//    fun saveIsSaveStateEnabled(enabled: Boolean) {
//        settingsLiveData.value.apply {
//            this?.isSaveStateEnabled = enabled
//        }
//        appSettingsUseCases.saveIsSaveStateEnabledUseCase.invoke(enabled)
//    }
//
//    fun saveIsAdaptableBackground(enabled: Boolean) {
//        settingsLiveData.value.apply {
//            this?.isAdaptableBackgroundEnabled = enabled
//        }
//        appSettingsUseCases.saveIsAdaptableBackgroundEnabledUseCase.invoke(enabled)
//    }
//
//}