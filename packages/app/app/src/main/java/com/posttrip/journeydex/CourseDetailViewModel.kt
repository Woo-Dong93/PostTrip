package com.posttrip.journeydex

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.core.data.repository.TravelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    private val travelRepository: TravelRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val contentId: String? = savedStateHandle.get<String>("contentId")

    private val _courseDetail = MutableStateFlow<Course?>(null)
    val courseDetail : StateFlow<Course?> = _courseDetail.asStateFlow()

    fun getCourseDetail(){
        viewModelScope.launch {
                contentId?.let {
                    _courseDetail.emit(travelRepository.getCachedCourseDetail(it))
                }
        }
    }

}
