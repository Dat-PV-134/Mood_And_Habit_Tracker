package com.rekoj134.moodandhabittracker.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rekoj134.moodandhabittracker.model.Focus
import com.rekoj134.moodandhabittracker.model.Mood

@Dao
interface FocusDao {
    @Insert
    suspend fun insertFocus(focus: Focus) : Long

    @Update
    suspend fun updateFocus(focus: Focus)

    @Delete
    suspend fun deleteFocus(focus: Focus)

    @Query("SELECT * FROM tbl_focus")
    fun getAllFocus() : List<Focus>

    @Query("SELECT * FROM tbl_focus ORDER BY id DESC")
    fun getAllFocusSorted() : List<Focus>
}