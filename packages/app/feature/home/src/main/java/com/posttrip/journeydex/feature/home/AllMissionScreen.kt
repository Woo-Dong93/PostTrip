package com.posttrip.journeydex.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.posttrip.journeydex.core.data.model.mission.Mission
import com.posttrip.journeydex.core.data.model.mission.MissionStatus
import com.posttrip.journeydex.feature.reward.navigation.JTopAppBar

enum class AllMissionScreenTab(val title: String) {
    Course("코스"), Event("이벤트")
}

@Composable
fun AllMissionScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AllMissionViewModel = hiltViewModel()
) {
    val missions by viewModel.missions.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf<AllMissionScreenTab>(AllMissionScreenTab.Course) }

    LaunchedEffect(Unit) {
        viewModel.getMissions()
    }

    AllMissionScreen(
        missions = missions,
        selectedTab = selectedTab,
        onTabClick = {
            selectedTab = it
        },
        onBackClick = onBackClick
    )
}

@Composable
fun AllMissionScreen(
    missions: List<Mission>,
    selectedTab: AllMissionScreenTab,
    onTabClick : (AllMissionScreenTab) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)),
    ) {
        JTopAppBar(
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
                }
            },
            actions = {
                Spacer(modifier = Modifier.size(40.dp))
            },
            title = {
                Text(
                    text = "미션",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp).height(46.dp),

        ){
            TabCard(
                modifier = Modifier.height(46.dp).weight(1f),
                tab = AllMissionScreenTab.Course,
                selected = selectedTab == AllMissionScreenTab.Course,
                onClick = {
                    onTabClick(AllMissionScreenTab.Course)
                }
            )
            TabCard(
                modifier = Modifier.height(46.dp).weight(1f),
                tab = AllMissionScreenTab.Event,
                selected = selectedTab == AllMissionScreenTab.Event,
                onClick = {
                    onTabClick(AllMissionScreenTab.Event)
                }
            )
        }
        when(selectedTab){
            AllMissionScreenTab.Course -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(missions){
                        MissionItem(
                            missionName = it.title,
                            status = it.statusType
                        )
                    }
                }
            }
            AllMissionScreenTab.Event -> {
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


@Composable
fun TabCard(
    selected: Boolean,
    tab: AllMissionScreenTab,
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

@Composable
fun MissionCard(title: String, location: String, status: String, imageUrl: String = "") {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
//            Image(
//                painter = rememberImagePainter(imageUrl),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(RoundedCornerShape(8.dp))
//            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(text = title,)
                Text(text = location, )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = status,
                    color = if (status == "참여중") Color.Green else Color.Gray,
                    modifier = Modifier
                        .background(
                            color = if (status == "참여중") Color(0xFF81C784) else Color(0xFFB0BEC5),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun MissionItem(missionName: String, status: MissionStatus) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.wrapContentHeight().weight(1f)
        ) {
            Text(text = missionName)

        }
        Spacer(Modifier.size(8.dp))
        Text(
            text = status.title,
            color = Color.Black,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(Color(status.colorLong), shape = RoundedCornerShape(4.dp))
                .width(62.dp)
                .padding(horizontal = 4.dp, vertical = 4.dp)
        )
    }
}
