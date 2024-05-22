package com.rekoj134.moodandhabittracker.preference

import android.content.Context
import android.content.SharedPreferences

object MyPreferences {
    private lateinit var prefs: SharedPreferences
    private const val PREFS_NAME = "shared_preferences"
    const val PREF_COMPLETE_TASKS_TODAY = "pref_complete_tasks_today"
    const val PREF_TODAY = "pref_today"

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun read(key: String, value: String): String? {
        return prefs.getString(key, value)
    }

    fun write(key: String, value: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(key, value)
            commit()
        }
    }
}