package com.example.moodtracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moodtracker.data.database.models.entities.Mood
import com.example.moodtracker.data.database.models.returnobjects.DateRating
import com.example.moodtracker.repository.mood.MoodLocal
import org.threeten.bp.LocalDateTime

@Dao
interface MoodDao : MoodLocal {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun addMood(mood: Mood)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun addMoods(moods: List<Mood>)

    @Query("SELECT * FROM moods")
    override suspend fun getAllMoods(): List<Mood>

    @Query("SELECT * FROM moods WHERE date = :moodDate")
    override suspend fun getMoodByDate(moodDate: LocalDateTime): Mood?

    @Query("SELECT * FROM moods WHERE date BETWEEN :monthStart AND :monthEnd")
    override suspend fun getMoodsFromRange(
        monthStart: LocalDateTime,
        monthEnd: LocalDateTime
    ): List<Mood>

    @Query("SELECT rating AS value, date FROM moods")
    override suspend fun getAllMoodRatings(): List<DateRating>

    @Query("UPDATE moods SET uploaded = :isUploaded WHERE date = :date ")
    override suspend fun updateMoodUploadStatus(date: LocalDateTime, isUploaded: Boolean)

    @Query("DELETE FROM moods WHERE date = :date" )
    override suspend fun deleteMoodByDate(date: LocalDateTime)

}