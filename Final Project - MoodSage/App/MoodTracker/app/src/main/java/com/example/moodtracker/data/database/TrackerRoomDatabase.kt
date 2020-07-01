package com.example.moodtracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moodtracker.data.database.dao.MoodDao
import com.example.moodtracker.data.database.dao.SolutionDao
import com.example.moodtracker.data.database.dao.UserDao
import com.example.moodtracker.data.database.dao.WorryDao
import com.example.moodtracker.data.database.models.entities.Mood
import com.example.moodtracker.data.database.models.entities.Solution
import com.example.moodtracker.data.database.models.entities.User
import com.example.moodtracker.data.database.models.entities.Worry
import com.example.moodtracker.utils.RoomDateConverter

@Database(
    entities = [User::class, Mood::class, Worry::class, Solution::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomDateConverter::class)
abstract class TrackerRoomDatabase : RoomDatabase() {

    abstract fun moodDao(): MoodDao
    abstract fun userDao(): UserDao
    abstract fun worryDao(): WorryDao
    abstract fun solutionDao(): SolutionDao


    companion object {
        @Volatile
        private var INSTANCE: TrackerRoomDatabase? = null

        fun getDatabase(context: Context): TrackerRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrackerRoomDatabase::class.java,
                    "tracker_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}