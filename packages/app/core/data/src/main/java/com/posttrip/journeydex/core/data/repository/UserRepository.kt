package com.posttrip.journeydex.core.data.repository

import com.posttrip.journeydex.core.data.model.LoginBody
import com.posttrip.journeydex.core.data.model.LoginData
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun login(body: LoginBody) : Flow<LoginData>
}