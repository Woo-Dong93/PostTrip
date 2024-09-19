package com.posttrip.journeydex.core.designsystem.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun JDialog(
    title: String,
    onDismissRequest: () -> Unit,
    onCancel : () -> Unit,
    onClickOk: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    okText: String? = null,
    cancelText: String? = null,
    shape: Shape = RoundedCornerShape(20.dp),
    dialogInnerPadding: PaddingValues = PaddingValues(
        top = 40.dp,
        bottom = 34.dp,
        start = 24.dp,
        end = 24.dp
    ),
    dialogProperties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false
    )
) {
    Dialog(
        properties = dialogProperties,
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 20.dp)
                .clip(shape)
                .background(Color.White)
                .padding(dialogInnerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
            if (description != null) {
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF777777)
                )
            }
            if (okText != null) {
                JButton(
                    onClick = onClickOk,
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .fillMaxWidth(),
                ){
                    Text(
                        text = okText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }

            if (cancelText != null) {
                Text(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = onCancel
                        ),
                    text = cancelText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = Color(0xFFB7B7B7)
                )
            }
        }
    }
}
