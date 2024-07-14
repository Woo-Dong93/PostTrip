package com.posttrip.journeydex

import android.app.Application
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JourneydexApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //kakao-map
        KakaoMapSdk.init(this, BuildConfig.KAKAO_APP_KEY)
    }
}