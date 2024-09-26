package com.posttrip.journeydex.feature.dex.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.posttrip.journeydex.core.data.model.mission.Mission
import com.posttrip.journeydex.core.data.model.mission.MissionStatus
import com.posttrip.journeydex.core.data.model.travel.Character
import com.posttrip.journeydex.feature.dex.R
import com.posttrip.journeydex.feature.reward.navigation.CharacterCard

enum class DexScreenTab(val title: String) {
    Character("캐릭터"), Coupon("미션")
}

@Composable
fun DexScreen(
    modifier: Modifier = Modifier,
    viewModel: DexViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf<DexScreenTab>(DexScreenTab.Character) }
    val characters by viewModel.characters.collectAsStateWithLifecycle()
    val missions by viewModel.missions.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getCharacters()
        viewModel.getMissions()
    }

    DexScreen(
        selectedTab = selectedTab,
        onTabClick = {
            selectedTab = it
        },
        characters = characters,
        missions = missions,
        onMissionClick = viewModel::clickMission
    )
}

@Composable
fun DexScreen(
    selectedTab: DexScreenTab,
    onTabClick: (DexScreenTab) -> Unit,
    characters: List<Character>,
    missions: List<Mission>,
    onMissionClick: (Mission) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()

            .background(Color(0xFFFAFAFA))
            .padding(top = 22.dp),
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
                tab = DexScreenTab.Character,
                selected = selectedTab == DexScreenTab.Character,
                onClick = {
                    onTabClick(DexScreenTab.Character)
                }
            )
            TabCard(
                modifier = Modifier
                    .height(46.dp)
                    .weight(1f),
                tab = DexScreenTab.Coupon,
                selected = selectedTab == DexScreenTab.Coupon,
                onClick = {
                    onTabClick(DexScreenTab.Coupon)
                }
            )
        }
        when (selectedTab) {
            DexScreenTab.Character -> {
                if (characters.isNotEmpty()) {
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
                                onClick = {},
                                collected = character.collected
                            )
                        }
                    }
                } else {

                }
            }

            DexScreenTab.Coupon -> {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ){
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//
//                    }
//                }
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ){
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Image(
//                            modifier = Modifier.size(225.dp),
//                            painter = painterResource(R.drawable.ic_waiting),
//                            contentDescription = null
//                        )
//                        Spacer(modifier = Modifier.size(8.dp))
//                        Text(
//                            text = "열심히 준비 중인 기능이에요!"
//                        )
//                    }
//                }
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(missions) {
                        MissionCard(
                            it,
                            onClick = {
                                onMissionClick(it)
                            }
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
    tab: DexScreenTab,
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

@Composable
fun MissionCard(
    mission: Mission,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val character = mission.characters.firstOrNull()
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (character != null) {
            CharacterCard(
                modifier = Modifier.size(110.dp),
                character = character,
                onClick = {},
                collected = true
            )
        }
        Column(
            modifier.wrapContentSize()
        ) {
            Text(
                text = mission.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF3a3a3a),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.size(4.dp))
            Text(
                text = mission.description,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF3a3a3a),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.size(13.dp))
            MissionLevel(
                mission = mission,
                onClick = onClick
            )
        }
    }
}

@Composable
fun MissionLevel(
    mission: Mission,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (mission.statusType == MissionStatus.PENDING) {
            MissionBar(
                modifier = Modifier
                    .height(8.dp)
                    .weight(1f),
                color = Color(0xFFd9d9d9),
                shape = RoundedCornerShape(40.dp)
            )
            Spacer(
                Modifier.size(6.dp)
            )
            MissionButton(
                text = "시작하기",
                onClick = onClick,
                enabled = true
            )

        } else if (mission.statusType == MissionStatus.COMPLETED) {
            MissionBar(
                modifier = Modifier
                    .height(8.dp)
                    .weight(1f),
                color = Color(0xFF497CFF),
                shape = RoundedCornerShape(40.dp)
            )
            Spacer(
                Modifier.size(6.dp)
            )
            MissionButton(
                text = "완료",
                onClick = {},
                enabled = false
            )
        } else {
            if (mission.collectedCount == mission.collectionCount) {
                MissionBar(
                    modifier = Modifier
                        .height(8.dp)
                        .weight(1f),
                    color = Color(0xFF497CFF),
                    shape = RoundedCornerShape(40.dp)
                )
                Spacer(
                    Modifier.size(6.dp)
                )
                MissionButton(
                    text = "미션완료",
                    onClick = onClick,
                    enabled = true
                )
            } else {
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    for (i in 0 until mission.collectionCount) {
                        MissionBar(
                            modifier = Modifier
                                .height(8.dp)
                                .weight(1f),
                            color =
                            if (mission.collectedCount > i) Color(0xFF497CFF)
                            else Color(0xFFd9d9d9),
                            shape = if (i == 0)
                                RoundedCornerShape(
                                    topStart = 40.dp,
                                    bottomStart = 40.dp,
                                    topEnd = 0.dp,
                                    bottomEnd = 0.dp
                                )
                            else if (i == mission.collectionCount - 1) RoundedCornerShape(
                                topStart = 0.dp,
                                bottomStart = 0.dp,
                                topEnd = 40.dp,
                                bottomEnd = 40.dp
                            )
                            else RectangleShape
                        )
                    }
                }
                Spacer(
                    Modifier.size(6.dp)
                )
                Text(
                    text = "${mission.collectedCount}/${mission.collectionCount}"
                )
            }
        }

    }
}

@Composable
fun MissionBar(
    modifier: Modifier = Modifier,
    shape: Shape,
    color: Color
) {
    Spacer(
        modifier = modifier
            .clip(shape)
            .background(color)
    )
}

@Composable
fun MissionButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color(0xFF497CFF),
            disabledContentColor = Color(0xFFd9d9d9),
            disabledContainerColor = Color.White
        ),
        border = BorderStroke(1.dp, if (enabled) Color(0xFF497CFF) else Color(0xFFd9d9d9)),
        contentPadding = PaddingValues(

            start = 10.dp, end = 10.dp,
            top = 3.dp,
            bottom = 6.dp
        )
    ) {
        Box(
            modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }


    }
}
