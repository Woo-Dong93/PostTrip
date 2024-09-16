package com.posttrip.journeydex.core.data.repository

import com.posttrip.journeydex.core.data.api.TravelService
import com.posttrip.journeydex.core.data.model.request.FavoriteCourse
import com.posttrip.journeydex.core.data.model.response.CourseList
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.core.data.util.LoginCached
import com.posttrip.journeydex.core.data.util.handleApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TravelRepositoryImpl @Inject constructor(
    private val travelService: TravelService
) : TravelRepository {
    private val cachedCourse = hashMapOf<String, CourseList>()

    override fun cacheCourse(contentId: String, course: CourseList) {
        if(!cachedCourse.contains(contentId)){
            cachedCourse[contentId] = course
        }
    }

    override fun getCachedCourse(contentId: String): CourseList {
        return cachedCourse.get(contentId) ?: CourseList(emptyList())
    }

    override fun getCourse(id: String): Flow<CourseList> = handleApi {
        travelService.getCourse(id)
    }

    override fun getCourseDetail(contentId: String): Flow<CourseList> = handleApi {
        travelService.getCourseDetail(LoginCached.kakaoId,contentId)
    }

    override fun getRecommendedCourse(id: String): Flow<CourseList> = handleApi {
        travelService.getRecommendedCourse(id)
    }

    override fun likeCourse(favoriteCourse: FavoriteCourse): Flow<FavoriteCourse> = handleApi {
        travelService.likeCourse(favoriteCourse)
    }

    override fun unlikeCourse(favoriteCourse: FavoriteCourse): Flow<FavoriteCourse> = handleApi {
        travelService.unlikeCourse(favoriteCourse)
    }
}
