package com.posttrip.journeydex.feature.reward.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.posttrip.journeydex.core.data.model.travel.Character
import com.posttrip.journeydex.feature.reward.R

@Composable
fun CharacterCard(
    character: Character,
    collected : Boolean,
    onClick: (Character) -> Unit,
    modifier: Modifier = Modifier,
) {

    Card (
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF),
            contentColor = Color.Black
        ),
        border = BorderStroke(
            1.dp, Color(0xFFEEEEEE)
        ),
//        contentPadding = PaddingValues(
//            0.dp
//        ),
//        onClick = {
//
//        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize().alpha(
                    if(collected) 1f
                    else 0.2f
                ),
            contentAlignment = Alignment.Center
        ) {
            //Text(character.title)
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(
                    if(character.title == "seoul") R.drawable.img_seoul_char
                    else if(character.title == "busan") R.drawable.img_busan_char
                    else R.drawable.img_jeonju_char
                ),
                contentScale = ContentScale.Fit,
                contentDescription = null
            )
        }
    }
}
