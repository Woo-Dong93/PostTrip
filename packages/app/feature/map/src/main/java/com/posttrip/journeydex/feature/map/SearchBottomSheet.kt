package com.posttrip.journeydex.feature.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.posttrip.journeydex.core.data.model.travel.Course

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelStyleKeywordBottomSheet(
    onSelect : (TravelStyleKeyword) -> Unit,
    onDismiss: () -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        shape = RoundedCornerShape(
            topStart = 35.dp,
            topEnd = 35.dp
        ),
        containerColor = Color.White,
        windowInsets = WindowInsets.displayCutout
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            TravelStyleKeyword.entries.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clickable {
                            onSelect(it)
                        }
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = it.title
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationTypeKeywordBottomSheet(
    onSelect : (DestinationTypeKeyword) -> Unit,
    onDismiss: () -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        shape = RoundedCornerShape(
            topStart = 35.dp,
            topEnd = 35.dp
        ),
        containerColor = Color.White,
        windowInsets = WindowInsets.displayCutout
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            DestinationTypeKeyword.entries.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clickable {
                            onSelect(it)
                        }
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = it.title
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelTypeKeywordBottomSheet(
    onSelect : (TravelTypeKeyword) -> Unit,
    onDismiss: () -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        shape = RoundedCornerShape(
            topStart = 35.dp,
            topEnd = 35.dp
        ),
        containerColor = Color.White,
        windowInsets = WindowInsets.displayCutout
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            TravelTypeKeyword.entries.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clickable {
                            onSelect(it)
                        }
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = it.title
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AreaBottomSheet(
    onSelect : (Area) -> Unit,
    onDismiss: () -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        shape = RoundedCornerShape(
            topStart = 35.dp,
            topEnd = 35.dp
        ),
        dragHandle = null,
        containerColor = Color.White,
        windowInsets = WindowInsets.displayCutout
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .navigationBarsPadding()
        ) {
            LazyColumn(
                modifier = Modifier .navigationBarsPadding().heightIn(max = 420.dp)
            ) {
                items(Area.entries){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clickable {
                                onSelect(it)
                            }
                            .padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = it.nameKor
                        )
                    }
                }
            }

        }
    }
}

enum class Area(val code: String, val course: Int, val nameKor: String) {
    SEOUL("1", 52, "서울"),
    INCHEON("2", 34, "인천"),
    DAEJEON("3", 13, "대전"),
    DAEGU("4", 19, "대구"),
    GWANGJU("5", 12, "광주"),
    BUSAN("6", 23, "부산"),
    ULSAN("7", 12, "울산"),
    SEJONG("8", 5, "세종특별자치시"),
    GYEONGGI_DO("31", 158, "경기도"),
    GANGWON_SPECIAL("32", 161, "강원특별자치도"),
    CHUNGCHEONGBUK_DO("33", 70, "충청북도"),
    CHUNGCHEONGNAM_DO("34", 86, "충청남도"),
    GYEONGSANGBUK_DO("35", 125, "경상북도"),
    GYEONGSANGNAM_DO("36", 116, "경상남도"),
    JEONBUK_SPECIAL("37", 67, "전북특별자치도"),
    JEOLLANAM_DO("38", 99, "전라남도"),
    JEJU("39", 17, "제주도");
}
