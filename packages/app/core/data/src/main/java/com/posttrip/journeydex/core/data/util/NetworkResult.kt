package com.posttrip.journeydex.core.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response

fun <T : Any> handleApi(
    execute: suspend () -> Response<T>
): Flow<T> = flow {
    try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            emit(body)
        } else {
            throw RuntimeException("Data is Null")
        }
    } catch (e: HttpException) {
        throw e
    } catch (e: Throwable) {
        throw e
    }
}