package com.rekoj134.moodandhabittracker.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rekoj134.moodandhabittracker.model.JournalContent
import com.rekoj134.moodandhabittracker.model.Label
import com.rekoj134.moodandhabittracker.model.RoutineTask
import com.rekoj134.moodandhabittracker.model.Text
import com.rekoj134.moodandhabittracker.model.Timeline

object ModelConverterUtil {
    fun fromStringToPattern(value: String): List<String> {
        if (value.isEmpty()) return ArrayList()
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    fun fromPatternToString(list: List<String>): String {
        return Gson().toJson(list)
    }

    fun fromStringToTimeline(value: String): Timeline {
        if (value.isEmpty()) return Timeline(0, "", 0, 0, 0)
        val timeline = object : TypeToken<Timeline>() {}.type
        return Gson().fromJson(value, timeline)
    }

    fun fromTimelineToString(timeline: Timeline): String {
        return Gson().toJson(timeline)
    }

    fun fromStringToLabel(value: String): Label {
        if (value.isEmpty()) return Label(0, "Unknown", "#4D94CADB")
        val label = object : TypeToken<Label>() {}.type
        return Gson().fromJson(value, label)
    }

    fun fromLabelToString(label: Label): String {
        return Gson().toJson(label)
    }

    fun fromStringToListLabel(value: String): List<Label> {
        if (value.isEmpty()) return ArrayList()
        val listType = object : TypeToken<List<Label>>() {}.type
        return Gson().fromJson(value, listType)
    }

    fun fromLabelToString(list: List<Label>): String {
        return Gson().toJson(list)
    }

    fun fromStringToListContent(value: String): List<JournalContent> {
        if (value.isEmpty()) return ArrayList()
        val listType = object : TypeToken<List<JournalContent>>() {}.type
        return Gson().fromJson(value, listType)
    }

    fun fromListContentToString(list: List<JournalContent>): String {
        return Gson().toJson(list)
    }

    fun fromStringToText(value: String): Text {
        if (value.isEmpty()) return Text(text = "")
        val text = object : TypeToken<Text>() {}.type
        return Gson().fromJson(value, text)
    }

    fun fromTextToString(text: Text): String {
        return Gson().toJson(text)
    }
}