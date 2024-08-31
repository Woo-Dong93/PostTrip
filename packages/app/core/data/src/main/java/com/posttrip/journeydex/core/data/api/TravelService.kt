package com.posttrip.journeydex.core.data.api

import com.posttrip.journeydex.core.data.model.request.FavoriteCourse
import com.posttrip.journeydex.core.data.model.response.CourseList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
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

    @GET("/travel/course/recommended/{id}")
    suspend fun getRecommendedCourse(
        @Path("id") id : String
    ) : Response<CourseList>

    @POST("/favorite")
    suspend fun likeCourse(
        @Body body : FavoriteCourse
    ) : Response<FavoriteCourse>

    @HTTP(method = "DELETE", path = "/favorite", hasBody = true)
    suspend fun unlikeCourse(
        @Body body : FavoriteCourse
    ) : Response<FavoriteCourse>

}

