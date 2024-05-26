package com.rekoj134.moodandhabittracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rekoj134.moodandhabittracker.db.converter.FocusTimeConverter
import com.rekoj134.moodandhabittracker.db.converter.RoutineTasksConverter
import com.rekoj134.moodandhabittracker.db.converter.TimeLineConverter
import com.rekoj134.moodandhabittracker.db.converter.TimeLinesConverter
import com.rekoj134.moodandhabittracker.db.dao.FocusDao
import com.rekoj134.moodandhabittracker.db.dao.MoodDao
import com.rekoj134.moodandhabittracker.db.dao.RoutineDao
import com.rekoj134.moodandhabittracker.model.Focus
import com.rekoj134.moodandhabittracker.model.Mood
import com.rekoj134.moodandhabittracker.model.Routine
import com.rekoj134.moodandhabittracker.util.getBackUpDirectory

@Database(
    entities = [Routine::class, Mood::class, Focus::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(value = [RoutineTasksConverter::class, TimeLinesConverter::class, TimeLineConverter::class, FocusTimeConverter::class])
abstract class MyDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
    abstract fun moodDao(): MoodDao
    abstract fun focusDao(): FocusDao

    companion object {
        const val BACKUP_DIRECTORY = ".backup_routines"
        private val storagePath = "${getBackUpDirectory()}/routines.db"

        @Volatile
        private var INSTANCE: MyDatabase? = null
        fun getInstance(context: Context): MyDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        "my_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
