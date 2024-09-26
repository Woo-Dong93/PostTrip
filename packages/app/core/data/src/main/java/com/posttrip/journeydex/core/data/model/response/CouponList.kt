package com.posttrip.journeydex.core.data.model.response

import com.posttrip.journeydex.core.data.model.mission.Coupon
import kotlinx.serialization.Serializable

@Serializable
data class CouponList(
    val data : List<Coupon>
)
