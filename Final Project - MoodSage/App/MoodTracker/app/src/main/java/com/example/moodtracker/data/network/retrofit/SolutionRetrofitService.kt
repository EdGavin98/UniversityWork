package com.example.moodtracker.data.network.retrofit

import com.example.moodtracker.data.network.models.SolutionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface SolutionRetrofitService {

    @POST("api/profile/worries/{dateString}/solutions")
    suspend fun addSolutionToWorry(
        @Header("Authorization") token: String,
        @Path("dateString") date: String,
        @Body solution : SolutionDto
    ) : Response<Unit>
}