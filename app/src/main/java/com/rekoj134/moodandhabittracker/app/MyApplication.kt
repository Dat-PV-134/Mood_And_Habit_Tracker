package com.rekoj134.moodandhabittracker.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.rekoj134.moodandhabittracker.model.Focus
import com.rekoj134.moodandhabittracker.preference.MyPreferences
import com.rekoj134.moodandhabittracker.presentation.focus.FocusInstance
import com.rekoj134.moodandhabittracker.presentation.focus.FocusInstance.breakTime
import com.rekoj134.moodandhabittracker.presentation.focus.FocusInstance.focusTime
import com.rekoj134.moodandhabittracker.presentation.focus.FocusInstance.longBreakTime

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MyPreferences.init(this)
        createNotificationChannel()
        initFocus()
    }

    private fun initFocus() {
        longBreakTime = MyPreferences.read(MyPreferences.PREF_LONG_BREAK_TIME, 900000L)
        FocusInstance.totalLongBreakTime = longBreakTime

        focusTime = MyPreferences.read(MyPreferences.PREF_FOCUS_TIME, 1500000L)
        FocusInstance.totalFocusTime = focusTime

        breakTime = MyPreferences.read(MyPreferences.PREF_BREAK_TIME, 300000L)
        FocusInstance.totalBreakTime = breakTime
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
        val manager = getSystemService(NotificationManager::class.java) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "CHANNEL_ID"
        const val CHANNEL_NAME = "my_channel"
    }
}