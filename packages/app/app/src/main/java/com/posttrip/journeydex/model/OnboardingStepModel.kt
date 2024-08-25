package com.posttrip.journeydex.model

data class OnboardingStepModel(
    val index : Int = 0,
    val style : String = "",
    val destination : String = "",
    val type : String = ""
) {
    fun isSelected(
        targetStepIndex : Int,
        target : Int
    ) : Boolean {
        val targetString = getTargetString(targetStepIndex,target)

        return if(targetStepIndex == 0) style == targetString
        else if(targetStepIndex == 1) destination == targetString
        else type == targetString
    }

    companion object {
        const val STYLE_HEALING = "healing"
        const val STYLE_CULTURE = "culture"
        const val STYLE_GOURMET = "gourmet"
        const val STYLE_ACTIVITY = "activity"

        const val DESTINATION_BEACH = "beach"
        const val DESTINATION_MOUNTAIN = "mountain"
        const val DESTINATION_CITY = "city"
        const val DESTINATION_ISLAND = "island"

        const val TYPE_SOLO = "solo"
        const val TYPE_FAMILY = "family"
        const val TYPE_COUPLE = "couple"
        const val TYPE_FRIENDS = "friends"

        fun getTargetString(
            stepIndex : Int,
            target : Int,
        ) : String {
            return if(stepIndex == 0){
                when(target){
                    0 -> STYLE_HEALING
                    1 -> STYLE_CULTURE
                    2 -> STYLE_GOURMET
                    else -> STYLE_ACTIVITY
                }
            }else if(stepIndex == 1){
                when(target){
                    0 -> DESTINATION_BEACH
                    1 -> DESTINATION_MOUNTAIN
                    2 -> DESTINATION_CITY
                    else -> DESTINATION_ISLAND
                }
            }else {
                when(target){
                    0 -> TYPE_SOLO
                    1 -> TYPE_FAMILY
                    2 -> TYPE_COUPLE
                    else -> TYPE_FRIENDS
                }
            }
        }
    }
}
