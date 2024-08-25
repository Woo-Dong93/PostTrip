package com.posttrip.journeydex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posttrip.journeydex.model.OnboardingStepModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(

) : ViewModel() {

    private val _onboardingStep = MutableStateFlow(OnboardingStepModel())
    val onboardingStep : StateFlow<OnboardingStepModel> = _onboardingStep.asStateFlow()

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
}