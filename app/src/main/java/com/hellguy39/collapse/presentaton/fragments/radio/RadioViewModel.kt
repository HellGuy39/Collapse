package com.hellguy39.collapse.presentaton.fragments.radio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.usecases.radio.RadioStationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RadioViewModel @Inject constructor(
    private val radioStationUseCases: RadioStationUseCases
) : ViewModel() {

    private val radioStationLiveData = MutableLiveData<List<RadioStation>>()

    fun getRadioStationList(): LiveData<List<RadioStation>> {
        return radioStationLiveData
    }

    fun updateRadioStationList(query: String? = null) {
        if (query.isNullOrEmpty()) {
            fetchRadioStationList()
        } else {
            fetchRadioStationWithQueryList(query = query)
        }
    }

    private fun fetchRadioStationList() = viewModelScope.launch(Dispatchers.IO) {
        val stations = radioStationUseCases.getAllRadioStationsUseCase.invoke()
        withContext(Dispatchers.Main) {
            radioStationLiveData.value = stations
        }
    }

    private fun fetchRadioStationWithQueryList(query: String) = viewModelScope.launch(Dispatchers.IO) {
        val stations = radioStationUseCases.getRadioStationsWithQueryUseCase.invoke(query = query)
        withContext(Dispatchers.Main) {
            radioStationLiveData.value = stations
        }
    }
}