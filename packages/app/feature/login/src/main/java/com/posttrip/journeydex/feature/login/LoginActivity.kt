package com.posttrip.journeydex.feature.login

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                Scaffold(modifier = Modifier.fillMaxSize().background(Color(0xFFFEEDFF))) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFFEEDFF)).padding(innerPadding)){
                        LoginScreen(
                            modifier = Modifier.fillMaxSize(),
                            onClickKakaoLogin = {
                                viewModel.updatedEvent(LoginEvent.LoginByKakao)
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
                when(it){
                    is LoginEvent.LoginByKakao -> {
                        kakaoLogin()
                    }
                    is LoginEvent.LoginSuccess -> {
                        intent.putExtra("needsOnboarding", it.needsOnboarding)
                        intent.putExtra("nickname", it.nickname)
                        intent.putExtra("id", it.id)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }
    }

    private suspend fun kakaoLogin(){
        try {
            val userData = kakaoAuthHelper.authorize()
            if(userData.uId.isNotBlank()){
                viewModel.login(
                    id = userData.uId,
                    nickname = userData.nickname,
                    authProvider = LoginViewModel.Companion.AuthProvider.kakao,
                    email = userData.email
                )
            }

        }catch (e: Exception){
            Log.e("kakaoLogin",e.message.toString())
        }
    }
}
