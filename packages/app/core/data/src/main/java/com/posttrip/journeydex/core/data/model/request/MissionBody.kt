package com.posttrip.journeydex.core.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class MissionBody(
    val userId : String,
    val id : String, // missionId
    val status : String
)
