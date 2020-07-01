package com.example.moodtracker.repository.mood

import com.example.moodtracker.data.database.models.entities.Mood
import com.example.moodtracker.data.network.ApiCall
import com.example.moodtracker.data.network.models.MoodDto
import com.example.moodtracker.utils.mappers.convertToMoodDto
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

class RemoteMockSource : MoodNetwork {

    private val moods = mutableListOf(
        MoodDto(
            user = "id",
            rating = 4,
            comment = "comment",
            date = LocalDate.now().atStartOfDay()
        ),
        MoodDto(
            user = "id",
            rating = 7,
            comment = "comment2",
            date = LocalDate.now().atStartOfDay().minusDays(1)
        )
    )

    private val testToken = "Bearer TestToken"

    override suspend fun addMood(mood: Mood, token: String): ApiCall<Unit> {
        return if (testToken == token) {
            moods.add(mood.convertToMoodDto())
            ApiCall.success(Unit, 201)
        } else {
            ApiCall.error(401)
        }
    }

    override suspend fun getMoods(token: String): ApiCall<List<MoodDto>> {
        return if (testToken == token) {
            ApiCall.success(this.moods, 200)
        } else {
            ApiCall.error(401)
        }
    }

    override suspend fun deleteMood(token: String, date: LocalDateTime): ApiCall<Unit> {
        return if (testToken == token) {
            ApiCall.success(Unit, 200)
        } else {
            ApiCall.error(401)
        }
    }

}