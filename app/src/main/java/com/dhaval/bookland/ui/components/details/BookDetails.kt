package com.dhaval.bookland.ui.components.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import com.dhaval.bookland.models.Items
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhaval.bookland.R
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun BookDetails(context: Context, item: Items?) {
    var expanded: Boolean by remember { mutableStateOf(false) }

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
                        .size(133.dp, 200.dp)
                        .padding(5.dp, 0.dp),
                ) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(100.dp, 200.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.image_not_available),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSecondary),
                    )
                }
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

        Divider(
            modifier = Modifier.padding(top = 15.dp),
            color = MaterialTheme.colors.primaryVariant,
        )

        Column(
            modifier = Modifier.padding(0.dp, 15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            Column {
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = "Description",
                    style = TextStyle(
                        color = MaterialTheme.colors.secondary,
                        fontSize = 20.sp,
                    )
                )

                if (item?.volumeInfo?.description != null) {
                    if(!expanded) {
                        Text(
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable { expanded = !expanded },
                            text = item.volumeInfo.description,
                            color = MaterialTheme.colors.onSecondary,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 3,
                        )
                    } else {
                        Text(
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable { expanded = !expanded },
                            text = item.volumeInfo.description,
                            color = MaterialTheme.colors.onSecondary,
                        )
                    }
                } else {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "Not available",
                        color = MaterialTheme.colors.onSecondary,
                    )
                }
            }

            Divider(
                color = MaterialTheme.colors.primaryVariant,
            )

            Column(
                modifier = Modifier.padding(horizontal = 10.dp),
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = "Details",
                    style = TextStyle(
                        color = MaterialTheme.colors.secondary,
                        fontSize = 20.sp,
                    )
                )

                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "ISBN",
                        color = MaterialTheme.colors.primaryVariant,
                    )
                    if(item?.volumeInfo?.industryIdentifiers?.get(0)?.identifier != null) {
                        Text(
                            text = item.volumeInfo.industryIdentifiers[0].identifier,
                            color = MaterialTheme.colors.onSecondary,
                        )
                    } else {
                        Text(
                            text = "-",
                            color = MaterialTheme.colors.onSecondary,
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Page Count",
                        color = MaterialTheme.colors.primaryVariant,
                    )
                    if(item?.volumeInfo?.pageCount != null) {
                        Text(
                            text = item.volumeInfo.pageCount.toString(),
                            color = MaterialTheme.colors.onSecondary,
                        )
                    } else {
                        Text(
                            text = "-",
                            color = MaterialTheme.colors.onSecondary,
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Published Date",
                        color = MaterialTheme.colors.primaryVariant,
                    )
                    if(item?.volumeInfo?.publishedDate != null) {
                        Text(
                            text = item.volumeInfo.publishedDate,
                            color = MaterialTheme.colors.onSecondary,
                        )
                    } else {
                        Text(
                            text = "-",
                            color = MaterialTheme.colors.onSecondary,
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Publisher",
                        color = MaterialTheme.colors.primaryVariant,
                    )
                    if(item?.volumeInfo?.publisher != null) {
                        Text(
                            text = item.volumeInfo.publisher,
                            color = MaterialTheme.colors.onSecondary,
                        )
                    } else {
                        Text(
                            text = "-",
                            color = MaterialTheme.colors.onSecondary,
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Language",
                        color = MaterialTheme.colors.primaryVariant,
                    )
                    if(item?.volumeInfo?.language != null) {
                        Text(
                            text = item.volumeInfo.language,
                            color = MaterialTheme.colors.onSecondary,
                        )
                    } else {
                        Text(
                            text = "-",
                            color = MaterialTheme.colors.onSecondary,
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Categories",
                        color = MaterialTheme.colors.primaryVariant,
                    )
                    if(item?.volumeInfo?.categories != null) {
                        item.volumeInfo.categories.let { it ->
                            val value = it.joinToString(
                                separator = ", ",
                            ) {
                                it
                            }
                            Text(text = value, color = MaterialTheme.colors.onSecondary)
                        }
                    } else {
                        Text(
                            text = "-",
                            color = MaterialTheme.colors.onSecondary,
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Link",
                        color = MaterialTheme.colors.primaryVariant,
                    )
                    if(item?.volumeInfo?.infoLink != null) {
                        Text(
                            modifier = Modifier.clickable {
                                val customTabsIntent = CustomTabsIntent.Builder()
                                    .setDefaultColorSchemeParams(
                                        CustomTabColorSchemeParams.Builder()
                                            .setToolbarColor(Color(0xFF252525).hashCode())
                                            .build()
                                    )
                                    .build()
                                customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                                customTabsIntent.launchUrl(context, Uri.parse(item.volumeInfo.infoLink))
                            },
                            text = "Google Books",
                            color = Color(3, 155, 229, 255),
                            style = TextStyle(
                                fontSize = 16.sp,
                                textDecoration = TextDecoration.Underline,
                            ),
                        )
                    } else {
                        Text(
                            text = "-",
                            color = MaterialTheme.colors.onSecondary,
                        )
                    }
                }
            }
        }
    }
}