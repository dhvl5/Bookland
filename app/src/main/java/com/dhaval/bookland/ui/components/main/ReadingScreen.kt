package com.dhaval.bookland.ui.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhaval.bookland.models.Items
import com.dhaval.bookland.ui.components.details.BookDetailsActivity
import com.dhaval.bookland.ui.components.search.BookItemCard

@Composable
fun ReadingScreen(items: List<Items>) {
    val context = LocalContext.current

    if(items.isNotEmpty()) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 25.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp),
        ) {
            itemsIndexed(items) { _, item ->
                BookItemCard(
                    item = item,
                    onClick = {
                        context.startActivity(
                            BookDetailsActivity.newIntent(
                                context,
                                item,
                            )
                        )
                    }
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .wrapContentSize(Alignment.Center)
        ) {
            Text(
                text = "Click + to add books",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSecondary,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )
        }
    }
}