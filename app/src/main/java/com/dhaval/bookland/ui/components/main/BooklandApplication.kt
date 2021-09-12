package com.dhaval.bookland.ui.components.main

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import com.dhaval.bookland.db.BookDatabase
import com.dhaval.bookland.networking.BookApi
import com.dhaval.bookland.networking.RetrofitClient
import com.dhaval.bookland.repositories.BookRepository

enum class ThemeMode {
    LIGHT, DARK, AUTO
}

class BooklandApplication : Application() {
    lateinit var bookRepository: BookRepository

    val themeMode = mutableStateOf(ThemeMode.AUTO)
    lateinit var prefs: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val bookApi = RetrofitClient.getInstance().create(BookApi::class.java)
        val database = BookDatabase.getDatabase(applicationContext)
        bookRepository = BookRepository(bookApi, database)

        prefs = getSharedPreferences("ThemeMode", Context.MODE_PRIVATE)
        editor = prefs.edit()
    }
}