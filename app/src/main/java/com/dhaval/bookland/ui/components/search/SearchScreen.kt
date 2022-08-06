package com.dhaval.bookland.ui.components.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
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

var textStateDuplicate: String = ""

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
    when(bookViewModel.errorType.value) {
        Internet.ErrorType.NONE -> {
            GetBooks(navController, bookViewModel)
        }
        Internet.ErrorType.INTERNET -> {
            ErrorAlert(
                drawableRes = R.drawable.ic_error,
                text = "No internet connection",
            )
        }
        Internet.ErrorType.EXCEPTION -> {
            ErrorAlert(
                drawableRes = R.drawable.ic_error,
                text = bookViewModel.errorType.value.errorMsg,
            )
        }
        Internet.ErrorType.CUSTOM -> {
            ErrorAlert(
                drawableRes = R.drawable.ic_reading,
                text = "No books found",
            )
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
            bookViewModel.clearLoadItemsList()
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
                    bookViewModel.clearLoadItemsList()

                    bookViewModel.loadItems(textState, 0)

                    focusManager.clearFocus()
                    textStateDuplicate = textState
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
fun GetBooks(navController: NavHostController, bookViewModel: BookViewModel) {
    val scrollState = rememberLazyListState()
    fun LazyListState.isScrolledToEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 25.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp),
            state = scrollState,
        ) {
            itemsIndexed(bookViewModel.items.toList()) { index, item ->
                val endOfListReached by remember {
                    derivedStateOf {
                        scrollState.isScrolledToEnd()
                    }
                }

                LaunchedEffect(endOfListReached) {
                    if (index >= bookViewModel.items.size - 1 && !bookViewModel.isNextItemsLoading.value) {
                        bookViewModel.loadNextItems(textStateDuplicate, index)
                    }
                }

                BookItemCard(
                    item = item,
                    onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("item", item)
                        navController.navigate(Screen.Details.route)
                    }
                )
            }
            item {
                if (bookViewModel.isNextItemsLoading.value) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}