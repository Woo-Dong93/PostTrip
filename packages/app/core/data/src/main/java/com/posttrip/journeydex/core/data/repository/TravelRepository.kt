package com.posttrip.journeydex.core.data.repository

import com.posttrip.journeydex.core.data.model.request.CollectCharacter
import com.posttrip.journeydex.core.data.model.request.FavoriteCourse
import com.posttrip.journeydex.core.data.model.request.SearchCourse
import com.posttrip.journeydex.core.data.model.response.CharacterList
import com.posttrip.journeydex.core.data.model.response.CourseList
import com.posttrip.journeydex.core.data.model.travel.Character
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.core.data.model.travel.CourseDetail
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TravelRepository {
    fun cacheCourse(contentId : String, course:  CourseList)

    fun getCachedCourse(contentId : String) : CourseList?

    fun cacheCourseDetail(contentId : String, course: Course)

    fun getCachedCourseDetail(contentId : String) : Course?

    fun getCourse(id : String) : Flow<CourseList>

    fun getCourseDetail(contentId : String) : Flow<CourseDetail>

    fun getRecommendedCourse(id : String) : Flow<CourseList>

    fun likeCourse(favoriteCourse : FavoriteCourse) : Flow<FavoriteCourse>

    fun unlikeCourse(favoriteCourse : FavoriteCourse) : Flow<FavoriteCourse>

    fun getLikedCourse() : Flow<CourseList>

    fun collectCharacter(characterId : String) : Flow<CollectCharacter>

    fun getCharacters() : Flow<CharacterList>

    fun searchCourse(searchCourse : SearchCourse) : Flow<CourseList>
}
