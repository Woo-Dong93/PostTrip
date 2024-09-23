package com.posttrip.journeydex.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.feature.reward.navigation.JTopAppBar

@Composable
fun FavoriteScreen(
    onBackClick: () -> Unit,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val courses by viewModel.courses.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getLikedCourses()
    }
    FavoriteScreen(
        courses,
        onBackClick = onBackClick,
        onFavoriteClick = {

        },
        onClick = {}
    )
}

@Composable
fun FavoriteScreen(
    courses : List<Course>,
    onBackClick: () -> Unit,
    onFavoriteClick: (Course) -> Unit,
    onClick : (Course) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
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
                    text = "저장한 코스",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(courses) { course ->
                TravelCourseItem(
                    course = course,
                    onClick = onClick,
                    onFavoriteClick = onFavoriteClick,
                    visibleFavorite = false
                )
            }
        }
    }

}


