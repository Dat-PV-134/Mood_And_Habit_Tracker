package com.rekoj134.moodandhabittracker.preference

import android.content.Context
import android.content.SharedPreferences

object MyPreferences {
    private lateinit var prefs: SharedPreferences
    private const val PREFS_NAME = "shared_preferences"
    const val PREF_COMPLETE_TASKS_TODAY = "pref_complete_tasks_today"
    const val PREF_TODAY = "pref_today"
    const val PREF_LONG_BREAK_TIME = "pref_long_break_time"
    const val PREF_FOCUS_TIME = "pref_focus_time"
    const val PREF_BREAK_TIME = "pref_break_time"
    const val PREF_CURRENT_FOCUS_LABEL = "pref_current_focus_label"
    const val PREF_LANGUAGE = "pref_language"

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun read(key: String, value: String?): String? {
        return prefs.getString(key, value)
    }

    fun write(key: String, value: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(key, value)
            commit()
        }
    }

    fun read(key: String, value: Long): Long {
        return prefs.getLong(key, value)
    }

    fun write(key: String, value: Long) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putLong(key, value)
            commit()
        }
    }
}