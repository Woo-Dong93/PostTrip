package com.posttrip.journeydex.core.data.repository

import com.posttrip.journeydex.core.data.model.response.CourseList
import com.posttrip.journeydex.core.data.model.travel.Course
import kotlinx.coroutines.flow.Flow

interface TravelRepository {
    fun getCourse(id : String) : Flow<CourseList>

    fun getCourseDetail(contentId : String) : Flow<CourseList>

}