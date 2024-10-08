package com.posttrip.journeydex.core.data.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginBody(
    val id : String,
    val nickname : String,
    @SerialName("auth_provider") val authProvider: String,
    val email : String
)
