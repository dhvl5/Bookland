package com.dhaval.bookland.ui.components.main

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import com.dhaval.bookland.db.BookDatabase
import com.dhaval.bookland.networking.BookApi
import com.dhaval.bookland.networking.RetrofitClient
import com.dhaval.bookland.repositories.BookRepository
import com.dhaval.bookland.utils.PrefsHelper

enum class ThemeMode {
    LIGHT, DARK, AUTO
}

class BooklandApplication : Application() {
    lateinit var bookRepository: BookRepository

    val themeMode = mutableStateOf(ThemeMode.AUTO)

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val bookApi = RetrofitClient.getInstance().create(BookApi::class.java)
        val database = BookDatabase.getDatabase(applicationContext)
        bookRepository = BookRepository(bookApi, database)

        PrefsHelper.initPrefs(applicationContext)
    }
}