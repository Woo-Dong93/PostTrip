package com.posttrip.journeydex

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posttrip.journeydex.core.data.model.mission.Mission
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.core.data.repository.MissionRepository
import com.posttrip.journeydex.core.data.repository.TravelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    private val travelRepository: TravelRepository,
    private val missionRepository: MissionRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val contentId: String? = savedStateHandle.get<String>("contentId")

    private val _courseDetail = MutableStateFlow<Course?>(null)
    val courseDetail : StateFlow<Course?> = _courseDetail.asStateFlow()

    private val _missions = MutableStateFlow<List<Mission>>(emptyList())
    val missions : StateFlow<List<Mission>> = _missions

    fun getCourseDetail(){
        viewModelScope.launch {
                contentId?.let {
                    _courseDetail.emit(travelRepository.getCachedCourseDetail(it))
                }
        }
    }

    fun getMissionListByCourse(contentId : String){
        viewModelScope.launch {
            missionRepository.getMissionListByCourse(contentId)
                .catch {  }
                .collect{
                    _missions.emit(it.missions)
                }
        }
    }

}
