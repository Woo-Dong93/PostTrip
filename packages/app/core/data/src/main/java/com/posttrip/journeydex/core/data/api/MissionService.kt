package com.posttrip.journeydex.core.data.api

import com.posttrip.journeydex.core.data.model.request.MissionBody
import com.posttrip.journeydex.core.data.model.response.MissionList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path

interface MissionService {
    @GET("/mission/user/{id}")
    suspend fun getUserMissionList(
        @Path("id") id : String
    ) : Response<MissionList>

    @GET("/mission/user/{id}/{contentId}")
    suspend fun getMissionListByCourse(
        @Path("id") id : String,
        @Path("contentId") contentId : String
    ) : Response<MissionList>

    @POST("/mission/user")
    suspend fun startMission(
        @Body body : MissionBody
    ) : Response<MissionBody>

    @HTTP(method = "DELETE", path = "/mission/user", hasBody = true)
    suspend fun completeMission(
        @Body body : MissionBody
    ) : Response<MissionBody>
}

