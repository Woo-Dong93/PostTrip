package com.posttrip.journeydex.core.data.model.mission

import kotlinx.serialization.Serializable

@Serializable
data class Coupon(
    val userId : String = "",
    val use : Boolean = false,
    val info : CouponInfo
)

@Serializable
data class CouponInfo(
    val id : String = "",
    val missionId : String = "",
    val title : String = "",
    val description : String = ""
)
