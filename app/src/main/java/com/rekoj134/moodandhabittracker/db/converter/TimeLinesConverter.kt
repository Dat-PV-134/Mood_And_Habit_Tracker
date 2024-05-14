package com.rekoj134.moodandhabittracker.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rekoj134.moodandhabittracker.model.RoutineTask
import com.rekoj134.moodandhabittracker.model.Timeline

class TimeLinesConverter {
    @TypeConverter
    fun fromString(value: String): ArrayList<Timeline> {
        val listType = object : TypeToken<ArrayList<Timeline>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: ArrayList<Timeline>): String {
        return Gson().toJson(list)
    }
}