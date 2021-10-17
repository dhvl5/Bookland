package com.dhaval.bookland.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MoreItem(icon: Int, title: String, description: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
        .fillMaxWidth()
        .height(70.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = onClick,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 30.dp)
                    .size(25.dp),
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = null,
            )
            Column {
                Text(
                    modifier = Modifier.padding(25.dp, 0.dp),
                    text = title,
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = MaterialTheme.colors.onSecondary,
                        textAlign = TextAlign.Center,
                    ),
                )
                Text(
                    modifier = Modifier.padding(25.dp, 0.dp),
                    text = description,
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = MaterialTheme.colors.primaryVariant,
                        textAlign = TextAlign.Center,
                    ),
                )
            }
        }
    }
}