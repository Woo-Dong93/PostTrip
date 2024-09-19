package com.posttrip.journeydex.feature.reward.navigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posttrip.journeydex.core.auth.KakaoAuthOutHelper
import com.posttrip.journeydex.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SettingEvent{
    Logout, Withdraw
}

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val kakaoAuthOutHelper : KakaoAuthOutHelper
) : ViewModel() {

    private val _event = MutableSharedFlow<SettingEvent>()
    val event : SharedFlow<SettingEvent> = _event.asSharedFlow()


    fun logout() {
        viewModelScope.launch {
            try {
                kakaoAuthOutHelper.logout()
                _event.emit(SettingEvent.Logout)
            }catch (e: Exception){

            }

        }
    }

    fun withdraw() {
        viewModelScope.launch {
            try {

                kakaoAuthOutHelper.unlink()
                userRepository.withdraw().collect {
                    _event.emit(SettingEvent.Withdraw)
                }



            }catch (e: Exception){

            }

        }
    }
}
