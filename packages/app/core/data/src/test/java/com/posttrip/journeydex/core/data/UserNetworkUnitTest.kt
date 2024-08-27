package com.posttrip.journeydex.core.data


import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.posttrip.journeydex.core.data.api.UserService
import com.posttrip.journeydex.core.data.model.user.LoginBody
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class UserNetworkUnitTest {
    private val responseOkCode = 200
    private val baseUrl = BuildConfig.BASE_URL
    private val networkJson = Json { ignoreUnknownKeys = true }

    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit
    private lateinit var userService: UserService

    @Before
    fun initNetworkInstances() {
        okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .build()

        retrofit = Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType())
            )
            .client(okHttpClient)
            .build()

        userService = retrofit.create(UserService::class.java)
    }

    @Test
    fun test_LOGIN_IS_OK() = runBlocking {
        val response = userService.login(
            LoginBody(
                id = "testByAndroid",
                nickname = "test",
                authProvider = "kakao"
            )
        )
        Assert.assertEquals(responseOkCode, response.code())
        println(response.body())
    }
}