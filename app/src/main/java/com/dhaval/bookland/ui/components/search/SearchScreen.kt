package com.dhaval.bookland.ui.components.search

import com.dhaval.bookland.models.Book
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dhaval.bookland.R
import com.dhaval.bookland.utils.ErrorAlert
import com.dhaval.bookland.utils.Internet
import com.dhaval.bookland.utils.Screen
import com.dhaval.bookland.viewmodels.BookViewModel
import com.skydoves.sandwich.*

@Composable
fun SearchScreen(navController: NavHostController, bookViewModel: BookViewModel) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Scaffold(
            topBar = { SearchTopBar(navController, bookViewModel) },
        ) {
            BookList(navController, bookViewModel)

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

            if(bookViewModel.emptySearchedResult) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(vertical = 10.dp),
                        text = "Powered by Google",
                        style = TextStyle(
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
fun BookList(navController: NavHostController, bookViewModel: BookViewModel) {
    val bookList by bookViewModel.bookQuery.observeAsState()

    bookList?.onSuccess {
        bookList?.toLiveData {
            if(data?.totalItems != 0) {
                if(bookViewModel.emptySearchedResult)
                    data?.items = emptyList()

                GetBooks(navController, data)
            } else {
                ErrorAlert(
                    drawableRes = R.drawable.ic_reading,
                    text = "No books found",
                )
            }
        }
    }?.onError {

    }?.onException {
        if(!Internet.isAvailable()) {
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
fun SearchTopBar(navController: NavHostController, bookViewModel: BookViewModel) {
    var textState by remember { mutableStateOf("") }
    val focusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current

    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
    ) {
        IconButton(onClick = {
            navController.popBackStack()
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
                    bookViewModel.emptySearchedResult = false
                    bookViewModel.addQuery(textState)
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
fun GetBooks(navController: NavHostController, book: Book?) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 25.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        itemsIndexed(book!!.items) { _, item ->
            BookItemCard(
                item = item,
                onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("item", item)
                    navController.navigate(Screen.Details.route)
                }
            )
        }
    }
}