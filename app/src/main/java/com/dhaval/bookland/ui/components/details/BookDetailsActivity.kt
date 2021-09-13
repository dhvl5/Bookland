package com.dhaval.bookland.ui.components.details

import com.dhaval.bookland.models.Items
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.dhaval.bookland.R
import com.dhaval.bookland.models.Status
import com.dhaval.bookland.ui.components.main.BooklandApplication
import com.dhaval.bookland.ui.components.main.ThemeMode
import com.dhaval.bookland.ui.theme.BooklandTheme
import com.dhaval.bookland.viewmodels.BookViewModel
import com.dhaval.bookland.viewmodels.BookViewModelFactory

class BookDetailsActivity : ComponentActivity() {
    private val item: Items? by lazy {
        intent?.getSerializableExtra(BOOK_ID) as Items?
    }

    private lateinit var bookViewModel: BookViewModel
    private lateinit var app: BooklandApplication

    private var openRemoveDialog by mutableStateOf(false)

    companion object {
        private const val BOOK_ID = "book_id"
        fun newIntent(context: Context, item: Items?) =
            Intent(context, BookDetailsActivity::class.java).apply {
                putExtra(BOOK_ID, item)
            }
    }

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
                val scrollState = rememberScrollState()
                val openDialog = remember { mutableStateOf(false) }
                val radioOptions = listOf("To Read", "Reading", "Finished")
                val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

                Scaffold(
                    topBar = { TopBar() },
                    floatingActionButton = {
                        FloatingActionButton(
                                modifier = Modifier.size(60.dp),
                                onClick = {
                                    openDialog.value = true
                                },
                                backgroundColor = MaterialTheme.colors.secondary,
                        ) {
                            Icon(painterResource(id = R.drawable.ic_save), "", tint = MaterialTheme.colors.secondaryVariant)
                        }
                    },
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState),
                        color = MaterialTheme.colors.background,
                    ) {
                        BookDetailsScreen(item = item)
                    }
                }

                if (openDialog.value) {
                    AlertDialog(
                        onDismissRequest = {
                            openDialog.value = false
                        },
                        title = {
                            Text(
                                text = "Save To",
                                color = MaterialTheme.colors.onSecondary,
                            )
                        },
                        text = {
                            Column(
                                modifier = Modifier.selectableGroup(),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                radioOptions.forEach { text ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(46.dp)
                                            .selectable(
                                                selected = (text == selectedOption),
                                                onClick = { onOptionSelected(text) },
                                                role = Role.RadioButton,
                                            ),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        RadioButton(
                                            selected = (text == selectedOption),
                                            onClick = null,
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = MaterialTheme.colors.primary,
                                                unselectedColor = MaterialTheme.colors.primaryVariant,
                                                disabledColor = Color.Black,
                                            ),
                                        )
                                        Text(
                                            text = text,
                                            style = MaterialTheme.typography.body1.merge(),
                                            modifier = Modifier.padding(start = 16.dp),
                                            color = MaterialTheme.colors.onSecondary,
                                        )
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    openDialog.value = false

                                    when(selectedOption) {
                                        "To Read" -> item?.status = Status.TO_READ
                                        "Reading" -> item?.status = Status.READING
                                        "Finished" -> item?.status = Status.FINISHED
                                    }

                                    item?.let { bookViewModel.insertItem(it) }
                                    Toast.makeText(applicationContext, "Book Saved", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text("Save")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    openDialog.value = false
                                }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }

                if (openRemoveDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            openRemoveDialog = false
                        },
                        title = {
                            Text(
                                text = "Remove",
                                color = MaterialTheme.colors.onSecondary,
                            )
                        },
                        text = {
                            Column(
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    text = "Are you sure you want to remove this book?",
                                    style = TextStyle(
                                        color = MaterialTheme.colors.onSecondary,
                                    ),
                                )
                            }
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    item?.id?.let { bookViewModel.deleteItem(it) }
                                    super.onBackPressed()
                                }
                            ) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    openRemoveDialog = false
                                }
                            ) {
                                Text("No")
                            }
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun TopBar() {
        val appBarHorizontalPadding = 4.dp
        val titleIconModifier = Modifier
            .fillMaxHeight()
            .width(72.dp - appBarHorizontalPadding)
        val openMenu = remember { mutableStateOf(false) }

        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.background,
            elevation = 0.dp,
        ) {
            Box(Modifier.height(32.dp)) {
                Row(titleIconModifier, verticalAlignment = Alignment.CenterVertically) {
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.high,
                    ) {
                        IconButton(
                            onClick = {
                                super.onBackPressed()
                            },
                            enabled = true,
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colors.onPrimary,
                            )
                        }
                    }
                }
                Row(
                    Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ProvideTextStyle(value = MaterialTheme.typography.h6) {
                        CompositionLocalProvider(
                            LocalContentAlpha provides ContentAlpha.high,
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 1,
                                text = "Details",
                                color = MaterialTheme.colors.onPrimary,
                                style = TextStyle(
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp,
                                ),
                            )
                        }
                    }
                }

                if(item?.status != null) {
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = {
                            openMenu.value = true
                        },
                        enabled = true,
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onPrimary,
                        )
                        DropdownMenu(
                            expanded = openMenu.value,
                            onDismissRequest = {
                                openMenu.value = false
                            },
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    openMenu.value = false
                                    openRemoveDialog = true
                                },
                            ) {
                                Text(
                                    text = "Remove",
                                    style = TextStyle(
                                        color = MaterialTheme.colors.onSecondary,
                                        textAlign = TextAlign.Center,
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}