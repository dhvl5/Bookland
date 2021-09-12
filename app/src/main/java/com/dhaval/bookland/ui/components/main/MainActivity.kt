package com.dhaval.bookland.ui.components.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dhaval.bookland.R
import com.dhaval.bookland.models.Status
import com.dhaval.bookland.ui.components.search.SearchActivity
import com.dhaval.bookland.ui.theme.BooklandTheme
import com.dhaval.bookland.utils.BottomNavItem
import com.dhaval.bookland.viewmodels.BookViewModel
import com.dhaval.bookland.viewmodels.BookViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var bookViewModel: BookViewModel
    private lateinit var app: BooklandApplication

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
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }

    @Composable
    fun MainScreen() {
        val navController = rememberNavController()

        Scaffold(
            topBar = { TopBar() },
            floatingActionButton = {
                if(currentRoute(navController) != "more")
                    FAB()
            },
            bottomBar = { BottomBar(navController) },
        ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    NavigateScreens(navController = navController)
                }
        }
    }

    @Composable
    fun TopBar() {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = MaterialTheme.colors.onPrimary,
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontFamily = FontFamily.Cursive,
                        fontWeight = FontWeight.ExtraBold,
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            },
            backgroundColor = MaterialTheme.colors.background,
            elevation = 0.dp,
        )
    }

    @Composable
    fun FAB() {
        val context = LocalContext.current

        FloatingActionButton(
            modifier = Modifier.size(60.dp),
            onClick = {
                context.startActivity(Intent(context, SearchActivity::class.java))
                overridePendingTransition(R.anim.slide_in, R.anim.zoom_out)
            },
            backgroundColor = MaterialTheme.colors.secondary,
        ) {
            Icon(Icons.Default.Add, "", tint = MaterialTheme.colors.secondaryVariant)
        }
    }

    @Composable
    fun BottomBar(navController: NavController) {
        val items = listOf(BottomNavItem.ToRead, BottomNavItem.Reading, BottomNavItem.Finished, BottomNavItem.More)

        BottomNavigation(
            backgroundColor = MaterialTheme.colors.background,
        ) {
            items.forEach { item ->
                BottomNavigationItem(
                    icon = { Icon(painterResource(id = item.icon), contentDescription = null) },
                    label = { Text(item.title) },
                    selectedContentColor = MaterialTheme.colors.secondary,
                    unselectedContentColor = MaterialTheme.colors.onSecondary.copy(.4f),
                    alwaysShowLabel = false,
                    selected = currentRoute(navController) == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        }
    }

    @Composable
    fun NavigateScreens(navController: NavHostController) {
        NavHost(navController, startDestination = BottomNavItem.ToRead.route) {
            composable(BottomNavItem.ToRead.route) {
                bookViewModel.getItemsByStatus(Status.TO_READ).observeAsState().value?.let { items -> ToReadScreen(items) }
            }
            composable(BottomNavItem.Reading.route) {
                bookViewModel.getItemsByStatus(Status.READING).observeAsState().value?.let { items -> ReadingScreen(items) }
            }
            composable(BottomNavItem.Finished.route) {
                bookViewModel.getItemsByStatus(Status.FINISHED).observeAsState().value?.let { items -> FinishedScreen(items) }
            }
            composable(BottomNavItem.More.route) {
                MoreScreen(app)
            }
        }
    }

    @Composable
    fun currentRoute(navController: NavController): String? {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        return navBackStackEntry?.destination?.route
    }
}