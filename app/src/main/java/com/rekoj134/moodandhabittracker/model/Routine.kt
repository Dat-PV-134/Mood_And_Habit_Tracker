package com.rekoj134.moodandhabittracker.model

data class Routine(
    val id: Int = 0,
    val routineName: String,
    val goal: String = "",
    val repeat: String = "",
    val notifyTime: Long,
    val routineTasks: String,
    val startDate: String,
    val completeDates: String
)