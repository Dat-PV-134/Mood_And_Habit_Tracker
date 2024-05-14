package com.rekoj134.moodandhabittracker.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rekoj134.moodandhabittracker.model.RoutineTask

object ModelConverterUtil {
    fun fromStringToPattern(value: String): List<String> {
        if (value.isEmpty()) return ArrayList()
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    fun fromPatternToString(list: List<String>): String {
        return Gson().toJson(list)
    }
}