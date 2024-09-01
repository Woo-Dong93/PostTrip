package com.posttrip.journeydex

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.posttrip.journeydex.core.data.model.response.LoginData
import com.posttrip.journeydex.feature.login.LoginActivity
import com.posttrip.journeydex.ui.JourneydexApp
import com.posttrip.journeydex.ui.theme.JourneydexTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val needsOnboarding = it.data?.getBooleanExtra("needsOnboarding", true) ?: true
                val id = it.data?.getStringExtra("id") ?: ""
                val nickname = it.data?.getStringExtra("nickname") ?: ""

                viewModel.updateData(
                    LoginData(
                        id = id,
                        nickname = nickname,
                        onboarding = needsOnboarding
                    )
                )
            }
        }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initObserver()
        setContent {
            val typeFromLogin by viewModel.typeFromLogin.collectAsState()
            val loginData by viewModel.loginData.collectAsState()
            JourneydexTheme {
                JourneydexApp(
                    typeFromLogin = typeFromLogin,
                    loginData = loginData,
                    onTypeFormLoginChanged = {
                        viewModel.updateTypeFromLogin(MainViewModel.Companion.TypeFromLogin.GoToHome)
                    }
                )
            }
        }
    }

    private fun initObserver() {
//        lifecycleScope.launch {
//            viewModel.typeFromLogin.collect {
//                if (it == MainViewModel.Companion.TypeFromLogin.None) {
//                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
//                    resultLauncher.launch(intent)
//                }
//            }
//        }
    }
}
