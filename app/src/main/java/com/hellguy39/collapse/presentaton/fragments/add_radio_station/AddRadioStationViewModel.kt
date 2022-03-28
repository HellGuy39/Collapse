package com.hellguy39.collapse.presentaton.fragments.add_radio_station

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.usecases.radio.RadioStationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRadioStationViewModel @Inject constructor(
    private val radioStationUseCases: RadioStationUseCases
): ViewModel() {

    fun addNewRadioStation(radioStation: RadioStation) = viewModelScope.launch(Dispatchers.IO) {
        radioStationUseCases.addRadioStationUseCase.invoke(radioStation = radioStation)
    }

}