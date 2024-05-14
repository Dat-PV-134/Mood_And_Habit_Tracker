package com.rekoj134.moodandhabittracker.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rekoj134.moodandhabittracker.model.Timeline

class TimeLineConverter {
    @TypeConverter
    fun fromStringObject(value: String) : Timeline {
        val timeLine = object : TypeToken<Timeline>() {}.type
        return Gson().fromJson(value, timeLine)
    }

    @TypeConverter
    fun fromObject(timeLine: Timeline): String {
        return Gson().toJson(timeLine)
    }
}