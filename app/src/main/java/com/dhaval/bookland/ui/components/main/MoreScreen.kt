package com.dhaval.bookland.ui.components.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhaval.bookland.R

val items = listOf("Theme", "General", "Changelog", "About")

@Composable
fun MoreScreen(application: BooklandApplication) {
    val openDialog = remember { mutableStateOf(false) }
    val radioOptions = listOf("Light", "Dark", "Auto")

    val value = when(application.prefs.getInt("mode", 2)) {
        0 -> "Light"
        1 -> "Dark"
        else -> "Auto"
    }

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(value) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = { openDialog.value = true },
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 30.dp)
                    .size(25.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_paint),
                contentDescription = null,
            )
            Column {
                Text(
                    modifier = Modifier.padding(25.dp, 0.dp),
                    text = "Theme",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = MaterialTheme.colors.onSecondary,
                        textAlign = TextAlign.Center,
                    ),
                )
                Text(
                    modifier = Modifier.padding(25.dp, 0.dp),
                    text = "Main theme of the app",
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = MaterialTheme.colors.primaryVariant,
                        textAlign = TextAlign.Center,
                    ),
                )
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
                                application.editor.putInt("mode", ThemeMode.LIGHT.ordinal)
                                application.editor.commit()
                            }
                            "Dark" -> {
                                application.themeMode.value = ThemeMode.DARK
                                application.editor.putInt("mode", ThemeMode.DARK.ordinal)
                                application.editor.commit()
                            }
                            "Auto" -> {
                                application.themeMode.value = ThemeMode.AUTO
                                application.editor.putInt("mode", ThemeMode.AUTO.ordinal)
                                application.editor.commit()
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
}