package com.posttrip.journeydex.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginData(
    val id : String,
    val nickname : String,
    val onboarding : Boolean
)
