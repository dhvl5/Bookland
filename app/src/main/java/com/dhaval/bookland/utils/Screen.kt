package com.dhaval.bookland.utils

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Main : Screen("main")
    object Search : Screen("search")
    object Details : Screen("more")
    object About : Screen("about")
    object Libraries : Screen("libraries")
}