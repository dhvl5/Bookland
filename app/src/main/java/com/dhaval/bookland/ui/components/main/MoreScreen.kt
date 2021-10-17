package com.dhaval.bookland.ui.components.main

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhaval.bookland.R
import com.dhaval.bookland.utils.MoreItem
import com.dhaval.bookland.utils.PrefsHelper
import com.dhaval.bookland.viewmodels.BookViewModel

val items = listOf("Theme", "General", "Changelog", "About")
private var openClearDatabaseDialog by mutableStateOf(false)

@Composable
fun MoreScreen(context: Context, application: BooklandApplication, bookViewModel: BookViewModel) {
    val openDialog = remember { mutableStateOf(false) }
    val radioOptions = listOf("Light", "Dark", "Auto")

    val value = when(PrefsHelper.readInt(PrefsHelper.THEME_MODE, 2)) {
        0 -> "Light"
        1 -> "Dark"
        else -> "Auto"
    }

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(value) }

    Column {
        Text(
            modifier = Modifier.padding(
                start = 30.dp,
                top = 10.dp,
                end = 0.dp,
                bottom = 10.dp,
            ),
            text = "Appearance",
            style = TextStyle(
                fontSize = 15.sp,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
        )

        MoreItem(
            icon = R.drawable.ic_paint,
            title = "Theme",
            description = "Main theme of the app",
            onClick = {
                openDialog.value = true
            },
        )

        Text(
            modifier = Modifier.padding(
                start = 30.dp,
                top = 10.dp,
                end = 0.dp,
                bottom = 10.dp,
            ),
            text = "Data",
            style = TextStyle(
                fontSize = 15.sp,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
        )

        MoreItem(
            icon = R.drawable.ic_export,
            title = "Export",
            description = "Export database as a file",
            onClick = {

            },
        )

        MoreItem(
            icon = R.drawable.ic_import,
            title = "Import",
            description = "Import database from a file",
            onClick = {

            },
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        onClick = { openClearDatabaseDialog = true },
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .size(25.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                    contentDescription = null,
                    tint = Color(206,32,41, 255),
                )
                Column {
                    Text(
                        modifier = Modifier.padding(25.dp, 0.dp),
                        text = "Clear Database",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color(206,32,41, 255),
                            textAlign = TextAlign.Center,
                        ),
                    )
                    Text(
                        modifier = Modifier.padding(25.dp, 0.dp),
                        text = "Delete all your tracked records",
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = Color(206,32,41, 255),
                            textAlign = TextAlign.Center,
                        ),
                    )
                }
            }
        }

        Text(
            modifier = Modifier.padding(
                start = 30.dp,
                top = 10.dp,
                end = 0.dp,
                bottom = 10.dp,
            ),
            text = "Information",
            style = TextStyle(
                fontSize = 15.sp,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
        )

        MoreItem(
            icon = R.drawable.ic_info,
            title = "About",
            description = "A bit about this app and developer",
            onClick = {

            },
        )

        MoreItem(
            icon = R.drawable.ic_article,
            title = "Libraries",
            description = "Open source licenses",
            onClick = {

            },
        )
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(
                    text = "Theme",
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
                            "Light" -> {
                                application.themeMode.value = ThemeMode.LIGHT
                                PrefsHelper.writeInt(PrefsHelper.THEME_MODE, ThemeMode.LIGHT.ordinal)
                            }
                            "Dark" -> {
                                application.themeMode.value = ThemeMode.DARK
                                PrefsHelper.writeInt(PrefsHelper.THEME_MODE, ThemeMode.DARK.ordinal)
                            }
                            "Auto" -> {
                                application.themeMode.value = ThemeMode.AUTO
                                PrefsHelper.writeInt(PrefsHelper.THEME_MODE, ThemeMode.AUTO.ordinal)
                            }
                        }
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("No")
                }
            }
        )
    }

    if (openClearDatabaseDialog) {
        AlertDialog(
            onDismissRequest = {
                openClearDatabaseDialog = false
            },
            title = {
                Text(
                    text = "Clear database",
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
                        modifier = Modifier.padding(vertical = 10.dp),
                        text = "Warning: This operation is irreversible",
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = Color.Yellow,
                        ),
                    )
                    Text(
                        text = "Are you sure you want to clear database? It will delete all the books and your progress.",
                        style = TextStyle(
                            color = MaterialTheme.colors.onSecondary,
                        ),
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        bookViewModel.clearDatabase()
                        Toast.makeText(context, "Database cleared", Toast.LENGTH_SHORT).show()
                        openClearDatabaseDialog = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openClearDatabaseDialog = false
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
}