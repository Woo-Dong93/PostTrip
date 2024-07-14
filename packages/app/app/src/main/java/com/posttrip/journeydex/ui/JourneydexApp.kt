package com.posttrip.journeydex.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.posttrip.journeydex.ui.navigation.JourneydexNavHost

@Composable
fun JourneydexApp() {
    val navController = rememberNavController()

    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            JourneydexNavHost(
                navController = navController,
            )
        }
    }
}