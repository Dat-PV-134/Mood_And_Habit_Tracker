package com.rekoj134.moodandhabittracker.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rekoj134.moodandhabittracker.model.FocusTime
import com.rekoj134.moodandhabittracker.model.Timeline

class FocusTimeConverter {
    @TypeConverter
    fun fromStringObject(value: String) : FocusTime {
        val focusTime = object : TypeToken<FocusTime>() {}.type
        return Gson().fromJson(value, focusTime)
    }

    @TypeConverter
    fun fromObject(focusTime: FocusTime): String {
        return Gson().toJson(focusTime)
    }
}