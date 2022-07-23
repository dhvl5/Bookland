package com.dhaval.bookland.ui.components.search

import androidx.compose.animation.core.animateFloatAsState
import com.dhaval.bookland.models.Items
import com.dhaval.bookland.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun BookItemCard(item: Items?, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val imageLinks = item?.volumeInfo?.imageLinks
        val selected = remember { mutableStateOf(false) }
        val interactionSource = remember { MutableInteractionSource() }
        val pressedState by interactionSource.collectIsPressedAsState()
        val scale = animateFloatAsState(if (selected.value && pressedState) .95f else 1f)

        if (pressedState) {
            selected.value = true

            DisposableEffect(Unit) {
                onDispose {
                    selected.value = false
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .size(0.dp, 150.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = true,
                    onClick = { onClick() }
                )
                .scale(scale.value),
            elevation = 2.dp,
            backgroundColor = MaterialTheme.colors.surface,
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        ) {
            Row {
                if (imageLinks != null) {
                    val url: StringBuilder = StringBuilder(imageLinks.thumbnail)
                    url.insert(4, "s")

                    CoilImage(
                        modifier = Modifier
                            .size(100.dp, 200.dp),
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center),
                                    color = MaterialTheme.colors.onSecondary,
                                )
                            }
                        },
                        imageModel = url.toString(),
                        contentScale = ContentScale.Fit,
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(100.dp, 200.dp),
                    ) {
                        Image(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(70.dp, 200.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.image_not_available),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.onSecondary),
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                ) {
                    item?.volumeInfo?.title?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colors.onSecondary,
                            style = TextStyle(
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                    }
                    item?.volumeInfo?.authors?.let { it ->
                        val value = it.joinToString(
                            separator = ", ",
                        ) {
                            it
                        }
                        Text(
                            text = value,
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.primaryVariant,
                        )
                    }
                }
            }
        }
    }
}