package com.rekoj134.moodandhabittracker.model

data class Focus(
    val id: Int,
    val label: String,
    val focusedTime: FocusTime,
    val color: Int,
    val startDate: Timeline
)