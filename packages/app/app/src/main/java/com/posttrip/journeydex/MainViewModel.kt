package com.posttrip.journeydex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : ViewModel() {

    var isLogged = MutableStateFlow(false)
    var step = MutableStateFlow(0)

    fun updateData(data : Boolean){
        viewModelScope.launch {
            isLogged.emit(data)
        }
    }

    fun updateStep(data : Int){
        viewModelScope.launch {
            step.emit(data)
        }
    }
}