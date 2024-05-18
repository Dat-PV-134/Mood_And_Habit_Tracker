package com.rekoj134.moodandhabittracker.model

data class Journal(
    val id: Int,
    val date: String,
    val content: String,
    val emotion: Int
)