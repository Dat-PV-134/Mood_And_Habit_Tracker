package com.rekoj134.moodandhabittracker.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rekoj134.moodandhabittracker.model.Routine

@Dao
interface RoutineDao {
    @Insert
    suspend fun insertRoutine(routine: Routine)

    @Update
    suspend fun updateRoutine(routine: Routine)

    @Delete
    suspend fun deleteRoutine(routine: Routine)

    @Query("SELECT * FROM tbl_routines")
    fun getAllRoutine() : List<Routine>

    @Query("SELECT * FROM tbl_routines ORDER BY id DESC")
    fun getAllRoutineSorted() : List<Routine>
}