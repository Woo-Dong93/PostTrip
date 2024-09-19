package com.posttrip.journeydex.core.auth

import com.kakao.sdk.user.UserApiClient
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class KakaoAuthOutHelper @Inject constructor(
) : AuthOutHelper {
    override suspend fun logout() : Unit = suspendCoroutine { continuation ->
        UserApiClient.instance.logout { error ->
            if (error != null) {
                continuation.resumeWithException(error)
            }
            else {
                continuation.resume(Unit)
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
