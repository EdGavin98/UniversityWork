package com.example.moodtracker.repository.mood

import com.example.moodtracker.data.database.models.entities.Mood
import com.example.moodtracker.data.database.models.returnobjects.DateRating
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

class LocalMockSource : MoodLocal {

    private val moods = mutableListOf(
        Mood(
            userId = "id",
            rating = 4,
            comment = "comment",
            date = LocalDate.of(2020, 4, 1).atStartOfDay(),
            uploaded = true
        ),
        Mood(
            userId = "id",
            rating = 6,
            comment = "comment",
            date = LocalDate.of(2020, 4, 8).atStartOfDay(),
            uploaded = true
        ),
        Mood(
            userId = "id",
            rating = 7,
            comment = "comment2",
            date = LocalDate.of(2020, 4, 2).atStartOfDay(),
            uploaded = true
        )
    )

    override suspend fun addMood(mood: Mood) {
        moods.add(mood)
    }

    override suspend fun addMoods(moods: List<Mood>) {
        this.moods.addAll(moods)
    }

    override suspend fun getAllMoods(): List<Mood> {
        return this.moods
    }

    override suspend fun getMoodByDate(date: LocalDateTime): Mood? {
        return moods.find {
            it.date == date
        }
    }

    override suspend fun getMoodsFromRange(
        monthStart: LocalDateTime,
        monthEnd: LocalDateTime
    ): List<Mood> {
        return moods.filter {
            (it.date.isAfter(monthStart) || it.date.isEqual(monthStart)) &&
            (it.date.isBefore(monthEnd) || it.date.isEqual(monthEnd))
        }
    }

    override suspend fun getAllMoodRatings(): List<DateRating> {
        val ratings = mutableListOf<DateRating>()
        moods.forEach {
            ratings.add(DateRating(
                date = it.date,
                value = it.rating
            ))
        }
        return ratings
    }

    override suspend fun getUnsyncedMoods(): List<Mood> {
        return moods.filter {
            !it.uploaded
        }
    }

    override suspend fun updateMoodUploadStatus(date: LocalDateTime, isUploaded: Boolean) {
        moods.find { it.date == date }?.uploaded = isUploaded
    }

    override suspend fun deleteMoodByDate(date: LocalDateTime) {
        moods.removeIf {  mood ->
            mood.date == date
        }
    }
}