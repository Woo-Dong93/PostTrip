package com.posttrip.journeydex.core.designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun JButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled : Boolean = true,
    shape: Shape = RoundedCornerShape(30.dp),
    contentPadding: PaddingValues = PaddingValues(vertical = 18.dp, horizontal = 30.dp),
    content: @Composable () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = ButtonColors(
            containerColor = Color(0xFF497CFF),
            disabledContainerColor = Color(0xFF777777),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        contentPadding = contentPadding,
        onClick = onClick
    ) {
        content()
    }
}
