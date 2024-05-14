package com.rekoj134.moodandhabittracker.model

import java.io.Serializable
import java.time.LocalDate

data class Timeline(
    val id: Int,
    val date: String,
    val hour: Int,
    val minute: Int,
    val second: Int
) : Serializable