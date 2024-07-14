package com.posttrip.journeydex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.posttrip.journeydex.core.auth.KakaoAuthHelper
import com.posttrip.journeydex.feature.login.navigation.loginNavigationRoute
import com.posttrip.journeydex.feature.login.navigation.loginScreen

@Composable
fun JourneydexNavHost(
    navController: NavHostController,
    modifier : Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = loginNavigationRoute
    ){
        loginScreen()
    }
}