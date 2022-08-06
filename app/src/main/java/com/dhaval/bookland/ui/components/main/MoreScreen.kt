package com.dhaval.bookland.ui.components.main

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
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
import androidx.navigation.NavHostController
import com.dhaval.bookland.R
import com.dhaval.bookland.utils.Constants
import com.dhaval.bookland.utils.MoreItem
import com.dhaval.bookland.utils.PrefsHelper
import com.dhaval.bookland.utils.Screen
import com.dhaval.bookland.viewmodels.BookViewModel
import org.apache.commons.io.IOUtils
import java.io.*

val items = listOf("Theme", "General", "Changelog", "About")

private var openClearDatabaseDialog by mutableStateOf(false)
private var openImportDialog by mutableStateOf(false)
private var openExportDialog by mutableStateOf(false)

@Composable
fun MoreScreen(context: Context, application: BooklandApplication, bookViewModel: BookViewModel, navController: NavHostController) {
    val openDialog = remember { mutableStateOf(false) }
    val radioOptions = listOf("Light", "Dark", "Auto")

    val value = when(PrefsHelper.readInt(PrefsHelper.THEME_MODE, 2)) {
        0 -> "Light"
        1 -> "Dark"
        else -> "Auto"
    }

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(value) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        val currentDb = context.getDatabasePath(Constants.DB_NAME)

        if (uri != null && context.contentResolver.getType(uri).equals("application/octet-stream")) {
            importDatabase(context, uri, currentDb)

            Toast.makeText(context, "Database imported successfully", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Doesn't seem to be a database file :(", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
    ) {
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
                openExportDialog = true
            },
        )

        MoreItem(
            icon = R.drawable.ic_import,
            title = "Import",
            description = "Import database from a file",
            onClick = {
                openImportDialog = true
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
                    tint = Color.Red,
                )
                Column {
                    Text(
                        modifier = Modifier.padding(25.dp, 0.dp),
                        text = "Clear Database",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                        ),
                    )
                    Text(
                        modifier = Modifier.padding(25.dp, 0.dp),
                        text = "Delete all your tracked records",
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = Color.Red,
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
                navController.navigate(Screen.About.route)
            },
        )

        MoreItem(
            icon = R.drawable.ic_article,
            title = "Libraries",
            description = "Open source licenses",
            onClick = {
                navController.navigate(Screen.Libraries.route)
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
                    Text("Apply")
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

    if (openExportDialog) {
        AlertDialog(
            onDismissRequest = {
                openExportDialog = false
            },
            title = {
                Text(
                    text = "Confirm Export",
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
                        text = "This operation will generate \"${Constants.BACKED_UP_FILE_NAME}\" file inside \"Download/Bookland\" folder.",
                        style = TextStyle(
                            color = MaterialTheme.colors.onSecondary,
                        ),
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val currentDb = context.getDatabasePath(Constants.DB_NAME)

                        exportDatabase(context, currentDb)
                        openExportDialog = false
                    }
                ) {
                    Text("Export")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openExportDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (openImportDialog) {
        AlertDialog(
            onDismissRequest = {
                openImportDialog = false
            },
            title = {
                Text(
                    text = "Confirm Import",
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
                        text = "Warning: This operation is irreversible and all your current data will be replaced.",
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = Color(0xFF9E9D24),
                        ),
                    )
                    Text(
                        text = "Please select \"${Constants.BACKED_UP_FILE_NAME}\" file.",
                        style = TextStyle(
                            color = MaterialTheme.colors.onSecondary,
                        ),
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "application/octet-stream"
                        intent.addCategory(Intent.CATEGORY_OPENABLE)
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                        launcher.launch("application/octet-stream")
                        openImportDialog = false
                    }
                ) {
                    Text("Import")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openImportDialog = false
                    }
                ) {
                    Text("Cancel")
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
                    text = "Clear Database",
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
                            color = Color(0xFF9E9D24),
                        ),
                    )
                    Text(
                        text = "Are you sure you want to clear database? It will delete all the books.",
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

private fun exportDatabase(context: Context, currentFile: File) {

    val contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI

    val selection = MediaStore.MediaColumns.RELATIVE_PATH + "=?"

    val selectionArgs = arrayOf(Environment.DIRECTORY_DOWNLOADS + "/Bookland/") //must include "/" in front and end

    val cursor = context.contentResolver.query(contentUri, null, selection, selectionArgs, null)!!

    var uri: Uri? = null

    cursor.use {
        while (it.moveToNext()) {
            val fileName = it.getString(it.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))

            if (fileName == Constants.BACKED_UP_FILE_NAME) {
                val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                uri = ContentUris.withAppendedId(contentUri, id)
                break
            }
        }

        if (uri == null) {
            val currentContentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, Constants.BACKED_UP_FILE_NAME)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + File.separator + "Bookland")
            }

            val currentUri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, currentContentValues)

            if (currentUri != null) {
                context.contentResolver.openOutputStream(currentUri, "rwt").use { outputStream ->
                    outputStream?.write(currentFile.readBytes())
                }
            }

            Toast.makeText(context, "Database exported successfully", Toast.LENGTH_LONG).show()
        } else {
            try {
                val outputStream: OutputStream = context.contentResolver.openOutputStream(
                    uri!!,
                    "rwt"
                )!! //overwrite mode, see below
                outputStream.write(currentFile.readBytes())
                outputStream.close()

                Toast.makeText(context, "File overwritten successfully", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Toast.makeText(context, "Failed to write file", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

private fun importDatabase(context: Context, uri: Uri, currentFile: File) {
    val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r", null)
    parcelFileDescriptor?.let {
        val inputStream = FileInputStream(it.fileDescriptor)
        val targetFile = File(currentFile.parent, currentFile.name.plus(currentFile.extension))

        val outputStream = FileOutputStream(targetFile)

        IOUtils.copy(inputStream, outputStream)
    }
}