package com.posttrip.journeydex.core.auth

interface AuthOutHelper {

    suspend fun logout()

    suspend fun unlink()
}
