package com.rekoj134.moodandhabittracker.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rekoj134.moodandhabittracker.model.RoutineTask

class RoutineTasksConverter {
    @TypeConverter
    fun fromString(value: String): ArrayList<RoutineTask> {
        val listType = object : TypeToken<ArrayList<RoutineTask>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: ArrayList<RoutineTask>): String {
        return Gson().toJson(list)
    }
}