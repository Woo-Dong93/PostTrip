package com.posttrip.journeydex.feature.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class Mission(
    val title : String,
    val state : String
)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 24.dp, horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            HomeCharacter()
            HomeMission()
        }
        items(listOf(
            Mission("숙박비 10% 쿠폰", "참여중"),
            Mission("외식 10% 쿠폰", "완료"),
            Mission("숙박비 50% 쿠폰", "미참여")
        )){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight(),
                        text = it.title
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight(),
                        textAlign = TextAlign.End,
                        text = it.title
                    )
                }
            }
        }
        item {
            HomeCourse()
        }
    }
}

@Composable
fun HomeCharacter() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f),
            contentAlignment = Alignment.Center
        ){
            Spacer(modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color.Gray)
            )
        }

        Text(
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f),
            text = "대표 캐릭터명"
        )
    }
}

@Composable
fun HomeMission(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(1f),
                text = "미션하고 혜택받기",
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(1f),
                text = "전체보기",
                textAlign = TextAlign.End
            )
        }
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "인기 미션",
        )
    }
}

@Composable
fun HomeCourse(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f),
            text = "나의 여행 코스",
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "OOO를 위한 추천 코스"
        )
    }
}