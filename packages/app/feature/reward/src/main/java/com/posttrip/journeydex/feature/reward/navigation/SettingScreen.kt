package com.posttrip.journeydex.feature.reward.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    onBackClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        JTopAppBar(
            navigationIcon = {
                Spacer(modifier = Modifier.size(40.dp))
//                IconButton(onClick = onBackClick) {
//                    Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
//                }
            },
            actions = {
                Spacer(modifier = Modifier.size(40.dp))
            },
            title = {
                Text(
                    text = "설정",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        )
        SettingItem(
            text = "서비스이용약관",
            onClick = onTermsClick
        )
        SettingItem(
            text = "개인정보처리방침",
            onClick = onPrivacyClick
        )
        SettingItem(
            text = "로그아웃",
            onClick = onLogoutClick
        )
        SettingItem(
            text = "버전 정보",
            onClick = {}
        )
        SettingItem(
            text = "회원 탈퇴",
            onClick = onWithdrawClick
        )
    }
}

@Composable
fun SettingItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            modifier = Modifier.padding(start = 30.dp),
            text = text
        )
    }
}

@Composable
fun JTopAppBar(
    navigationIcon: @Composable () -> Unit,
    actions: @Composable () -> Unit,
    title: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        navigationIcon()
        Box(
            modifier = Modifier.weight(1f), contentAlignment = Alignment.Center
        ) {
            title()
        }
        actions()
    }
}
