package com.posttrip.journeydex.feature.map

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posttrip.journeydex.core.data.model.response.CourseList
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.core.data.repository.TravelRepository
import com.posttrip.journeydex.core.data.util.LoginCached
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
class MapViewModel @Inject constructor(
    private val travelRepository: TravelRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val contentId: String? = savedStateHandle.get<String>("contentId")

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses : StateFlow<List<Course>> = _courses.asStateFlow()



    private val _lineDetailCourses = MutableStateFlow<CourseList?>(null)
    val lineDetailCourses : StateFlow<CourseList?> = _lineDetailCourses.asStateFlow()

    private val _normalDetailCourses = MutableStateFlow<CourseList?>(null)
    val normalDetailCourses : StateFlow<CourseList?> = _normalDetailCourses.asStateFlow()

    private val _collectingDetailCourses = MutableStateFlow<CourseList?>(null)
    val collectingDetailCourses : StateFlow<CourseList?> = _collectingDetailCourses.asStateFlow()

    private val _finishDetailCourses = MutableStateFlow<CourseList?>(null)
    val finishDetailCourses : StateFlow<CourseList?> = _finishDetailCourses.asStateFlow()

    private var _shownLoading = MutableSharedFlow<Boolean>()
    val shownLoading : SharedFlow<Boolean> = _shownLoading.asSharedFlow()

    var cachecd : List<Course> = emptyList()

    fun initCourseDetail(){
        if(contentId != null && contentId != "-1"){
            viewModelScope.launch {
                delay(300)
                _shownLoading.emit(true)
                delay(2700)
                val courses = travelRepository.getCachedCourse(contentId)
                emitDetails(courses)
            }
        }
    }

    fun getCourse(id : String) {
        viewModelScope.launch {
            travelRepository.getCourse(LoginCached.kakaoId)
                .catch {

                }.collect { course ->
                    _courses.emit(course.courses)
//                    course.courses.forEach {
//                        travelRepository.cacheCourse(
//                            contentId = it.contentId,
//                            course = course
//                        )
//                    }
                    cachecd = course.courses
                }
        }
    }

    fun getCourseDetail(course : Course) {
        viewModelScope.launch {
            _shownLoading.emit(true)
            travelRepository.getCourseDetail(course.contentId)
                .catch {

                }.collect {
                    delay(1000)
                    val courseList = it.copy(
                        courses = it.courses.map {
                            it.copy(isDetail = true)
                        },
                        course = course
                    )
                    emitDetails(courseList)
                }
        }
    }

    fun emitDetails(courseList : CourseList) {
        viewModelScope.launch {
            val collecting = listOf(courseList.courses.first())
            val normal = courseList.courses.subList(1, courseList.courses.size)
            _normalDetailCourses.emit(
                courseList.copy(
                    courses = normal
                )
            )
            _collectingDetailCourses.emit(
                courseList.copy(
                    courses = collecting
                )
            )
//            _finishDetailCourses.emit()


            _lineDetailCourses.emit(courseList)
        }
    }

    fun refresh(courseList : CourseList){
        viewModelScope.launch {
            _normalDetailCourses.emit(
                courseList
            )
            _collectingDetailCourses.emit(
                CourseList(
                    emptyList()
                )
            )
        }
    }

    fun updateLoading(boolean: Boolean){
        viewModelScope.launch {
            _shownLoading.emit(boolean)
        }
    }
}
