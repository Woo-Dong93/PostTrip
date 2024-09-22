package com.posttrip.journeydex.feature.home

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.navOptions
import coil.compose.AsyncImage
import com.posttrip.journeydex.core.data.model.mission.Mission
import com.posttrip.journeydex.core.data.model.mission.MissionStatus
import com.posttrip.journeydex.core.data.model.response.CourseList
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.core.data.util.LoginCached
import com.posttrip.journeydex.feature.home.component.CourseDetailBottomSheet
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLoadingShow: (Boolean) -> Unit,
    onDetail: (Course) -> Unit,
    onNavigateMap: (String) -> Unit,
    onNavigateAllMission : () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val recommendedCourse by viewModel.courses.collectAsStateWithLifecycle()
    val missions by viewModel.missions.collectAsStateWithLifecycle()
    var courseList by remember { mutableStateOf<CourseList?>(null) }

    LaunchedEffect(key1 = Unit) {
        launch {
            viewModel.shownLoading.collect {
                onLoadingShow(it)
            }
        }
        launch {
            viewModel.getRecommendedCourse("1")
        }
        launch {
            viewModel.getMissions()
        }
        launch {
            viewModel.courseDetail.collect {
                courseList = it
            }
        }
    }


    if (courseList != null) {
        CourseDetailBottomSheet(
            onDetail = {
                viewModel.cacheDetail(it)
                courseList = null
                onDetail(it)
            },
            courseList = courseList!!,
            onNavigateMap = {
                courseList = null
                onNavigateMap(it)
            },
            onDismiss = {
                courseList = null
            }
        )
    }

    HomeScreen(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFaFaFa)),
        recommendedCourse = recommendedCourse,
        mission = missions,
        onClick = {
            viewModel.getCourseDetail(it)
        },
        onNavigateAllMission = onNavigateAllMission,
        onFavoriteClick = {
            viewModel.favoriteCourse("1", it)
        }
    )

}

@Composable
fun HomeScreen(
    recommendedCourse: List<Course>,
    mission: List<Mission>,
    onClick: (Course) -> Unit,
    onNavigateAllMission : () -> Unit,
    onFavoriteClick: (Course) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        UserProfileSection(name = "${LoginCached.nickname} 님")
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
        ) {
            if (mission.isNotEmpty()) {
                item(span = {
                    GridItemSpan(2)
                }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "미션", fontSize = 16.sp)
                        Text(
                            modifier = Modifier.clickable {
                                onNavigateAllMission()
                            },
                            text = "전체보기", color = Color.Gray, fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(mission, span = {
                    GridItemSpan(2)
                }) {
                    Column {
                        CouponItem(couponName = it.title, status = it.statusType)
                    }

                }
            }

            item(span = {
                GridItemSpan(2)
            }) {
                Column {
                    if (mission.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Text(text = "추천 여행 코스", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                }
            }

            items(recommendedCourse) { course ->
                TravelCourseItem(
                    course = course,
                    onClick = onClick,
                    onFavoriteClick = onFavoriteClick
                )
            }
        }
    }
}

@Composable
fun UserProfileSection(name: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = "$name 안녕하세요!", fontSize = 24.sp)
            Text(text = "저니넥스에 오신 것을 환영해요!", fontSize = 12.sp)
        }
    }
}


@Composable
fun CouponItem(couponName: String, status: MissionStatus) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = couponName)
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

@Composable
fun TravelCourseItem(
    course: Course,
    onClick: (Course) -> Unit,
    onFavoriteClick: (Course) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                onClick(course)
            },
        shape = RoundedCornerShape(8.dp),
    ) {
        Box {
            AsyncImage(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,
                model = course.firstImage,
                contentDescription = null
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.0f),
                                Color.Black.copy(alpha = 0.75f),
                            ),
                        )
                    )
                    .align(Alignment.BottomCenter)
            )
            Text(
                text = course.title,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Box(
                modifier = Modifier.align(Alignment.TopEnd),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .size(32.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.15f),
                                    Color.Black.copy(alpha = 0.0f),
                                ),
                            )
                        )
                )
                IconButton(
                    onClick = {
                        onFavoriteClick(course)
                    },
                ) {
                    if (course.favorite) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            tint = Color.White
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = Color.White
                        )
                    }

                }
            }

        }
    }
}

data class TravelCourse(val name: String, val imageResId: Int)

