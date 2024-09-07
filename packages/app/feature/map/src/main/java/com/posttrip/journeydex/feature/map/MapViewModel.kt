package com.posttrip.journeydex.feature.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class MapViewModel @Inject constructor(
    private val travelRepository: TravelRepository
) : ViewModel() {

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses : StateFlow<List<Course>> = _courses.asStateFlow()

    var cachecd : List<Course> = emptyList()

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

    fun getCourseDetail(contentId : String) {
        viewModelScope.launch {
            travelRepository.getCourseDetail(contentId)
                .catch {

                }.collect {
                    _courses.emit(it.courses.map {
                        it.copy(isDetail = true)
                    })
                }
        }
    }
}
