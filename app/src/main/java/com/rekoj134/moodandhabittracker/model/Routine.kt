package com.rekoj134.moodandhabittracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tbl_routines")
data class Routine(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "routine_name")
    var routineName: String,
    @ColumnInfo(name = "routine_goal")
    var routineGoal: String = "",
    @ColumnInfo(name = "repeat")
    var repeat: String = "",
    @ColumnInfo(name = "notify_time")
    var notifyTime: Long,
    @ColumnInfo(name = "routine_tasks")
    var routineTasks: ArrayList<RoutineTask>,
    @ColumnInfo(name = "start_date")
    var startDate: Timeline,
    @ColumnInfo(name = "complete_dates")
    var completeDates: ArrayList<Timeline>
) : Serializable