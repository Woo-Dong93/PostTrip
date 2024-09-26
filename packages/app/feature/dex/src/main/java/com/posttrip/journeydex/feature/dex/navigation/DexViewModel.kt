package com.posttrip.journeydex.feature.dex.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posttrip.journeydex.core.data.model.mission.Mission
import com.posttrip.journeydex.core.data.model.mission.MissionStatus
import com.posttrip.journeydex.core.data.model.request.MissionBody
import com.posttrip.journeydex.core.data.model.travel.Character
import com.posttrip.journeydex.core.data.repository.MissionRepository
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
class DexViewModel @Inject constructor(
    private val travelRepository: TravelRepository,
    private val missionRepository: MissionRepository
) :  ViewModel() {
    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters : StateFlow<List<Character>> = _characters.asStateFlow()
    private val _missions = MutableStateFlow<List<Mission>>(emptyList())
    val missions : StateFlow<List<Mission>> = _missions.asStateFlow()
    fun getCharacters() {
        viewModelScope.launch {
            travelRepository.getCharacters()
                .catch { }
                .collect {
                    _characters.emit(it.characters.distinctBy { it.title })
                }
        }
    }

    fun getMissions() {
        viewModelScope.launch {
            missionRepository.getUserMissionList()
                .catch {  }
                .collect {
                    val missionList = it.missions
                    _missions.emit(
                        missionList
                    )
                }
        }
    }

    fun clickMission(mission: Mission){
        if(mission.statusType == MissionStatus.ACTIVE && mission.collectedCount == mission.collectionCount){
            viewModelScope.launch {
                missionRepository.completeMission(
                    missionBody = MissionBody(
                        id = mission.id,
                        userId = LoginCached.kakaoId,
                        status = ""
                    )
                ).catch {

                }.collect {
                    getMissions()
                }
            }
        }else if(mission.statusType == MissionStatus.PENDING){
            viewModelScope.launch {
                missionRepository.startMission(
                    missionBody = MissionBody(
                        id = mission.id,
                        userId = LoginCached.kakaoId,
                        status = ""
                    )
                ).catch {

                }.collect {
                    getMissions()
                }
            }
        }
    }
}
