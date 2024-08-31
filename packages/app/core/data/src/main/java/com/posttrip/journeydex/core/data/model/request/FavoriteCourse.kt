package com.posttrip.journeydex.core.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteCourse(
    val id : String,
    val contentId : String
)
