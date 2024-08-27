package com.posttrip.journeydex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posttrip.journeydex.core.data.model.response.LoginData
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
    val typeFromLogin: StateFlow<TypeFromLogin> = _typeFromLogin.asStateFlow()

    private val _loginData = MutableStateFlow(
        LoginData(
            id = "",
            nickname = "",
            onboarding = true
        )
    )
    val loginData : StateFlow<LoginData> = _loginData.asStateFlow()

    fun updateData(loginData: LoginData) {
        viewModelScope.launch {
            _typeFromLogin.emit(
                if (loginData.onboarding) TypeFromLogin.NeedsOnboarding else TypeFromLogin.GoToHome
            )
            _loginData.emit(loginData)
        }
    }

    fun updateTypeFromLogin(typeFromLogin: TypeFromLogin) {
        viewModelScope.launch {
            _typeFromLogin.emit(typeFromLogin)
        }
    }

    companion object {
        enum class TypeFromLogin {
            NeedsOnboarding, GoToHome, None
        }
    }


}