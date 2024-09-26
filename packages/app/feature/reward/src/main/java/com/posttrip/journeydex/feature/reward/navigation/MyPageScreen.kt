package com.posttrip.journeydex.feature.reward.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.posttrip.journeydex.core.data.model.mission.Coupon
import com.posttrip.journeydex.core.data.model.travel.Character
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.core.data.util.LoginCached

@Composable
fun MyPageScreen(
    onSettingClick: () -> Unit,
    onNavigateFavorite: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf<MyPageScreenTab>(MyPageScreenTab.Character) }
    val missions by viewModel.missions.collectAsStateWithLifecycle()
    val characters by viewModel.characters.collectAsStateWithLifecycle()
    val courses by viewModel.courses.collectAsStateWithLifecycle()
    val coupons by viewModel.coupons.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getLikedCourses()
        viewModel.getCharacters()
        viewModel.getCoupons()
    }

    MyPageScreen(
        courses = courses,
        characters = characters,
        coupons = coupons,
        selectedTab = selectedTab,
        onTabClick = {
            selectedTab = it
        },
        onSettingClick = onSettingClick,
        onNavigateFavorite = onNavigateFavorite,
        modifier = modifier
    )

}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun MyPageScreen(
    courses: List<Course>,
    characters: List<Character>,
    coupons : List<Coupon>,
    selectedTab: MyPageScreenTab,
    onTabClick: (MyPageScreenTab) -> Unit,
    onSettingClick: () -> Unit,
    onNavigateFavorite: () -> Unit,
    modifier: Modifier = Modifier,
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
                IconButton(onClick = onSettingClick) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                }
            },
            title = {
                Text(
                    text = "마이 페이지",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        )

        Row(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                text = LoginCached.nickname + "님",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Column(
                modifier = Modifier
                    .padding(end = 40.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {
                            onNavigateFavorite()
                        }
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "저장한 코스",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )

                Text(
                    text = "${courses.size}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp
                )
            }
        }



        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
                .padding(
                    top = 24.dp
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
                    .height(46.dp),

                ) {
                TabCard(
                    modifier = Modifier
                        .height(46.dp)
                        .weight(1f),
                    tab = MyPageScreenTab.Character,
                    selected = selectedTab == MyPageScreenTab.Character,
                    onClick = {
                        onTabClick(MyPageScreenTab.Character)
                    }
                )
                TabCard(
                    modifier = Modifier
                        .height(46.dp)
                        .weight(1f),
                    tab = MyPageScreenTab.Coupon,
                    selected = selectedTab == MyPageScreenTab.Coupon,
                    onClick = {
                        onTabClick(MyPageScreenTab.Coupon)
                    }
                )
            }
            when (selectedTab) {
                MyPageScreenTab.Character -> {
                    if(characters.isNotEmpty()){
                        LazyVerticalGrid(
                            modifier = Modifier.fillMaxSize(),
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(20.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(characters) { character ->

                                CharacterCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f),
                                    character = character,
                                    collected = character.collected,
                                    onClick = {}
                                )
                            }
                        }
                    }else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = "수집한 캐릭터가 없습니다."
                            )
                        }
                    }


                }

                MyPageScreenTab.Coupon -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        if(coupons.isNotEmpty()){
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(20.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                items(coupons){
                                    CouponItem(
                                        couponTitle = it.info.title,
                                        couponDescription = it.info.description,
                                        isUsed = it.use,
                                        onClick = {

                                        }
                                    )
                                }
                            }

                        }else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = "수집한 쿠폰이 없습니다."
                                )
                            }
                        }

                    }
                }
            }
        }

    }


}


@Composable
fun CouponItem(
    couponTitle: String,
    couponDescription : String,
    isUsed: Boolean,
    onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.wrapContentHeight().weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = couponTitle,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,

            )
            Text(
                text = couponDescription,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(Modifier.size(8.dp))
        if(isUsed){
            Text(
                text ="사용완료",
                color = Color.Black,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(Color(0xFFc5c5c5), shape = RoundedCornerShape(4.dp))
                    .width(62.dp)
                    .padding(horizontal = 4.dp, vertical = 4.dp)
            )
        }

    }
}


@Composable
fun TabCard(
    selected: Boolean,
    tab: MyPageScreenTab,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
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

enum class MyPageScreenTab(val title: String) {
    Character("캐릭터"), Coupon("쿠폰")
}

