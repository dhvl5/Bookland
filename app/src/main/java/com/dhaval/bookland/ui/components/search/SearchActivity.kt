package com.dhaval.bookland.ui.components.search

import com.dhaval.bookland.models.Book
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.dhaval.bookland.R
import com.dhaval.bookland.ui.components.details.BookDetailsActivity
import com.dhaval.bookland.ui.components.main.BooklandApplication
import com.dhaval.bookland.ui.components.main.ThemeMode
import com.dhaval.bookland.utils.ErrorAlert
import com.dhaval.bookland.ui.theme.BooklandTheme
import com.dhaval.bookland.utils.Internet
import com.dhaval.bookland.utils.hideKeyboard
import com.dhaval.bookland.viewmodels.BookViewModel
import com.dhaval.bookland.viewmodels.BookViewModelFactory
import com.skydoves.sandwich.*


class SearchActivity : ComponentActivity() {
    private lateinit var bookViewModel: BookViewModel
    private lateinit var app: BooklandApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = (application as BooklandApplication)

        val repository = (application as BooklandApplication).bookRepository
        bookViewModel = ViewModelProvider(this, BookViewModelFactory(repository)).get(BookViewModel::class.java)

        setContent {
            var themeMode = when(app.themeMode.value) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                else -> isSystemInDarkTheme()
            }
            if(app.prefs.contains("mode")) {
                val value = app.prefs.getInt("mode", 0)
                themeMode = when(value) {
                    0 -> false
                    1 -> true
                    else -> isSystemInDarkTheme()
                }
            }

            BooklandTheme(themeMode) {
                Surface(color = MaterialTheme.colors.background) {
                    SearchScreen()
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

            if (bookViewModel.isLoading.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                        .wrapContentSize(Alignment.Center),
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    @Composable
    fun BookList(bookViewModel: BookViewModel) {
        val bookList by bookViewModel.bookQuery.observeAsState()

        bookList?.onSuccess {
            bookList?.toLiveData {
                if(data?.totalItems != 0) {
                    GetBooks(data)
                } else {
                    ErrorAlert(
                        drawableRes = R.drawable.ic_reading,
                        text = "No books found",
                    )
                }
            }
        }?.onError {

        }?.onException {
            if(!Internet.check()) {
                ErrorAlert(
                    drawableRes = R.drawable.ic_error,
                    text = "No internet connection",
                )
            } else {
                exception.message?.let {
                    ErrorAlert(
                        drawableRes = R.drawable.ic_error,
                        text = it,
                    )
                }
            }
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
                Icon(Icons.Default.ArrowBack, null, tint = MaterialTheme.colors.onPrimary)
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
                        text = "Search for title, author or ISBN",
                        color = MaterialTheme.colors.primaryVariant,
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    if(textState.trim().isNotEmpty()) {
                        bookViewModel.addQuery(textState)
                        window.decorView.hideKeyboard()
                        focusManager.clearFocus()
                    }
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
    fun GetBooks(book: Book?) {
        val context = LocalContext.current

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 25.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp),
        ) {
            itemsIndexed(book!!.items) { _, item ->
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
    }
}