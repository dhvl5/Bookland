package com.dhaval.bookland.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    when(duration) {
        0 -> Toast.LENGTH_SHORT
        1 -> Toast.LENGTH_LONG
    }

    Toast.makeText(applicationContext, message , duration).show()
}