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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.posttrip.journeydex.feature.login.LoginActivity
import com.posttrip.journeydex.ui.JourneydexApp
import com.posttrip.journeydex.ui.theme.JourneydexTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                val needsOnboarding = it.data?.getBooleanExtra("needsOnboarding",true) ?: true
                viewModel.updateData(needsOnboarding)
            }
        }

    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
       // initObserver()
        setContent {
            JourneydexTheme {
                JourneydexApp()
            }
//            val typeFromLogin by viewModel.typeFromLogin.collectAsState()
//            if(typeFromLogin != MainViewModel.Companion.TypeFromLogin.None){
//                JourneydexTheme {
//                    JourneydexApp()
//                }
//            }else {
//
//            }

        }
    }

    private fun initObserver(){
        lifecycleScope.launch {
            viewModel.typeFromLogin .collect {
                if(it == MainViewModel.Companion.TypeFromLogin.None){
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    resultLauncher.launch(intent)
                }
            }
        }
    }
}
