package com.posttrip.journeydex.core.data.repository

import com.posttrip.journeydex.core.data.api.UserService
import com.posttrip.journeydex.core.data.model.user.LoginBody
import com.posttrip.journeydex.core.data.model.response.LoginData
import com.posttrip.journeydex.core.data.model.user.OnboardingData
import com.posttrip.journeydex.core.data.util.LoginCached.kakaoId
import com.posttrip.journeydex.core.data.util.handleApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService
) : UserRepository {
    override fun login(body: LoginBody): Flow<LoginData> = handleApi {
        kakaoId = body.id
        userService.login(body)
    }

    override fun setOnboarding(body: OnboardingData): Flow<OnboardingData> = handleApi {
        userService.setOnboarding(body)
    }

}
