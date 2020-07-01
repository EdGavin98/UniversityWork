package com.example.moodtracker.data.network.retrofit

import com.example.moodtracker.data.network.models.WorryDto
import org.threeten.bp.LocalDateTime
import retrofit2.Response
import retrofit2.http.*

interface WorryRetrofitService {

    @POST("api/profile/worries")
    suspend fun addWorry(
        @Header("Authorization") token: String,
        @Body worry: WorryDto
    ): Response<Unit>

    @GET("api/profile/worries")
    suspend fun getWorries(
        @Header("Authorization") token: String
    ): Response<List<WorryDto>>

    @DELETE("api/profile/worries/{date}")
    suspend fun deleteWorry(
        @Header("Authorization") token: String,
        @Path("date") date: LocalDateTime
    ): Response<Unit>

}