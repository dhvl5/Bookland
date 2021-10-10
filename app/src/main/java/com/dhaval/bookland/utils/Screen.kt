package com.dhaval.bookland.utils

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Search : Screen("search")
    object Details : Screen("More")
}