package com.posttrip.journeydex.core.data.repository

import com.posttrip.journeydex.core.data.model.mission.Coupon
import com.posttrip.journeydex.core.data.model.request.MissionBody
import com.posttrip.journeydex.core.data.model.response.CouponList
import com.posttrip.journeydex.core.data.model.response.MissionList
import kotlinx.coroutines.flow.Flow

interface MissionRepository {

    fun getUserMissionList() : Flow<MissionList>

    fun getMissionListByCourse(contentId : String) : Flow<MissionList>

    fun startMission(missionBody : MissionBody) : Flow<MissionBody>

    fun completeMission(missionBody : MissionBody) : Flow<MissionBody>

    fun getCouponList() : Flow<CouponList>
}
