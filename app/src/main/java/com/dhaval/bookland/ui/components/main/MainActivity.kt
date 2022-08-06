package com.dhaval.bookland.ui.components.main

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dhaval.bookland.R
import com.dhaval.bookland.models.Items
import com.dhaval.bookland.models.Status
import com.dhaval.bookland.ui.components.details.BookDetailsScreen
import com.dhaval.bookland.ui.components.search.SearchScreen
import com.dhaval.bookland.ui.theme.BooklandTheme
import com.dhaval.bookland.utils.*
import com.dhaval.bookland.viewmodels.BookViewModel
import com.dhaval.bookland.viewmodels.BookViewModelFactory
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues

@Immutable
enum class BottomTab(
    val title: String,
    val icon: Int,
) {
    TO_READ("To Read", R.drawable.ic_to_read),
    READING("Reading", R.drawable.ic_reading),
    FINISHED("Finished", R.drawable.ic_finished),
    MORE("More", R.drawable.ic_more_hor),
}

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private lateinit var bookViewModel: BookViewModel
    private lateinit var application: BooklandApplication

    override fun onBackPressed() {
        if(navController.currentBackStackEntry?.destination?.route == Screen.Search.route) {
            navController.navigate(Screen.Main.route)
            return
        }

        if(navController.currentBackStackEntry?.destination?.route == Screen.About.route) {
            navController.navigate(Screen.Main.route)
            return
        }

        if(navController.currentBackStackEntry?.destination?.route == Screen.Libraries.route) {
            navController.navigate(Screen.Main.route)
            return
        }

        if(bookViewModel.selectedTab.value != BottomTab.TO_READ) {
            bookViewModel.selectTab(BottomTab.TO_READ)
            return
        }

        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        application = (getApplication() as BooklandApplication)

        val repository = application.bookRepository
        bookViewModel = ViewModelProvider(this, BookViewModelFactory(repository))[BookViewModel::class.java]

        setContent {
            var themeMode = when(application.themeMode.value) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                else -> isSystemInDarkTheme()
            }
            if(PrefsHelper.keyExists(PrefsHelper.THEME_MODE)) {
                val value = PrefsHelper.readInt(PrefsHelper.THEME_MODE, 0)
                themeMode = when(value) {
                    0 -> false
                    1 -> true
                    else -> isSystemInDarkTheme()
                }
            }

            ProvideWindowInsets {
                BooklandTheme(themeMode) {
                    Surface(color = MaterialTheme.colors.background) {
                        navController = rememberNavController()

                        NavigateScreens(navController = navController)
                    }
                }
            }
        }
    }

    @Composable
    fun MainScreen() {
        com.google.accompanist.insets.ui.Scaffold(
            topBar = { TopBar() },
            floatingActionButton = {
                if(bookViewModel.selectedTab.value != BottomTab.MORE)
                    FAB()
            },
            bottomBar = { BottomBar() },
        ) { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding)) {
                when (bookViewModel.selectedTab.value) {
                    BottomTab.TO_READ ->
                        bookViewModel.getItemsByStatus(Status.TO_READ).observeAsState().value?.let {
                                items -> ToReadScreen(navController, items)
                        }
                    BottomTab.READING ->
                        bookViewModel.getItemsByStatus(Status.READING).observeAsState().value?.let {
                                items -> ReadingScreen(navController, items)
                        }
                    BottomTab.FINISHED ->
                        bookViewModel.getItemsByStatus(Status.FINISHED).observeAsState().value?.let {
                                items -> FinishedScreen(navController, items)
                        }
                    BottomTab.MORE ->
                        MoreScreen(applicationContext, application, bookViewModel, navController)
                }
            }
        }
    }

    @Composable
    fun TopBar() {
        com.google.accompanist.insets.ui.TopAppBar(
            title = {
                Text(
                    text = bookViewModel.selectedTab.value.title,
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
            contentPadding = rememberInsetsPaddingValues(
                LocalWindowInsets.current.statusBars,
                applyBottom = false,
            ),
        )
    }

    @Composable
    fun FAB() {
        FloatingActionButton(
            modifier = Modifier.size(60.dp),
            onClick = {
                navController.navigate(Screen.Search.route)
                bookViewModel.emptySearchedResult = true
            },
            backgroundColor = MaterialTheme.colors.secondary,
        ) {
            Icon(Icons.Default.Add, "", tint = MaterialTheme.colors.secondaryVariant)
        }
    }

    @Composable
    fun BottomBar() {
        val selectedTab by bookViewModel.selectedTab
        val tabs = BottomTab.values()

        BottomNavigation(
            backgroundColor = MaterialTheme.colors.background,
        ) {
            tabs.forEach { tab ->
                BottomNavigationItem(
                    icon = { Icon(painterResource(id = tab.icon), contentDescription = null) },
                    label = { Text(tab.title) },
                    selectedContentColor = MaterialTheme.colors.secondary,
                    unselectedContentColor = MaterialTheme.colors.onSecondary.copy(.4f),
                    alwaysShowLabel = false,
                    selected = tab == selectedTab,
                    onClick = { bookViewModel.selectTab(tab) },
                )
            }
        }
    }

    @Composable
    fun NavigateScreens(navController: NavHostController) {
        NavHost(navController, startDestination = Screen.Splash.route) {
            composable(Screen.Splash.route) {
                SplashScreen(navController)
            }
            composable(Screen.Main.route) {
                MainScreen()
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    navController = navController,
                    bookViewModel = bookViewModel,
                )
            }
            composable(
                route = Screen.Details.route,
            ) {
                val item = navController.previousBackStackEntry?.savedStateHandle?.get<Items>("item")
                if (item != null) {
                    BookDetailsScreen(
                        context = applicationContext,
                        navController = navController,
                        bookViewModel = bookViewModel,
                        item = item,
                    )
                }
            }
            composable(Screen.About.route) {
                AboutScreen(applicationContext, navController)
            }
            composable(Screen.Libraries.route) {
                LibrariesScreen(navController)
            }
        }
    }
}