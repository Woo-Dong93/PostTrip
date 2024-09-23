package com.posttrip.journeydex.core.data.repository

import com.posttrip.journeydex.core.data.api.CharacterService
import com.posttrip.journeydex.core.data.api.TravelService
import com.posttrip.journeydex.core.data.model.request.CollectCharacter
import com.posttrip.journeydex.core.data.model.request.FavoriteCourse
import com.posttrip.journeydex.core.data.model.request.SearchCourse
import com.posttrip.journeydex.core.data.model.response.CharacterList
import com.posttrip.journeydex.core.data.model.response.CourseList
import com.posttrip.journeydex.core.data.model.travel.Character
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.core.data.util.LoginCached
import com.posttrip.journeydex.core.data.util.handleApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TravelRepositoryImpl @Inject constructor(
    private val travelService: TravelService,
    private val characterService: CharacterService
) : TravelRepository {
    private val cachedCourse = hashMapOf<String, CourseList>()
    private val cachedCourseDetail = hashMapOf<String, Course>()

    override fun cacheCourse(contentId: String, course: CourseList) {
        if(!cachedCourse.contains(contentId)){
            cachedCourse[contentId] = course
        }
    }

    override fun getCachedCourse(contentId: String): CourseList {
        return cachedCourse.get(contentId) ?: CourseList(emptyList())
    }

    override fun cacheCourseDetail(contentId: String, course: Course) {
        if(!cachedCourseDetail.contains(contentId)){
            cachedCourseDetail[contentId] = course
        }
    }

    override fun getCachedCourseDetail(contentId: String): Course? {
        return cachedCourseDetail.get(contentId)
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

    override fun getLikedCourse(): Flow<CourseList> =  handleApi {
        travelService.getLikedCourses(LoginCached.kakaoId)
    }


    override fun collectCharacter(characterId: String): Flow<CollectCharacter>  = handleApi {
        characterService.collectCharacter(
            CollectCharacter(
                userId = LoginCached.kakaoId,
                id = characterId
            )
        )
    }

    override fun getCharacters(): Flow<CharacterList> = handleApi {
        characterService.getCharacters(
            LoginCached.kakaoId
        )
    }


    override fun searchCourse(searchCourse: SearchCourse): Flow<CourseList> = handleApi {
        travelService.searchCourse(
            LoginCached.kakaoId,
            searchCourse
        )
    }
}
