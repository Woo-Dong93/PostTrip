package com.posttrip.journeydex.core.data.util

object LoginCached {
    var kakaoId = ""
    var nickname = ""

    fun clearCached() {
        kakaoId = ""
        nickname = ""
    }
}
