package com.rekoj134.moodandhabittracker.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object LocalDateUtil {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    fun fromDateToString(date: LocalDate): String {
        return date.format(formatter)
    }

    fun fromStringToDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString, formatter)
    }
}