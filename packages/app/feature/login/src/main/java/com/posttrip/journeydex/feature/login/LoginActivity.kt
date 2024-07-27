package com.posttrip.journeydex.feature.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.posttrip.journeydex.core.auth.KakaoAuthHelper
import com.posttrip.journeydex.feature.login.ui.theme.JourneydexTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    @Inject
    lateinit var kakaoAuthHelper : KakaoAuthHelper

    private val viewModel : LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initObserver()
        setContent {
            JourneydexTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)){
                        LoginScreen(
                            modifier = Modifier.fillMaxSize(),
                            onClickKakaoLogin = {
                                viewModel.updatedEvent(LoginEvent.Kakao_Login)
                            }
                        )
                    }

                }
            }
        }
    }

    private fun initObserver(){
        lifecycleScope.launch {
            viewModel.loginEvent.collect {
                kakaoLogin()
            }
        }
    }

    private suspend fun kakaoLogin(){
        try {
       //     kakaoAuthHelper.unlink()
            val userData = kakaoAuthHelper.authorize()
            if(userData.uId.isNotBlank()){
                intent.putExtra("nickname", userData.nickname)
                intent.putExtra("uId", userData.uId)
                setResult(RESULT_OK, intent)
                finish()
            }

        }catch (e: Exception){
            Log.e("kakaoLogin",e.message.toString())
        }
    }
}
