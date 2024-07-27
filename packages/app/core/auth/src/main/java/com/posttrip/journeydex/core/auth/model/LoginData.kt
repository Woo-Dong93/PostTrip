package com.posttrip.journeydex.core.auth.model

import android.provider.ContactsContract.CommonDataKinds.Nickname

data class LoginData(
    val uId : String,
    val nickname : String
)
