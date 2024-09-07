package com.posttrip.journeydex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posttrip.journeydex.core.data.model.user.OnboardingData
import com.posttrip.journeydex.core.data.model.user.OnboardingKeywords
import com.posttrip.journeydex.core.data.repository.UserRepository
import com.posttrip.journeydex.core.data.util.LoginCached
import com.posttrip.journeydex.model.OnboardingStepModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _onboardingStep = MutableStateFlow(OnboardingStepModel())
    val onboardingStep : StateFlow<OnboardingStepModel> = _onboardingStep.asStateFlow()

    private val _event = MutableSharedFlow<Unit>()
    val event : SharedFlow<Unit> = _event

    fun updateStepIndex(targetIndex : Int){
        viewModelScope.launch {
            _onboardingStep.emit(
                onboardingStep.value.copy(
                    index = targetIndex
                )
            )
        }
    }

    fun updateStepStyle(style : String){
        viewModelScope.launch {
            _onboardingStep.emit(
                onboardingStep.value.copy(
                    style = style
                )
            )
        }
    }

    fun updateStepDestination(destination : String){
        viewModelScope.launch {
            _onboardingStep.emit(
                onboardingStep.value.copy(
                    destination = destination
                )
            )
        }
    }

    fun updateStepType(type : String){
        viewModelScope.launch {
            _onboardingStep.emit(
                onboardingStep.value.copy(
                    type = type
                )
            )
        }
    }

    fun setOnboarding(id : String){
        viewModelScope.launch {
            userRepository.setOnboarding(
                OnboardingData(
                    id = LoginCached.kakaoId,
                    keywords = OnboardingKeywords(
                        travelStyleKeyword = onboardingStep.value.style,
                        destinationTypeKeyword = onboardingStep.value.destination,
                        travelTypeKeyword = onboardingStep.value.type
                    )
                )
            ).catch {

            }.collect {
                _event.emit(Unit)
            }
        }
    }
}
