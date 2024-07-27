package com.posttrip.journeydex

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Onboarding(
    step : Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.padding(horizontal = 20.dp)
    ) {
        if(step <= 2){
            TitleIntro(
                modifier = Modifier.fillMaxHeight(0.2f),
                title = when(step){
                    0 -> "어떤 여행을 선호하시나요?"
                    1 -> "선호하는 여행지가 있으신가요?"
                    2 -> "좋아하는 액티비티가 있으신가요?"
                    else -> ""
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            if(step <= 2)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        CardText(
                            step = step
                        )
                    }
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        CardText(
                            step = step
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        CardText(
                            step = step
                        )
                    }
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        CardText(
                            step = step
                        )
                    }
                }
            }
            else {
                Text(text = "환영합니다!!", modifier = Modifier.clickable { onClick() })
            }
        }
        if(step <= 2)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f),
            contentAlignment = Alignment.TopCenter
        ) {
            NextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                onClick = {
                    onClick()
                }
            )
        }
    }
}

@Composable
fun TitleIntro(
    title : String,
    intro : String = "저니덱스에 오신 것을 환영해요!",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = intro,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

    }
}

@Composable
fun NextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    containerColor: Color = Color(0xFFE1E1E1),
    contentColor: Color = Color(0xFF000000)
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onClick
    ) {
        Row(
            modifier = modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                modifier = Modifier.weight(1f),
                text = "다음",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun CardText(
    step : Int
){
    Text(text = "")
}