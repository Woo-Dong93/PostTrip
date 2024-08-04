package com.posttrip.journeydex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : ViewModel() {

    private val _typeFromLogin = MutableStateFlow<TypeFromLogin>(TypeFromLogin.None)
    val typeFromLogin : StateFlow<TypeFromLogin> = _typeFromLogin.asStateFlow()

    var step = MutableStateFlow(0)

    fun updateData(needsOnboarding : Boolean){
        viewModelScope.launch {
            _typeFromLogin.emit(
                if(needsOnboarding) TypeFromLogin.NeedsOnboarding else TypeFromLogin.GoToHome
            )
        }
    }
    fun updateStep(data : Int){
        viewModelScope.launch {
            step.emit(data)
        }
    }
    companion object {
        enum class TypeFromLogin {
            NeedsOnboarding, GoToHome, None
        }
    }


}