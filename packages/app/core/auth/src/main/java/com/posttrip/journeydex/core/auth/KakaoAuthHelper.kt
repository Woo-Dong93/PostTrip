package com.posttrip.journeydex.core.auth

import android.content.Context
import android.util.Log
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class KakaoAuthHelper @Inject constructor(
    @ActivityContext private val context : Context
) : AuthHelper {
    override fun authorize(): Flow<Boolean> = callbackFlow {
        if(UserApiClient.instance.isKakaoTalkLoginAvailable(context)){
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e("TAG", "카카오톡으로 로그인 실패", error)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                } else if (token != null) {
                    Log.i("TAG", "카카오톡으로 로그인 성공 ${token.accessToken}")
                }
            }
        }else {
            Log.e("TAG", "카카오톡으로 로그인 아예 안됨", )
        }
        awaitClose {
            cancel()
        }
    }

}