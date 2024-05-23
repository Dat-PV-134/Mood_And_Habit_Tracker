package com.rekoj134.moodandhabittracker.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rekoj134.moodandhabittracker.model.Mood

@Dao
interface MoodDao {
    @Insert
    suspend fun insertMood(mood: Mood) : Long

    @Update
    suspend fun updateMood(mood: Mood)

    @Delete
    suspend fun deleteMood(mood: Mood)

    @Query("SELECT * FROM tbl_moods")
    fun getAllMood() : List<Mood>

    @Query("SELECT * FROM tbl_moods ORDER BY id DESC")
    fun getAllMoodSorted() : List<Mood>
}