package com.posttrip.journeydex.feature.map.navigation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.feature.map.MapScreen

const val mapNavigationRoute = "map_route"
const val courseDetailNavigationRoute = "course_detail_route"
internal const val contentIdArg = "contentId"

fun NavController.navigateToMap(contentId : String = "-1",navOptions: NavOptions? = null) {
    this.navigate(mapNavigationRoute + "/${contentId}", navOptions = navOptions)
}

fun NavController.navigateToCourseDetail(contentId : String = "-1",navOptions: NavOptions? = null) {
    this.navigate(courseDetailNavigationRoute + "/${contentId}", navOptions = navOptions)
}

fun NavGraphBuilder.mapScreen(
    onDetail : (Course) -> Unit,
    onLoadingShow : (Boolean) -> Unit,
) {
    composable(
        route = "${mapNavigationRoute}/{$contentIdArg}",
        arguments = listOf(navArgument(contentIdArg) { type = NavType.StringType })
    ) {navBackStackEntry ->
        val missionId = navBackStackEntry.arguments?.getString(contentIdArg)
        Log.d("123123",missionId ?: "")
        MapScreen(
            onDetail = onDetail,
            onLoadingShow = onLoadingShow
        )
    }
}
//
//fun NavGraphBuilder.courseDetailScreen(
//    onClickBack : () -> Unit
//) {
//    composable(
//        route = "${courseDetailNavigationRoute}/{$contentIdArg}",
//        arguments = listOf(navArgument(contentIdArg) { type = NavType.StringType })
//    ) {
//        CourseDetailScreen()
//    }
//}
