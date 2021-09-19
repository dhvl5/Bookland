package com.dhaval.bookland.utils

import android.content.Context
import android.content.SharedPreferences

object PrefsHelper {
    private lateinit var prefs: SharedPreferences
    private const val PREFS_NAME = "default_prefs"

    const val THEME_MODE = "theme_mode"

    fun initPrefs(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun keyExists(key: String): Boolean {
        if(prefs.contains(key))
            return true

        return false
    }

    fun writeString(key: String, value: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    fun writeInt(key: String, value: Int) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        prefsEditor.putInt(key, value)
        prefsEditor.apply()
    }

    fun readString(key: String, value: String): String? {
        return prefs.getString(key, value)
    }

    fun readInt(key: String, value: Int): Int {
        return prefs.getInt(key, value)
    }
}