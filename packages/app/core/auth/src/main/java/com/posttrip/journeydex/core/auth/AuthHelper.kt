package com.posttrip.journeydex.core.auth

import com.kakao.sdk.user.model.User
import com.posttrip.journeydex.core.auth.model.LoginData
import kotlinx.coroutines.flow.Flow

interface AuthHelper {

    suspend fun authorize() : LoginData

    suspend fun getKakaoUser() : User

    suspend fun unlink()
}