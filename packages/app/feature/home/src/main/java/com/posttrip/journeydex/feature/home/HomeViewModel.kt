package com.posttrip.journeydex.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posttrip.journeydex.core.data.model.request.FavoriteCourse
import com.posttrip.journeydex.core.data.model.response.CourseList
import com.posttrip.journeydex.core.data.model.travel.Course
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
    private val travelRepository: TravelRepository
) : ViewModel() {
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses.asStateFlow()

    private val _courseDetail = MutableSharedFlow<CourseList>()
    val courseDetail: SharedFlow<CourseList> = _courseDetail.asSharedFlow()

    fun getRecommendedCourse(id: String) {
        if (courses.value.isEmpty())
            viewModelScope.launch {
                travelRepository.getRecommendedCourse(LoginCached.kakaoId)
                    .catch {

                    }.collect {
                        _courses.emit(it.courses.subList(0, 6))
                    }
            }
    }

    fun getCourseDetail(course: Course) {
        viewModelScope.launch {

            travelRepository.getCourseDetail(course.contentId)
                .catch {

                }.collect {
                    travelRepository.cacheCourse(
                        course.contentId,
                        it.copy(
                            course = course,
                            courses = it.courses.map {
                                it.copy(isDetail = true)
                            }
                        )
                    )
                    _courseDetail.emit(it.copy(course = course))
                }
        }
    }

    fun favoriteCourse(id: String, course: Course) {
        if (course.favorite) {
            unlikeCourse(LoginCached.kakaoId, course)
        } else {
            likeCourse(LoginCached.kakaoId, course)
        }
    }

    fun likeCourse(id: String, course: Course) {
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
                        if (it.contentId == course.contentId) it.copy(favorite = true)
                        else it
                    }
                )
            }
        }
    }

    fun unlikeCourse(id: String, course: Course) {
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
                        if (it.contentId == course.contentId) it.copy(favorite = false)
                        else it
                    }
                )
            }
        }
    }
}
