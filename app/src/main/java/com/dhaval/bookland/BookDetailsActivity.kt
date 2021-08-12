package com.dhaval.bookland

import Items
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.dhaval.bookland.ui.theme.BooklandTheme

class BookDetailsActivity : ComponentActivity() {
    private val item: Items? by lazy {
        intent?.getSerializableExtra(BOOK_ID) as Items?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BooklandTheme {
                Surface(color = MaterialTheme.colors.background) {
                    item?.volumeInfo?.let { Greeting(it.title) }
                }
            }
        }
    }

    companion object {
        private const val BOOK_ID = "book_id"
        fun newIntent(context: Context, item: Items?) =
            Intent(context, BookDetailsActivity::class.java).apply {
                putExtra(BOOK_ID, item)
            }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}