package com.posttrip.journeydex.feature.map

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posttrip.journeydex.core.data.model.response.CourseList
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.core.data.repository.TravelRepository
import com.posttrip.journeydex.core.data.util.LoginCached
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val travelRepository: TravelRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val contentId: String? = savedStateHandle.get<String>("contentId")

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses : StateFlow<List<Course>> = _courses.asStateFlow()

    private val _detailCourses = MutableStateFlow<CourseList?>(null)
    val detailCourses : StateFlow<CourseList?> = _detailCourses.asStateFlow()

    var cachecd : List<Course> = emptyList()

    fun initCourseDetail(){
        if(contentId != null && contentId != "-1"){
            getCourseDetail(travelRepository.getCachedCourse(contentId))
        }
    }

    fun getCourse(id : String) {
        viewModelScope.launch {
            travelRepository.getCourse(LoginCached.kakaoId)
                .catch {

                }.collect {
                    _courses.emit(it.courses)
                    cachecd = it.courses
                }
        }
    }

    fun getCourseDetail(course : Course) {
        viewModelScope.launch {
            travelRepository.getCourseDetail(course.contentId)
                .catch {

                }.collect {
                    delay(1000)
                    _detailCourses.emit(it.copy(
                        courses = it.courses.map {
                            it.copy(isDetail = true)
                        },
                        course = course
                    ))
                }
        }
    }
}
