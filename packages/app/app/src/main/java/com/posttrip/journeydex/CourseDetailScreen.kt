package com.posttrip.journeydex

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.posttrip.journeydex.core.data.model.travel.Course

@Composable
fun CourseDetailScreen(
    course: Course,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                model = course.firstImage,
                contentDescription = null
            )
        }
        Text(
            text = course.title,
            modifier = Modifier.padding(20.dp),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "상세정보",
            modifier = Modifier.padding(20.dp)
        )
        Text(
            text = course.overview,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom =  20.dp)
        )

    }
}
