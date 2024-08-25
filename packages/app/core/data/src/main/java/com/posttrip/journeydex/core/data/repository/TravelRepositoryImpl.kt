package com.posttrip.journeydex.core.data.repository

import com.posttrip.journeydex.core.data.api.TravelService
import com.posttrip.journeydex.core.data.model.response.CourseList
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.core.data.util.handleApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TravelRepositoryImpl @Inject constructor(
    private val travelService: TravelService
) : TravelRepository {
    override fun getCourse(id: String): Flow<CourseList> = handleApi {
        travelService.getCourse(id)
    }

    override fun getCourseDetail(contentId: String): Flow<CourseList> = handleApi {
        travelService.getCourseDetail(contentId)
    }
}