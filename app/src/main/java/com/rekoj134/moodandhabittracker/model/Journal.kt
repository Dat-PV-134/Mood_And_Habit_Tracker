package com.rekoj134.moodandhabittracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tbl_journals")
data class Journal(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "date")
    val date: Timeline,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "mood")
    val mood: Int
) : Serializable