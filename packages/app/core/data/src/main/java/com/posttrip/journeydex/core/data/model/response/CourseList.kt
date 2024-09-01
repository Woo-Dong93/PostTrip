package com.posttrip.journeydex.core.data.model.response

import com.posttrip.journeydex.core.data.model.travel.Course
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseList(
    @SerialName("data") val courses : List<Course>,
    val course : Course? = null
){
    fun getKeywordList() : List<String> {
        val styles = course?.travelStyleKeyword?.split(",") ?: emptyList()
        val types = course?.travelTypeKeyword?.split(",") ?: emptyList()
        val destinations = course?.destinationTypeKeyword?.split(",") ?: emptyList()
        return (styles + types + destinations).map { keywordMap.computeIfAbsent(it.trim()){it.trim()} }
    }
}

val keywordMap = mutableMapOf(
    "healing" to "힐링",
    "culture" to "문화",
    "gourmet" to "미식",
    "activity" to "액티비티",
    "beach" to "바닷가",
    "mountain" to "산",
    "city" to "도시",
    "island" to "섬",
    "solo" to "나홀로",
    "family" to "가족",
    "couple" to "연인",
    "friends" to "친구"

)