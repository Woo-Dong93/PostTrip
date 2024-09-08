package com.posttrip.journeydex.feature.map

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.feature.home.TravelCourseItem

@Composable
fun SearchScreen(
    onDismiss : (Course) -> Unit,
    modifier: Modifier = Modifier,
    courseList : List<Course>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SearchTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        FilterButtonSection(
            modifier = Modifier.fillMaxWidth()
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
        ) {

            items(courseList) { course ->
                TravelCourseItem(
                    course = course,
                    onClick = {
                        onDismiss(course)
                    },
                    onFavoriteClick = {

                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    readOnly : Boolean = false
) {
    var query by remember { mutableStateOf("") }

    TextField(
        value = query,
        readOnly = readOnly,
        onValueChange = { query = it },
        placeholder = {
            Text(text = "여행하고 싶은 곳이 있으신가요?", color = Color.LightGray)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        modifier = modifier
            .background(Color.White, RoundedCornerShape(8.dp)),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun SearchText(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "여행하고 싶은 곳이 있으신가요?",
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun FilterButtonSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterButton(text = "여행지")
        FilterButton(text = "스타일")
        FilterButton(text = "동행")
    }
}

@Composable
fun FilterButton(text: String) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.outlinedButtonColors(),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Text(text = text)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown"
            )
        }
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            // 필터 옵션 예시
//            DropdownMenuItem(onClick = { expanded = false }) {
//                Text("Option 1")
//            }
//            DropdownMenuItem(onClick = { expanded = false }) {
//                Text("Option 2")
//            }
//            DropdownMenuItem(onClick = { expanded = false }) {
//                Text("Option 3")
//            }
//        }
    }
}