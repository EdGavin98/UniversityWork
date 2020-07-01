package com.example.moodtracker.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moodtracker.data.database.models.entities.Solution
import com.example.moodtracker.data.database.models.entities.Worry
import com.example.moodtracker.data.database.models.returnobjects.DateRating
import com.example.moodtracker.data.database.models.returnobjects.WorryTypeCount
import com.example.moodtracker.repository.worry.WorryLocal
import org.threeten.bp.LocalDateTime

@Dao
interface WorryDao : WorryLocal {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun addWorry(worry: Worry)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun addWorry(worry: Worry, solutions: List<Solution>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun addWorries(worries: List<Worry>)

    @Query("SELECT * FROM worries ORDER BY date DESC")
    override fun getAllWorries(): LiveData<List<Worry>>

    @Query("SELECT date, severity AS value FROM worries")
    override suspend fun getAllWorrySeverities(): List<DateRating>

    @Query("SElECT COUNT(date) AS count, type FROM worries GROUP BY type")
    override suspend fun getCurrentAndHypotheticalCount(): List<WorryTypeCount>

    @Query("UPDATE worries SET uploaded = :isUploaded WHERE date = :date ")
    override suspend fun updateWorryUploadStatus(date: LocalDateTime, isUploaded: Boolean)

    @Query("DELETE FROM worries WHERE date = :date")
    override suspend fun deleteWorryByDate(date: LocalDateTime)

    @Query("SELECT * FROM worries WHERE date = :date")
    override suspend fun getWorryByDate(date: LocalDateTime): Worry?

}