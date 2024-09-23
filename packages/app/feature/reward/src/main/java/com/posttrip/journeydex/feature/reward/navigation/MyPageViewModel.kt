package com.posttrip.journeydex.feature.reward.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posttrip.journeydex.core.data.model.mission.Mission
import com.posttrip.journeydex.core.data.model.travel.Character
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.core.data.repository.MissionRepository
import com.posttrip.journeydex.core.data.repository.TravelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val missionRepository: MissionRepository,
    private val travelRepository: TravelRepository
) : ViewModel() {

    private val _missions = MutableStateFlow<List<Mission>>(emptyList())
    val missions: StateFlow<List<Mission>> = _missions.asStateFlow()

    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters : StateFlow<List<Character>> = _characters.asStateFlow()
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses.asStateFlow()

    fun getLikedCourses() {
        if (courses.value.isEmpty())

            viewModelScope.launch {
                travelRepository.getLikedCourse()
                    .catch {
                    }.collect {
                        _courses.emit(it.courses)
                    }
            }
    }
    fun getMissions() {
        viewModelScope.launch {
            missionRepository.getUserMissionList()
                .catch { }
                .collect {
                    val missionList = it.missions
                    _missions.emit(
                        missionList
                    )
                }
        }
    }

    fun getCharacters() {
        viewModelScope.launch {
            travelRepository.getCharacters()
                .catch { }
                .collect {
                    _characters.emit(it.characters.distinctBy { it.title })
                }
        }
    }
}
