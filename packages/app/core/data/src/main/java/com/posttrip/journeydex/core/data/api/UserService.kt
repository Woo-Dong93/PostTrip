package com.posttrip.journeydex.core.data.api

import com.posttrip.journeydex.core.data.model.request.Withdraw
import com.posttrip.journeydex.core.data.model.user.LoginBody
import com.posttrip.journeydex.core.data.model.response.LoginData
import com.posttrip.journeydex.core.data.model.user.OnboardingData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HTTP
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


    @HTTP(method = "DELETE", path = "/user/withdrawal", hasBody = true)
    suspend fun withdraw(
        @Body body : Withdraw
    ) : Response<Withdraw>
}
