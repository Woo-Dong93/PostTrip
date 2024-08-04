package com.posttrip.journeydex.core.data.repository

import com.posttrip.journeydex.core.data.api.UserService
import com.posttrip.journeydex.core.data.model.LoginBody
import com.posttrip.journeydex.core.data.model.LoginData
import com.posttrip.journeydex.core.data.util.handleApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService
) : UserRepository {
    override fun login(body: LoginBody): Flow<LoginData> = handleApi {
        userService.login(body)
    }

}