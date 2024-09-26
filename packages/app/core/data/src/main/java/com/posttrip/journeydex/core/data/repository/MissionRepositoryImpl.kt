package com.posttrip.journeydex.core.data.repository

import com.posttrip.journeydex.core.data.api.MissionService
import com.posttrip.journeydex.core.data.model.request.MissionBody
import com.posttrip.journeydex.core.data.model.response.CouponList
import com.posttrip.journeydex.core.data.model.response.MissionList
import com.posttrip.journeydex.core.data.util.LoginCached
import com.posttrip.journeydex.core.data.util.handleApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MissionRepositoryImpl @Inject constructor(
    private val missionService: MissionService
) : MissionRepository {
    override fun getUserMissionList(): Flow<MissionList> = handleApi {
        missionService.getUserMissionList(LoginCached.kakaoId)
    }

    override fun getMissionListByCourse(contentId: String): Flow<MissionList> = handleApi {
        missionService.getMissionListByCourse(LoginCached.kakaoId,contentId)
    }

    override fun startMission(missionBody: MissionBody): Flow<MissionBody> = handleApi {
        missionService.startMission(missionBody)
    }

    override fun completeMission(missionBody: MissionBody): Flow<MissionBody> = handleApi {
        missionService.completeMission(missionBody)
    }

    override fun getCouponList(): Flow<CouponList> = handleApi {
        missionService.getCouponList(LoginCached.kakaoId)
    }

}
