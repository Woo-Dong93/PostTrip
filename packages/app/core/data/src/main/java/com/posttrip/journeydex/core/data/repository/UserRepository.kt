package com.posttrip.journeydex.core.data.repository

import com.posttrip.journeydex.core.data.model.request.Withdraw
import com.posttrip.journeydex.core.data.model.user.LoginBody
import com.posttrip.journeydex.core.data.model.response.LoginData
import com.posttrip.journeydex.core.data.model.user.OnboardingData
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun login(body: LoginBody) : Flow<LoginData>
    fun setOnboarding(body : OnboardingData) : Flow<OnboardingData>
    fun withdraw() : Flow<Withdraw>
}
