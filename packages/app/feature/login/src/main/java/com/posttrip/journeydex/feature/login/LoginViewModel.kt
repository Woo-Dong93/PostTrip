package com.posttrip.journeydex.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posttrip.journeydex.core.data.model.user.LoginBody
import com.posttrip.journeydex.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginEvent {
    data object LoginByKakao : LoginEvent()
    data class LoginSuccess(
        val needsOnboarding: Boolean,
        val nickname: String,
        val id: String,
    ) : LoginEvent()
}


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginEvent = MutableSharedFlow<LoginEvent>()
    val loginEvent: SharedFlow<LoginEvent> = _loginEvent.asSharedFlow()

    fun login(
        id: String,
        nickname: String,
        authProvider: AuthProvider
    ) {
        viewModelScope.launch {
            userRepository.login(
                body = LoginBody(
                    id = id,
                    nickname = nickname,
                    authProvider = authProvider.name
                )
            ).catch {

            }.collect {
                _loginEvent.emit(
                    LoginEvent.LoginSuccess(
                        needsOnboarding = it.onboarding.not(),
                        nickname = it.nickname,
                        id = it.id
                    )
                )
            }
        }
    }

    fun updatedEvent(event: LoginEvent) {
        viewModelScope.launch {
            _loginEvent.emit(event)
        }
    }

    companion object {
        enum class AuthProvider {
            kakao
        }
    }
}