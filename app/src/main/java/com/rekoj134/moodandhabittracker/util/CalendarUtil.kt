package com.rekoj134.moodandhabittracker.util

import java.util.Calendar

object CalendarUtil {
    private val calendar = Calendar.getInstance()

    fun getCurrentHour() : Int {
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun getCurrentMinute() : Int {
        return calendar.get(Calendar.MINUTE)
    }

    fun getCurrentSecond() : Int {
        return calendar.get(Calendar.SECOND)
    }
}