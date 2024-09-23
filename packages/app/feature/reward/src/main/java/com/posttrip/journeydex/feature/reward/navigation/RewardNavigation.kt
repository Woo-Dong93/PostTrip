package com.posttrip.journeydex.feature.reward.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val rewardNavigationRoute = "reward_route"
const val termsNavigationRoute = "terms_route"
const val privacyNavigationRoute = "privacy_route"
const val settingNavigationRoute = "setting_route"

fun NavController.navigateToReward(navOptions: NavOptions? = null) {
    this.navigate(rewardNavigationRoute, navOptions)
}

fun NavController.navigateToSetting(navOptions: NavOptions? = null) {
    this.navigate(settingNavigationRoute, navOptions)
}

fun NavController.navigateToTerms(navOptions: NavOptions? = null) {
    this.navigate(termsNavigationRoute, navOptions)
}

fun NavController.navigateToPrivacy(navOptions: NavOptions? = null) {
    this.navigate(privacyNavigationRoute, navOptions)
}


fun NavGraphBuilder.rewardScreen(
    onSettingClick : () -> Unit,
    onNavigateFavorite : () -> Unit,
) {
    composable(
        rewardNavigationRoute
    ) {
        MyPageScreen(
            onSettingClick= onSettingClick,
            onNavigateFavorite = onNavigateFavorite
        )
    }
}

fun NavGraphBuilder.settingScreen(
    onBackClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit
) {
    composable(
        route = settingNavigationRoute
    ) {
        SettingScreen(
            onBackClick = onBackClick,
            onTermsClick = onTermsClick,
            onPrivacyClick = onPrivacyClick,
            onLogoutClick = onLogoutClick,
            onWithdrawClick = onWithdrawClick
        )
    }
}

fun NavGraphBuilder.termsScreen(
    onBackClick: () -> Unit
) {
    composable(
        termsNavigationRoute
    ) {
        WebViewScreen(
            onBackClick= onBackClick,
            url = "https://purrfect-chill-5d6.notion.site/bb093d65359f4fcaaf0feb7bcbb8215a"
        )
    }
}

fun NavGraphBuilder.privacyScreen(
    onBackClick: () -> Unit
) {
    composable(
        privacyNavigationRoute
    ) {
        WebViewScreen(
            onBackClick= onBackClick,
            url = "https://purrfect-chill-5d6.notion.site/49bb46d26c604ff09913b9f67db1806e"
        )
    }
}
