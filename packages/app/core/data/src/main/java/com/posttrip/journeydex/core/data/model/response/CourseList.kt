package com.posttrip.journeydex.core.data.model.response

import com.posttrip.journeydex.core.data.model.travel.Course
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseList(
    @SerialName("data") val courses : List<Course>
)