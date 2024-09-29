package com.posttrip.journeydex.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posttrip.journeydex.core.data.model.request.FavoriteCourse
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.core.data.repository.TravelRepository
import com.posttrip.journeydex.core.data.util.LoginCached
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val travelRepository: TravelRepository
) : ViewModel() {
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses.asStateFlow()

    fun getLikedCourses() {
        if (courses.value.isEmpty())
            viewModelScope.launch {
                travelRepository.getLikedCourse()
                    .catch {
                    }.collect {
                        _courses.emit(it.courses.map { it.copy(
                            favorite = true
                        ) })
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
                    courses.value.filter {
                       it.contentId != course.contentId
                    }
                )
            }
        }
    }
}
