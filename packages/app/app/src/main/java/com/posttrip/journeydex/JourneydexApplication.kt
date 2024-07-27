package com.posttrip.journeydex

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JourneydexApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //kakao-login
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)

        //kakao-map
        KakaoMapSdk.init(this, BuildConfig.KAKAO_APP_KEY)
    }
}