package com.posttrip.journeydex.core.data.model.response

import com.posttrip.journeydex.core.data.model.mission.Mission
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MissionList(
    @SerialName("data") val missions : List<Mission>,
)
