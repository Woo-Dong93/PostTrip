package com.posttrip.journeydex.core.data.model.travel
import kotlinx.serialization.Serializable

@Serializable
data class Character(
    val id : String = "",
    val title : String ="",
    val collected : Boolean = false
)
