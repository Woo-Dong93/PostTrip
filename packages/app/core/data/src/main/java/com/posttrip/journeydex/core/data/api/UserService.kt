package com.posttrip.journeydex.core.data.api

import com.posttrip.journeydex.core.data.model.LoginBody
import com.posttrip.journeydex.core.data.model.LoginData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST(value = "/user/login")
    suspend fun login(
        @Body body : LoginBody
    ) : Response<LoginData>
}