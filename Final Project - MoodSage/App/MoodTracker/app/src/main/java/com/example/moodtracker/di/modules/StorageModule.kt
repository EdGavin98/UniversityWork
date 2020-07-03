package com.example.moodtracker.di.modules

import android.content.Context
import com.example.moodtracker.data.database.TrackerRoomDatabase
import com.example.moodtracker.repository.account.AccountLocal
import com.example.moodtracker.repository.mood.MoodLocal
import com.example.moodtracker.repository.solution.SolutionLocal
import com.example.moodtracker.repository.worry.WorryLocal
import dagger.Module
import dagger.Provides

@Module
object StorageModule {

    @Provides
    fun provideRoomDatabase(context: Context): TrackerRoomDatabase {
        return TrackerRoomDatabase.getDatabase(context)
    }

    @Provides
    fun provideUserDao(db: TrackerRoomDatabase): AccountLocal {
        return db.userDao()
    }

    @Provides
    fun provideMoodDao(db: TrackerRoomDatabase): MoodLocal {
        return db.moodDao()
    }

    @Provides
    fun provideWorryDao(db: TrackerRoomDatabase): WorryLocal {
        return db.worryDao()
    }

    @Provides
    fun provideSolutionDao(db: TrackerRoomDatabase): SolutionLocal {
        return db.solutionDao()
    }

}