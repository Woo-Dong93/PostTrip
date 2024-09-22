package com.posttrip.journeydex.feature.dex.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

enum class DexScreenTab(val title: String) {
    Character("캐릭터 미션"), Coupon("쿠폰 미션")
}

@Composable
fun DexScreen(
    modifier: Modifier = Modifier,
    viewModel: DexViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf<DexScreenTab>(DexScreenTab.Character) }


    DexScreen(
        selectedTab = selectedTab,
        onTabClick = {
            selectedTab = it
        }
    )
}

@Composable
fun DexScreen(
    selectedTab: DexScreenTab,
    onTabClick : (DexScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()

            .background(Color(0xFFFAFAFA))
            .padding(top = 22.dp)
        ,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp).height(46.dp),

        ){
            TabCard(
                modifier = Modifier.height(46.dp).weight(1f),
                tab = DexScreenTab.Character,
                selected = selectedTab == DexScreenTab.Character,
                onClick = {
                    onTabClick(DexScreenTab.Character)
                }
            )
            TabCard(
                modifier = Modifier.height(46.dp).weight(1f),
                tab = DexScreenTab.Coupon,
                selected = selectedTab == DexScreenTab.Coupon,
                onClick = {
                    onTabClick(DexScreenTab.Coupon)
                }
            )
        }
        when(selectedTab){
            DexScreenTab.Character -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                }
            }
            DexScreenTab.Coupon -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                    }
                }
            }
        }

    }
}


@Composable
fun TabCard(
    selected: Boolean,
    tab: DexScreenTab,
    onClick : () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clip(RoundedCornerShape(14.dp)).clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Color(0xFF48484A) else Color(0xFFF6F5F6),
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = tab.title,
                color = if (selected) Color.White else Color(0xFF3A3A3C)
            )
        }
    }
}

