package com.dhaval.bookland

import Items
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BooklandTheme {

                val scrollState = rememberScrollState()
                val openDialog = remember { mutableStateOf(false) }

                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(
                                modifier = Modifier.size(60.dp),
                                onClick = {
                                    openDialog.value = true
                                },
                                backgroundColor = MaterialTheme.colors.secondary,
                        ) {
                            Icon(painterResource(id = R.drawable.ic_save), "", tint = MaterialTheme.colors.onSecondary)
                        }
                    },
                ) {
                    Surface(
                            modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(scrollState),
                            color = MaterialTheme.colors.background,
                    ) {

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

                if (openDialog.value) {
                    AlertDialog(
                        onDismissRequest = {
                            openDialog.value = false
                        },
                        title = {
                            Text(
                                    text = "Save",
                                    color = MaterialTheme.colors.onSecondary,
                            )
                        },
                        text = {
                            Text(
                                    text = "Select where you want to save this book.",
                                    color = MaterialTheme.colors.onSecondary,
                            )
                        },
                        confirmButton = {
                            TextButton(
                                    onClick = {
                                        openDialog.value = false
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
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.zoom_in, R.anim.slide_out)
    }
}