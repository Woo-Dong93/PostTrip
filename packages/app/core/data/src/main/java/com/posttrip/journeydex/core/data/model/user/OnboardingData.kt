package com.posttrip.journeydex.core.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class OnboardingData(
    val id : String,
    val keywords : OnboardingKeywords
)

@Serializable
data class OnboardingKeywords(
    val travelStyleKeyword : String,
    val destinationTypeKeyword : String,
    val travelTypeKeyword : String
)