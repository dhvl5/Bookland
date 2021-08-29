package com.dhaval.bookland

import Book
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.airbnb.lottie.compose.*
import com.dhaval.bookland.ui.theme.BooklandTheme
import com.dhaval.bookland.utils.Resource
import com.dhaval.bookland.viewmodels.BookViewModel
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*


class SearchActivity : ComponentActivity() {
    private val bookViewModel by lazy {
        ViewModelProvider(this@SearchActivity).get(BookViewModel::class.java)
    }

    private var isLoading = mutableStateOf(false)

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

        // To Fix: Progress indicator only works first time
        if(isLoading.value) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
            )
        }

        when(bookList) {
            is Resource.Success -> {
                BookList(bookList?.data)
                isLoading.value = false
            }
            is Resource.Error -> {
                if(!isNetworkAvailable(applicationContext)) {
                    LottieInstance(
                        rawRes = R.raw.cloud,
                        text = "No internet connection!",
                    )
                }

                isLoading.value = false
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
                Icon(Icons.Filled.ArrowBack, null, tint = MaterialTheme.colors.onPrimary)
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
                        text = "Search books",
                        color = MaterialTheme.colors.primaryVariant,
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    bookViewModel.addQuery(textState)
                    isLoading.value = true
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
    fun BookList(book: Book?) {
        val context = LocalContext.current

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 25.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp),
        ) {
                itemsIndexed(book!!.items){ index, item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val imageLinks = item.volumeInfo.imageLinks

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(0.dp, 150.dp)
                                .clickable {
                                    context.startActivity(
                                        BookDetailsActivity.newIntent(
                                            context,
                                            item
                                        )
                                    )
                                    overridePendingTransition(R.anim.slide_in, R.anim.zoom_out)
                                },
                            elevation = 2.dp,
                            backgroundColor = MaterialTheme.colors.surface,
                            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                        ) {
                            Row {
                                if(imageLinks != null) {
                                    val url: StringBuilder = StringBuilder(imageLinks.thumbnail)
                                    url.insert(4, "s")

                                    CoilImage(
                                        modifier = Modifier
                                            .size(100.dp, 200.dp),
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
                                            .size(100.dp, 200.dp),
                                        imageModel = R.drawable.image_not_available,
                                        contentScale = ContentScale.Fit,
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                        .align(Alignment.CenterVertically)
                                ) {
                                    Text(
                                        text = item.volumeInfo.title,
                                        color = MaterialTheme.colors.onSecondary,
                                        style = TextStyle(
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                        ),
                                    )
                                    item.volumeInfo.authors?.get(0)?.let {
                                        Text(
                                            text = it,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colors.primaryVariant,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun LottieInstance(rawRes: Int, text: String) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
        ) {
            Column {
                LottieAnimation(
                    modifier = Modifier
                        .size(250.dp, 250.dp)
                        .align(Alignment.CenterHorizontally),
                    composition = composition,
                    iterations = Int.MAX_VALUE,
                    contentScale = ContentScale.Fit,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = text,
                    style = TextStyle(
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                    ),
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

    fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val nw      = connectivityManager.activeNetwork ?: return false
    val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        //for other device how are able to connect with Ethernet
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        //for check internet over Bluetooth
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
        else -> false
    }
}
