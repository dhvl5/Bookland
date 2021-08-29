package com.dhaval.bookland

import Items
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Call
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.dhaval.bookland.ui.theme.BooklandTheme
import com.dhaval.bookland.ui.theme.Typography
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun ToReadScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ){
            items(5){index -> Text(text = "Item: $index", color = MaterialTheme.colors.onSecondary)}
        }
    }
}

@Composable
fun ReadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Reading View",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}

@Composable
fun FinishedScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Finished View",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun BookDetailsScreen(item: Items?) {
    var descriptionExpandedState by remember { mutableStateOf(false) }
    val descriptionRotationState by animateFloatAsState(
        targetValue = if (descriptionExpandedState) 180f else 0f
    )
    var detailsExpandedState by remember { mutableStateOf(false) }
    val detailsRotationState by animateFloatAsState(
        targetValue = if (detailsExpandedState) 180f else 0f
    )

    Column(
        modifier = Modifier
            .padding(25.dp, 15.dp)
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val imageLinks = item?.volumeInfo?.imageLinks

            if (imageLinks != null) {
                val url: StringBuilder = StringBuilder(imageLinks.thumbnail)
                url.insert(4, "s")

                CoilImage(
                    modifier = Modifier
                        .size(133.dp, 200.dp)
                        .padding(5.dp, 0.dp),
                    loading = {
                        CircularProgressIndicator(
                            color = MaterialTheme.colors.onSecondary,
                        )
                    },
                    imageModel = url.toString(),
                    contentScale = ContentScale.Fit,
                )
            } else {
                CoilImage(
                    modifier = Modifier
                        .size(133.dp, 200.dp)
                        .padding(5.dp, 0.dp),
                    imageModel = R.drawable.image_not_available,
                    contentScale = ContentScale.Fit,
                )
            }

            item?.volumeInfo?.title?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colors.onSecondary,
                    style = TextStyle(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    ),
                )
            }
            item?.volumeInfo?.authors?.get(0)?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.primaryVariant,
                )
            }
        }

        Column(
            modifier = Modifier.padding(0.dp, 15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                backgroundColor = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    descriptionExpandedState = !descriptionExpandedState
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(6f)
                                .padding(10.dp, 0.dp, 0.dp, 0.dp),
                            text = "Description",
                            color = MaterialTheme.colors.secondary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W500,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        IconButton(
                            modifier = Modifier
                                .weight(1f)
                                .alpha(ContentAlpha.medium)
                                .rotate(descriptionRotationState),
                            onClick = {
                                descriptionExpandedState = !descriptionExpandedState
                            }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Drop-Down Arrow",
                                tint = MaterialTheme.colors.secondary,
                            )
                        }
                    }

                    if (descriptionExpandedState) {
                        if (item?.volumeInfo?.description != null) {
                            Text(
                                modifier = Modifier.padding(10.dp),
                                text = item.volumeInfo.description,
                                color = MaterialTheme.colors.onSecondary,
                                overflow = TextOverflow.Ellipsis,
                            )
                        } else {
                            Text(
                                modifier = Modifier.padding(10.dp),
                                text = "Description not found :(",
                                color = MaterialTheme.colors.onSecondary,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                backgroundColor = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    detailsExpandedState = !detailsExpandedState
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(6f)
                                .padding(10.dp, 0.dp, 0.dp, 0.dp),
                            text = "Details",
                            color = MaterialTheme.colors.secondary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W500,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        IconButton(
                            modifier = Modifier
                                .weight(1f)
                                .alpha(ContentAlpha.medium)
                                .rotate(detailsRotationState),
                            onClick = {
                                detailsExpandedState = !detailsExpandedState
                            }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Drop-Down Arrow",
                                tint = MaterialTheme.colors.secondary,
                            )
                        }
                    }

                    if (detailsExpandedState) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row {
                                Text(
                                    modifier = Modifier.width(120.dp),
                                    text = "ISBN",
                                    color = MaterialTheme.colors.primaryVariant,
                                )
                                item?.volumeInfo?.industryIdentifiers?.get(0)?.identifier?.let {
                                    Text(text = it, color = MaterialTheme.colors.onSecondary)
                                }
                            }
                            Row {
                                Text(
                                    modifier = Modifier.width(120.dp),
                                    text = "Page Count",
                                    color = MaterialTheme.colors.primaryVariant,
                                )
                                Text(
                                    text = item?.volumeInfo?.pageCount.toString(),
                                    color = MaterialTheme.colors.onSecondary
                                )
                            }
                            Row {
                                Text(
                                    modifier = Modifier.width(120.dp),
                                    text = "Published Date",
                                    color = MaterialTheme.colors.primaryVariant,
                                )
                                item?.volumeInfo?.publishedDate?.let {
                                    Text(text = it, color = MaterialTheme.colors.onSecondary)
                                }
                            }
                            Row {
                                Text(
                                    modifier = Modifier.width(120.dp),
                                    text = "Publisher",
                                    color = MaterialTheme.colors.primaryVariant,
                                )
                                item?.volumeInfo?.publisher?.let {
                                    Text(text = it, color = MaterialTheme.colors.onSecondary)
                                }
                            }
                            Row {
                                Text(
                                    modifier = Modifier.width(120.dp),
                                    text = "Language",
                                    color = MaterialTheme.colors.primaryVariant,
                                )
                                item?.volumeInfo?.language?.let {
                                    Text(text = it, color = MaterialTheme.colors.onSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}