package com.dhaval.bookland

import Items
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun BookDetailsScreen(item: Items?) {
    Column(
        modifier = Modifier
            .padding(15.dp, 0.dp)
            .verticalScroll(rememberScrollState())
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
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
            item?.volumeInfo?.authors?.get(0)?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colors.secondaryVariant,
                )
            }
        }

        Divider(
            modifier = Modifier.padding(0.dp, 20.dp),
            color = MaterialTheme.colors.secondaryVariant,
        )

        Column {
            Text(
                "Description",
                color = MaterialTheme.colors.secondary,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W500,
                ),
            )
            if(item?.volumeInfo?.description != null) {
                Text(
                    modifier = Modifier.padding(0.dp, 5.dp),
                    text = item.volumeInfo.description,
                    color = MaterialTheme.colors.secondaryVariant,
                )
            } else {
                Text(
                    modifier = Modifier.padding(0.dp, 5.dp),
                    text = "Description not found :(",
                    color = MaterialTheme.colors.secondaryVariant,
                )
            }
        }

        Divider(
            modifier = Modifier.padding(0.dp, 20.dp),
            color = MaterialTheme.colors.secondaryVariant,
        )

        Column {
            Text(
                "Details",
                color = MaterialTheme.colors.secondary,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W500,
                ),
            )

            Column {
                Row {
                    Text(modifier = Modifier.width(120.dp), text = "ISBN", color = Color.Gray)
                    item?.volumeInfo?.industryIdentifiers?.get(0)?.identifier?.let {
                        Text(text = it, color = MaterialTheme.colors.onSecondary)
                    }
                }
                Row {
                    Text(modifier = Modifier.width(120.dp), text = "Page Count", color = Color.Gray)
                    Text(text = item?.volumeInfo?.pageCount.toString(), color = MaterialTheme.colors.onSecondary)
                }
                Row {
                    Text(modifier = Modifier.width(120.dp), text = "Published Date", color = Color.Gray)
                    item?.volumeInfo?.publishedDate?.let {
                        Text(text = it, color = MaterialTheme.colors.onSecondary)
                    }
                }
                Row {
                    Text(modifier = Modifier.width(120.dp), text = "Publisher", color = Color.Gray)
                    item?.volumeInfo?.publisher?.let {
                        Text(text = it, color = MaterialTheme.colors.onSecondary)
                    }
                }
                Row {
                    Text(modifier = Modifier.width(120.dp), text = "Language", color = Color.Gray)
                    item?.volumeInfo?.language?.let {
                        Text(text = it, color = MaterialTheme.colors.onSecondary)
                    }
                }
            }
        }

//        Button(
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//                .padding(20.dp)
//                .size(100.dp),
//            shape = RoundedCornerShape(10.dp),
//            colors = ButtonDefaults.buttonColors(
//                backgroundColor = MaterialTheme.colors.secondary,
//            ),
//            onClick = {
//
//            },
//        ) {
//            Text(
//                text = "Save To",
//                color = Color.White,
//                style = TextStyle(
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                )
//            )
//        }
    }
}