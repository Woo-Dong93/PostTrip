package com.posttrip.journeydex.feature.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class LoginEvent {
    Kakao_Login
}

@HiltViewModel
class LoginViewModel @Inject constructor(

) : ViewModel() {

    private val _loginEvent = MutableSharedFlow<LoginEvent>()
    val loginEvent : SharedFlow<LoginEvent> = _loginEvent.asSharedFlow()

    fun updatedEvent(event : LoginEvent){
        viewModelScope.launch {
            _loginEvent.emit(event)
        }
    }
}