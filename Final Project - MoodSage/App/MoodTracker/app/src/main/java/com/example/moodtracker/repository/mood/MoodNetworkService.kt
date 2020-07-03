package com.example.moodtracker.repository.mood

import com.example.moodtracker.data.database.models.entities.Mood
import com.example.moodtracker.data.network.ApiCall
import com.example.moodtracker.data.network.makeCall
import com.example.moodtracker.data.network.models.MoodDto
import com.example.moodtracker.data.network.retrofit.MoodRetrofitService
import com.example.moodtracker.utils.mappers.convertToMoodDto
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class MoodNetworkService @Inject constructor(private val retrofit: MoodRetrofitService) :
    MoodNetwork {

    override suspend fun getMoods(token: String): ApiCall<List<MoodDto>> {
        return makeCall {
            retrofit.getMoods(token)
        }
    }

    override suspend fun deleteMood(
        token: String,
        date: LocalDateTime
    ): ApiCall<Unit> {
        return makeCall {
            retrofit.deleteMood(
                token = token,
                date = date
            )
        }
    }


    override suspend fun addMood(
        mood: Mood,
        token: String
    ): ApiCall<Unit> {
        return makeCall {
            retrofit.addMood(token, mood.convertToMoodDto())
        }
    }

}