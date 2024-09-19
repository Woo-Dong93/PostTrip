package com.posttrip.journeydex.core.auth.di

import com.posttrip.journeydex.core.auth.AuthHelper
import com.posttrip.journeydex.core.auth.AuthOutHelper
import com.posttrip.journeydex.core.auth.KakaoAuthHelper
import com.posttrip.journeydex.core.auth.KakaoAuthOutHelper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun bindKakaoAuthHelper(
        kakaoAuthHelper: KakaoAuthHelper
    ): AuthHelper

    @Binds
    abstract fun bindKakaoAuthOutHelper(
        kakaoAuthHelper: KakaoAuthOutHelper
    ): AuthOutHelper
}
