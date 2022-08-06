package com.dhaval.bookland.ui.components.details

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import com.dhaval.bookland.models.Items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.dhaval.bookland.R
import com.dhaval.bookland.models.Status
import com.dhaval.bookland.viewmodels.BookViewModel

private var openRemoveDialog by mutableStateOf(false)

@Composable
fun BookDetailsScreen(context: Context, navController: NavHostController, bookViewModel: BookViewModel, item: Items) {
    val scrollState = rememberScrollState()
    val openDialog = remember { mutableStateOf(false) }
    val radioOptions = listOf("To Read", "Reading", "Finished")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    Scaffold(
        topBar = {
            TopBar(navController, item)
        },
        floatingActionButton = {
            FloatingActionButton(
                    modifier = Modifier.size(60.dp),
                    onClick = {
                        openDialog.value = true
                    },
                    backgroundColor = MaterialTheme.colors.secondary,
            ) {
                if(item.status != null) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.ic_swap),
                        contentDescription = "",
                        tint = MaterialTheme.colors.secondaryVariant,
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_save),
                        contentDescription = "",
                        tint = MaterialTheme.colors.secondaryVariant,
                    )
                }
            }
        },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            color = MaterialTheme.colors.background,
        ) {
            BookDetails(context = context, item = item)
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
                            "To Read" -> item.status = Status.TO_READ
                            "Reading" -> item.status = Status.READING
                            "Finished" -> item.status = Status.FINISHED
                        }

                        item.let { bookViewModel.insertItem(it) }
                        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
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
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = MaterialTheme.colors.onSecondary,
                    ),
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
                        item.id.let { bookViewModel.deleteItem(it, navController) }
                        openRemoveDialog = false
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

    if(bookViewModel.showRemovingDialog) {
        Dialog(
            onDismissRequest = { },
            content = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colors.surface)
                        .size(270.dp, 100.dp),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 30.dp),
                    )
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Removing",
                        style = TextStyle(
                            color = MaterialTheme.colors.onSecondary,
                            fontSize = 15.sp,
                        ),
                    )
                }
            }
        )
    }
}

@Composable
fun TopBar(navController: NavHostController, item: Items?) {
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
                            navController.popBackStack()
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
                                fontSize = 30.sp,
                                fontFamily = FontFamily.Cursive,
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