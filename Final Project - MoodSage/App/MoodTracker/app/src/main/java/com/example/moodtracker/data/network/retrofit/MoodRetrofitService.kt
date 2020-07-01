package com.example.moodtracker.data.network.retrofit

import com.example.moodtracker.data.network.models.MoodDto
import org.threeten.bp.LocalDateTime
import retrofit2.Response
import retrofit2.http.*

interface MoodRetrofitService {

    @POST("api/profile/moods")
    suspend fun addMood(
        @Header("Authorization") token: String,
        @Body mood: MoodDto
    ): Response<Unit>

    @GET("api/profile/moods")
    suspend fun getMoods(
        @Header("Authorization") token: String
    ): Response<List<MoodDto>>

    @DELETE("api/profile/moods/{date}")
    suspend fun deleteMood(
        @Header("Authorization") token: String,
        @Path("date") date: LocalDateTime
    ) : Response<Unit>

}