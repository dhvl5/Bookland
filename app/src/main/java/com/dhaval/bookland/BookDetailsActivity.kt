package com.dhaval.bookland

import Items
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhaval.bookland.ui.theme.BooklandTheme

class BookDetailsActivity : ComponentActivity() {
    private val item: Items? by lazy {
        intent?.getSerializableExtra(BOOK_ID) as Items?
    }

    companion object {
        private const val BOOK_ID = "book_id"
        fun newIntent(context: Context, item: Items?) =
            Intent(context, BookDetailsActivity::class.java).apply {
                putExtra(BOOK_ID, item)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BooklandTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Column {
                        IconButton(
                            modifier = Modifier.padding(5.dp, 5.dp),
                            onClick = {
                                super.onBackPressed()
                                overridePendingTransition(R.anim.zoom_in, R.anim.slide_out)
                            },
                        ) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colors.secondary,
                            )
                        }

                        BookDetailsScreen(item = item)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.zoom_in, R.anim.slide_out)
    }
}