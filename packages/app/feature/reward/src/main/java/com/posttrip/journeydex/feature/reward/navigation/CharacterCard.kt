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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.posttrip.journeydex.core.data.model.travel.Character

@Composable
fun CharacterCard(
    character: Character,
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
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(character.title)
//            Image(
//                modifier = Modifier.fillMaxSize(),
//                painter = painterResource(
//
//                ),
//                contentScale = ContentScale.Fit,
//                contentDescription = null
//            )
        }
    }
}
