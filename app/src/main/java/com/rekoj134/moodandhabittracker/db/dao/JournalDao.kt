package com.rekoj134.moodandhabittracker.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rekoj134.moodandhabittracker.model.Journal

@Dao
interface JournalDao {
    @Insert
    suspend fun insertJournal(journal: Journal)

    @Update
    suspend fun updateJournal(journal: Journal)

    @Delete
    suspend fun deleteJournal(journal: Journal)

    @Query("SELECT * FROM tbl_journals")
    fun getAllJournal() : List<Journal>

    @Query("SELECT * FROM tbl_journals ORDER BY id DESC")
    fun getAllJournalSorted() : List<Journal>
}