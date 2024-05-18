package com.rekoj134.moodandhabittracker.model

data class Mood(
    val id: Int,
    val mood: Int,
    val date: Timeline,
    val feeling: String
)