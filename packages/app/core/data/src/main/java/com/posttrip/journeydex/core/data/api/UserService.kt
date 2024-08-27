package com.posttrip.journeydex.core.data.api

import com.posttrip.journeydex.core.data.model.user.LoginBody
import com.posttrip.journeydex.core.data.model.response.LoginData
import com.posttrip.journeydex.core.data.model.user.OnboardingData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST(value = "/user/login")
    suspend fun login(
        @Body body : LoginBody
    ) : Response<LoginData>

    @POST(value = "/user/onboarding")
    suspend fun setOnboarding(
        @Body body : OnboardingData
    ) : Response<OnboardingData>
}