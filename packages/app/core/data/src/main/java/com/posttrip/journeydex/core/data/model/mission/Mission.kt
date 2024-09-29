package com.posttrip.journeydex.core.data.model.mission

import android.graphics.Color
import com.posttrip.journeydex.core.data.model.travel.Character
import kotlinx.serialization.Serializable

enum class MissionStatus(val title: String, val colorLong: Long) {
    ACTIVE("참여중",0xFFAEF4A9),
    PENDING("참여하기", 0xFFADDCFF),
    COMPLETED("완료",0xFFAEF4A9)
}


@Serializable
data class Mission(
    val id: String = "",
    val contentId: String = "",
    val title: String = "",
    val description: String = "",
    val collectionCount : Int = 0,
    val collectedCount : Int = 0,
    val status: String = "",
    val characters : List<Character> = emptyList()
) {
    val statusType: MissionStatus by lazy {
        MissionStatus.entries.find {
            it.name == status
        } ?: MissionStatus.PENDING
    }
}
