package com.dhaval.bookland

import Book
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.dhaval.bookland.ui.theme.BooklandTheme
import com.dhaval.bookland.viewmodels.BookViewModel
import com.skydoves.landscapist.coil.CoilImage
import java.util.*


class SearchActivity : ComponentActivity() {
    private val bookViewModel by lazy {
        ViewModelProvider(this@SearchActivity).get(BookViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BooklandTheme {
                Surface(color = MaterialTheme.colors.background) {
                    SearchScreen()
                    window.decorView.showKeyboard()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.zoom_in, R.anim.slide_out)
    }

    @Composable
    fun SearchScreen() {
        Scaffold(
            topBar = { SearchTopBar() },
        ) {
            BookList(bookViewModel)
        }
    }

    @Composable
    fun BookList(bookViewModel: BookViewModel) {
        val bookList by bookViewModel.bookQuery.observeAsState()

        if (bookList != null) {
            BookListItem(book = bookList)
        }
    }

    @Composable
    fun SearchTopBar() {
        var textState by remember { mutableStateOf("") }
        val focusRequester = FocusRequester()
        val focusManager = LocalFocusManager.current

        TopAppBar(
            backgroundColor = MaterialTheme.colors.background,
        ) {
            IconButton(onClick = {
                super.onBackPressed()
                overridePendingTransition(R.anim.zoom_in, R.anim.slide_out)
                window.decorView.hideKeyboard()
            }) {
                Icon(Icons.Filled.ArrowBack, null, tint = MaterialTheme.colors.secondary)
            }
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = textState,
                onValueChange = {
                    textState = it
                },
                placeholder = {
                    Text(
                        text = "Start typing...",
                        color = MaterialTheme.colors.secondaryVariant,
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    bookViewModel.addQuery(textState)
                    window.decorView.hideKeyboard()
                    focusManager.clearFocus()
                }),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background,
                    textColor = MaterialTheme.colors.onSecondary,
                ),
            )
            DisposableEffect(Unit) {
                focusRequester.requestFocus()
                onDispose { }
            }
        }
    }

    @Composable
    fun BookListItem(book: Book?) {
        val context = LocalContext.current
        LazyColumn {
            itemsIndexed(book!!.items){ index, item ->
                Row(
                    modifier = Modifier
                        .clickable {
                            context.startActivity(BookDetailsActivity.newIntent(context, item))
                            overridePendingTransition(R.anim.slide_in, R.anim.zoom_out)
                        }
                        .fillMaxWidth()
                        .height(200.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val imageLinks = item.volumeInfo.imageLinks
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
                    Column {
                        Text(
                            item.volumeInfo.title,
                            color = MaterialTheme.colors.onSecondary,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                        item.volumeInfo.authors?.get(0)?.let {
                            Text(
                                it,
                                color = MaterialTheme.colors.secondaryVariant,
                            )
                        }
                    }
                }
                Divider(
                    color = MaterialTheme.colors.secondaryVariant,
                )
            }
        }
    }

    private fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}