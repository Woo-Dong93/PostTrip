package com.posttrip.journeydex.core.data.model.response

import com.posttrip.journeydex.core.data.model.mission.Mission
import com.posttrip.journeydex.core.data.model.travel.Character
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterList(
    @SerialName("data") val characters : List<Character>,
)
