package com.posttrip.journeydex.core.data.model.travel

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val areaCode: String = "",
    val contentId: String = "",
    val destinationTypeKeyword: String = "",
    val firstAddress: String = "",
    val firstImage: String = "",
    val secondAddress: String = "",
    val secondImage: String = "",
    val title: String = "",
    val travelStyleKeyword: String = "",
    val travelTypeKeyword: String = "",
    val x: String = "",
    val y: String = "",
    val isDetail : Boolean = false,
    val favorite : Boolean = false,
    val overview : String = "",
    val characterInfo : Character = Character(),
    val enabledToCollect : Boolean = false,
    val courseList : List<Course> = emptyList()
)
