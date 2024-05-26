package com.rekoj134.moodandhabittracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tbl_focus")
data class Focus(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "label")
    val label: String,
    @ColumnInfo(name = "focused_time")
    val focusedTime: FocusTime,
    @ColumnInfo(name = "color")
    val color: String,
) : Serializable