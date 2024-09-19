package com.posttrip.journeydex.core.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class CollectCharacter(
    val userId : String,
    val id : String
)
