package com.posttrip.journeydex.core.data.api

import com.posttrip.journeydex.core.data.model.response.CourseList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TravelService {

    @GET("/travel/course/{id}")
    suspend fun getCourse(
        @Path("id") id : String
    ) : Response<CourseList>

    @GET("/travel/detail/{contentId}")
    suspend fun getCourseDetail(
        @Path("contentId") contentId : String
    ) : Response<CourseList>
}