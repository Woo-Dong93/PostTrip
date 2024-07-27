package com.posttrip.journeydex

import android.content.Intent
import android.os.Bundle
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
import com.posttrip.journeydex.feature.login.LoginActivity
import com.posttrip.journeydex.ui.JourneydexApp
import com.posttrip.journeydex.ui.theme.JourneydexTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val nickname = it.data?.getStringExtra("nickname")
                val uId = it.data?.getStringExtra("uId")
                viewModel.updateData(uId?.isNotBlank() == true)
            }
        }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initObserver()
        setContent {
            val isLogged by viewModel.isLogged.collectAsState()
            val step by viewModel.step.collectAsState()
            if (isLogged) {
                JourneydexTheme {
                    if (step <= 3) {
                        Onboarding(
                            step = step,
                            onClick = {
                                viewModel.updateStep(step + 1)
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        JourneydexApp()
                    }

                }
            }

        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.isLogged.collect {
                if (!it) {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    resultLauncher.launch(intent)
                }
            }
        }
    }

}
