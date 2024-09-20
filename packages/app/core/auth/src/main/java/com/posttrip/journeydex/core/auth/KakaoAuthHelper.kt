package com.posttrip.journeydex.core.auth

import android.content.Context
import android.util.Log
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.posttrip.journeydex.core.auth.model.LoginData
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class KakaoAuthHelper @Inject constructor(
    @ActivityContext private val context: Context
) : AuthHelper {
    override suspend fun authorize(): LoginData = suspendCoroutine { continuation ->
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    continuation.resumeWithException(error)
                } else if (token != null) {
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            continuation.resumeWithException(error)
                        } else if (user?.id != null) {
                            continuation.resume(LoginData(
                                nickname = user.kakaoAccount?.profile?.nickname  ?: "",
                                uId = user.id.toString(),
                                email = user.kakaoAccount?.email ?: ""
                            ))
                        } else {
                            continuation.resumeWithException(RuntimeException("Kakao User Error"))
                        }
                    }
                } else {
                    continuation.resumeWithException(RuntimeException("Kakao Login Error"))
                }
            }
        } else {
            try {
                UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                    if (error != null) {
                        continuation.resumeWithException(RuntimeException("Kakao Login Error"))
                    }
                    else if (token != null) {
                        UserApiClient.instance.me { user, error ->
                            if (error != null) {
                                continuation.resumeWithException(error)
                            } else if (user?.id != null) {
                                continuation.resume(LoginData(
                                    nickname = user.kakaoAccount?.profile?.nickname  ?: "",
                                    uId = user.id.toString(),
                                    email = user.kakaoAccount?.email ?: ""
                                ))
                            } else {
                                continuation.resumeWithException(RuntimeException("Kakao User Error"))
                            }
                        }
                    }
                }
            }catch (e: Exception){
                continuation.resumeWithException(RuntimeException("Kakao Login Error"))
            }
        }
    }

    override suspend fun getKakaoUser(): User = suspendCoroutine { continuation ->
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                continuation.resumeWithException(error)
            } else if (user?.id != null) {
                continuation.resume(user)
            } else {
                continuation.resumeWithException(RuntimeException("Kakao User Error"))
            }
        }
    }

    override suspend fun unlink() : Unit = suspendCoroutine { continuation ->
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                continuation.resumeWithException(error)
            }
            else {
                continuation.resume(Unit)
            }
        }
    }
}
