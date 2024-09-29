package com.posttrip.journeydex

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.feature.home.MissionItem
import kotlinx.coroutines.launch

@Composable
fun CourseDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: CourseDetailViewModel = hiltViewModel()
) {
    //val courseDetail by viewModel.courseDetail.co()
//
//    LaunchedEffect(key1 = Unit) {
//        viewModel.getCourseDetail()
//    }
//
//    if(courseDetail != null){
//        CourseDetailScreen(course = courseDetail!!)
//    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CourseDetailScreen(
    course: Course,
    onDismiss: () -> Unit,
    onMissionClick : (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CourseDetailViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val missions by viewModel.missions.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getMissionListByCourse(course.contentId)
    }

    BackHandler(enabled = true) {
        coroutineScope.launch {
            onDismiss()
        }
    }
    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize().navigationBarsPadding()
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ){
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
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
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )

                        }
                    }
                    item {
                        Text(
                            text = course.title,
                            modifier = Modifier.padding(20.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(missions){
                        MissionItem(
                            missionName = it.title,
                            status = it.statusType
                        ) {
                            onDismiss()
                            onMissionClick(it.contentId)
                        }
                    }
                    item{
                        Text(
                            text = "상세정보",
                            modifier = Modifier.padding(20.dp)
                        )
                        Text(
                            text = course.overview,
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.7f),
                                    Color.White.copy(alpha = 0.0f),
                                    Color.White.copy(alpha = 0.0f),
                                ),
                            )
                        )
                )
                Row(
                    modifier = Modifier.statusBarsPadding()
                ) {
                    IconButton(onClick = onDismiss) {
                        Image(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = null)
                    }

                }
            }



        }
    }

}
