package com.rekoj134.moodandhabittracker.model

import java.io.Serializable

data class RoutineTask(
    val id: Int,
    val taskName: String,
) : Serializable