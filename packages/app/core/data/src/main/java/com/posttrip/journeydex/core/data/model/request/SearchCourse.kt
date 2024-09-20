package com.posttrip.journeydex.core.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class SearchCourse(
    val area : String,
    val travelStyleKeyword : String,
    val destinationTypeKeyword : String,
    val travelTypeKeyword : String
)
