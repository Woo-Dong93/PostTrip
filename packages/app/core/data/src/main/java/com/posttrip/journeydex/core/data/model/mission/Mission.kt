package com.posttrip.journeydex.core.data.model.mission

import android.graphics.Color
import kotlinx.serialization.Serializable

enum class MissionStatus(val title: String, val colorLong: Long) {
    PENDING("참여하기", 0xFFADDCFF),
    ACTIVE("참여중",0xFFAEF4A9),
    COMPLETED("완료",0xFFAEF4A9)
}


@Serializable
data class Mission(
    val id: String = "",
    val contentId: String = "",
    val title: String = "",
    val description: String = "",
    val status: String = ""
) {
    val statusType: MissionStatus by lazy {
        MissionStatus.entries.find {
            it.name == status
        } ?: MissionStatus.PENDING
    }
}
