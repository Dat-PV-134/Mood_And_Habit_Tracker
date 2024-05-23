package com.rekoj134.moodandhabittracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tbl_moods")
data class Mood(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id:Int = 0,
    @ColumnInfo(name = "mood")
    val mood: Int,
    @ColumnInfo(name = "date")
    val date: Timeline,
    @ColumnInfo(name = "feeling")
    val feeling: String
) : Serializable