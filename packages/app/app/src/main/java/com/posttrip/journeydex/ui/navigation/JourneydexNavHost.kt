package com.posttrip.journeydex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.feature.dex.navigation.dexScreen
import com.posttrip.journeydex.feature.home.navigation.allMissionScreen
import com.posttrip.journeydex.feature.home.navigation.favoriteScreen
import com.posttrip.journeydex.feature.home.navigation.homeNavigationRoute
import com.posttrip.journeydex.feature.home.navigation.homeScreen
import com.posttrip.journeydex.feature.home.navigation.navigateToAllMission
import com.posttrip.journeydex.feature.home.navigation.navigateToFavorite
import com.posttrip.journeydex.feature.map.navigation.mapScreen
import com.posttrip.journeydex.feature.reward.navigation.navigateToPrivacy
import com.posttrip.journeydex.feature.reward.navigation.navigateToReward
import com.posttrip.journeydex.feature.reward.navigation.navigateToSetting
import com.posttrip.journeydex.feature.reward.navigation.navigateToTerms
import com.posttrip.journeydex.feature.reward.navigation.privacyScreen
import com.posttrip.journeydex.feature.reward.navigation.rewardScreen
import com.posttrip.journeydex.feature.reward.navigation.settingScreen
import com.posttrip.journeydex.feature.reward.navigation.termsScreen

@Composable
fun JourneydexNavHost(
    onLoadingShow : (Boolean) -> Unit,
    onDetail  : (Course) -> Unit,
    onNavigateMap : (String) -> Unit,
    onLogout: () -> Unit,
    navController: NavHostController,
    modifier : Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = homeNavigationRoute
    ) {
        homeScreen(
            onLoadingShow = onLoadingShow,
            onDetail = {
//                navController.navigateToCourseDetail(
//                    contentId = it.contentId
//                )
                onDetail(it)
            },
            onNavigateMap = onNavigateMap,
            onNavigateAllMission = {
                navController.navigateToAllMission()
            }
        )
        mapScreen(
            onDetail = {
//                navController.navigateToCourseDetail(
//                    contentId = it.contentId
//                )
                onDetail(it)
            },
            onLoadingShow = onLoadingShow
        )
        dexScreen()
        rewardScreen(
            onSettingClick = {
                navController.navigateToSetting()
            },
            onNavigateFavorite = {
                navController.navigateToFavorite()
            }

        )
        settingScreen(
            onBackClick = {
                navController.popBackStack()
            },
            onTermsClick = {
                navController.navigateToTerms()
            },
            onPrivacyClick = {
                navController.navigateToPrivacy()
            },
            onLogoutClick = onLogout,
            onWithdrawClick = onLogout
        )
        termsScreen {
            navController.popBackStack()
        }
        privacyScreen {
            navController.popBackStack()
        }
        allMissionScreen(
            onMissionClick = {
                onNavigateMap(it.contentId)
            },
            onBackClick = {
                navController.popBackStack()
            }
        )
        favoriteScreen(
            onBackClick = {
                navController.popBackStack()
            }
        )
//        courseDetailScreen(
//            onClickBack = {
//                navController.popBackStack()
//            }
//        )
    }
}
