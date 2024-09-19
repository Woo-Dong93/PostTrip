package com.posttrip.journeydex.feature.reward.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val rewardNavigationRoute = "reward_route"
const val termsNavigationRoute = "terms_route"
const val privacyNavigationRoute = "privacy_route"

fun NavController.navigateToReward(navOptions: NavOptions? = null) {
    this.navigate(rewardNavigationRoute, navOptions)
}


fun NavController.navigateToTerms(navOptions: NavOptions? = null) {
    this.navigate(termsNavigationRoute, navOptions)
}

fun NavController.navigateToPrivacy(navOptions: NavOptions? = null) {
    this.navigate(privacyNavigationRoute, navOptions)
}

fun NavGraphBuilder.rewardScreen(
    onBackClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit
) {
    composable(
        route = rewardNavigationRoute
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
