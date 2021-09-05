package com.dhaval.bookland.utils

import com.dhaval.bookland.R

sealed class BottomNavItem(var route: String, var title: String, var icon: Int) {
    object ToRead : BottomNavItem("toread", "To Read", R.drawable.ic_to_read)
    object Reading : BottomNavItem("reading", "Reading", R.drawable.ic_reading)
    object Finished : BottomNavItem("finished", "Finished", R.drawable.ic_finished)
}