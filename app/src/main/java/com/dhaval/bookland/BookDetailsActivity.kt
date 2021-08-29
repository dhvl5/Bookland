package com.dhaval.bookland

import Items
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
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

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BooklandTheme {

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
                                            onClick = null, // null recommended for accessibility with screenreaders
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
                                        "To Read" -> Toast.makeText(applicationContext, "To Read", Toast.LENGTH_SHORT).show()
                                        "Reading" -> Toast.makeText(applicationContext, "Reading", Toast.LENGTH_SHORT).show()
                                        "Finished" -> Toast.makeText(applicationContext, "Finished", Toast.LENGTH_SHORT).show()
                                    }
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

    @Composable
    fun TopBar() {
        val appBarHorizontalPadding = 4.dp
        val titleIconModifier = Modifier
            .fillMaxHeight()
            .width(72.dp - appBarHorizontalPadding)

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
                                overridePendingTransition(R.anim.zoom_in, R.anim.slide_out)
                            },
                            enabled = true,
                        ) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colors.onPrimary,
                            )
                        }
                    }
                }
                Row(
                    Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
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
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.zoom_in, R.anim.slide_out)
    }
}