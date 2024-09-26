package com.posttrip.journeydex.feature.reward.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.posttrip.journeydex.core.data.model.mission.Mission
import com.posttrip.journeydex.core.data.model.travel.Character
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.core.data.util.LoginCached
import com.posttrip.journeydex.feature.reward.R

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

    LaunchedEffect(Unit) {
        viewModel.getLikedCourses()
        viewModel.getCharacters()
    }

    MyPageScreen(
        courses = courses,
        characters = characters,
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
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                modifier = Modifier.size(225.dp),
                                painter = painterResource(R.drawable.ic_waiting),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = "열심히 준비 중인 기능이에요!"
                            )
                        }
                    }
                }
            }
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

