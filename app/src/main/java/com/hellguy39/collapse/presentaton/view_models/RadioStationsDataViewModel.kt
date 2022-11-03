package com.hellguy39.collapse.presentaton.view_models

//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.hellguy39.domain.models.Playlist
//import com.hellguy39.domain.models.RadioStation
//import com.hellguy39.domain.models.Track
//import com.hellguy39.domain.usecases.radio.RadioStationUseCases
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import javax.inject.Inject
//
//@HiltViewModel
//class RadioStationsDataViewModel @Inject constructor(
//    private val radioStationUseCases: RadioStationUseCases
//): ViewModel() {
//
//    init {
//        updateRadioStationList()
//    }
//
//    private val radioStationLiveData = MutableLiveData<List<RadioStation>>()
//
//    fun getRadioStationList(): LiveData<List<RadioStation>> {
//        return radioStationLiveData
//    }
//
//    fun updateRadioStationList() {
//        fetchRadioStationList()
//    }
//
//    fun addNewRadioStation(radioStation: RadioStation) = viewModelScope.launch(Dispatchers.IO) {
//        radioStationUseCases.addRadioStationUseCase.invoke(radioStation = radioStation)
//        updateRadioStationList()
//    }
//
//    fun deleteRadioStation(radioStation: RadioStation) = viewModelScope.launch(Dispatchers.IO) {
//        radioStationUseCases.deleteRadioStationUseCase.invoke(radioStation = radioStation)
//        updateRadioStationList()
//    }
//
//    fun updateRadioStation(radioStation: RadioStation) = viewModelScope.launch(Dispatchers.IO) {
//        radioStationUseCases.editRadioStationUseCase.invoke(radioStation = radioStation)
//        updateRadioStationList()
//    }
//
//    private fun fetchRadioStationList() = viewModelScope.launch(Dispatchers.IO) {
//        val stations = radioStationUseCases.getAllRadioStationsUseCase.invoke()
//        withContext(Dispatchers.Main) {
//            radioStationLiveData.value = stations
//        }
//    }
//
//    fun searchWithQueryInRadioStations(query: String = "", playlists: List<RadioStation>?): List<RadioStation> {
//        if (playlists.isNullOrEmpty())
//            return listOf()
//
//        val returnableList = mutableListOf<RadioStation>()
//
//        for (n in playlists.indices) {
//            if (playlists[n].name.contains(query, true)) {
//                returnableList.add(playlists[n])
//            }
//        }
//
//        return returnableList
//    }
//
//}