package com.posttrip.journeydex.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posttrip.journeydex.core.data.model.mission.Mission
import com.posttrip.journeydex.core.data.model.request.FavoriteCourse
import com.posttrip.journeydex.core.data.model.response.CourseList
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.core.data.repository.MissionRepository
import com.posttrip.journeydex.core.data.repository.TravelRepository
import com.posttrip.journeydex.core.data.util.LoginCached
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val travelRepository: TravelRepository,
    private val missionRepository: MissionRepository
) : ViewModel() {

    private val _missions = MutableStateFlow<List<Mission>>(emptyList())
    val missions : StateFlow<List<Mission>> = _missions.asStateFlow()

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses.asStateFlow()

    private val _courseDetail = MutableSharedFlow<CourseList>()
    val courseDetail : SharedFlow<CourseList> = _courseDetail.asSharedFlow()

    private var _shownLoading = MutableSharedFlow<Boolean>()
    val shownLoading : SharedFlow<Boolean> = _shownLoading.asSharedFlow()

    private val _showErrorToast = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val showErrorToast: SharedFlow<String> = _showErrorToast.asSharedFlow()

    fun getMissions() {
        viewModelScope.launch {
            missionRepository.getUserMissionList()
                .catch {  }
                .collect {
                    val missionList = it.missions
                    _missions.emit(
                        if(missionList.size >= 3) missionList.subList(0,3) else missionList
                    )
                }
        }
    }

    fun getRecommendedCourse(id: String) {
        if (courses.value.isEmpty())

            viewModelScope.launch {
                _shownLoading.emit(true)
                travelRepository.getRecommendedCourse(LoginCached.kakaoId)
                    .catch {
                        _shownLoading.emit(false)
                    }.collect {
                        _shownLoading.emit(false)
                        _courses.emit(it.courses.subList(0, 10))
                    }
            }
    }

    fun getCourseDetail(course: Course) {
        viewModelScope.launch {
            _shownLoading.emit(true)
            travelRepository.getCourseDetail(course.contentId)
                .catch {
                    _shownLoading.emit(false)
                }.collect {
                    _shownLoading.emit(false)
                    travelRepository.cacheCourse(
                        course.contentId,
                        CourseList(
                            course = it.data,
                            courses = it.data.courseList.map {
                                it.copy(isDetail = true)
                            }
                        )

                    )
                    _courseDetail.emit(CourseList(
                        course = it.data,
                        courses = it.data.courseList.map {
                            it.copy(isDetail = true)
                        }
                    ))
                }
        }
    }

    fun favoriteCourse(id: String,course: Course) {
        if (course.favorite) {
            unlikeCourse(LoginCached.kakaoId, course)
        } else {
            likeCourse(LoginCached.kakaoId, course)
        }
    }

    fun likeCourse(id : String,course: Course) {
        viewModelScope.launch {
            travelRepository.likeCourse(
                FavoriteCourse(
                    id = LoginCached.kakaoId,
                    contentId = course.contentId
                )
            ).catch {

            }.collect {
                _courses.emit(
                    courses.value.map {
                        if(it.contentId == course.contentId) it.copy(favorite = true)
                        else it
                    }
                )
            }
        }
    }

    fun unlikeCourse(id : String, course: Course) {
        viewModelScope.launch {
            travelRepository.unlikeCourse(
                FavoriteCourse(
                    id = LoginCached.kakaoId,
                    contentId = course.contentId
                )
            ).catch {

            }.collect {
                _courses.emit(
                    courses.value.map {
                        if(it.contentId == course.contentId) it.copy(favorite = false)
                        else it
                    }
                )
            }
        }
    }

    fun cacheDetail(course: Course){
        viewModelScope.launch {
            travelRepository.cacheCourseDetail(course.contentId,course)
        }
    }
}
