package com.posttrip.journeydex.feature.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
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
                    modifier = Modifier.fillMaxWidth().height(40.dp).clickable {
                        onSelect(it)
                    }.padding(horizontal = 24.dp),
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
                    modifier = Modifier.fillMaxWidth().height(40.dp).clickable {
                        onSelect(it)
                    }.padding(horizontal = 24.dp),
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
                    modifier = Modifier.fillMaxWidth().height(40.dp).clickable {
                        onSelect(it)
                    }.padding(horizontal = 24.dp),
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
