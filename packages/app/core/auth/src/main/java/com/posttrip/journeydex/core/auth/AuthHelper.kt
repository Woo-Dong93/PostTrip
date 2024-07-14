package com.posttrip.journeydex.core.auth

import kotlinx.coroutines.flow.Flow

interface AuthHelper {

    fun authorize() : Flow<Boolean>
}