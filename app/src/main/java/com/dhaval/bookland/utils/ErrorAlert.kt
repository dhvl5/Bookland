package com.dhaval.bookland.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorAlert(drawableRes: Int, text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
    ) {
        Column {
            Image(
                modifier = Modifier
                    .size(130.dp, 150.dp)
                    .align(Alignment.CenterHorizontally),
                imageVector = ImageVector.vectorResource(drawableRes),
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onSecondary),
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = text,
                style = TextStyle(
                    color = MaterialTheme.colors.onSecondary,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                ),
            )
        }
    }
}