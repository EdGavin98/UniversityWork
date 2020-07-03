package com.example.moodtracker.data.network.retrofit

import com.example.moodtracker.data.network.models.*
import retrofit2.Response
import retrofit2.http.*

interface AccountRetrofitService {

    @POST("Auth/Register")
    suspend fun registerUser(
        @Body userDto: UserRegisterDto
    ): Response<Unit>

    @POST("Auth/Login")
    suspend fun loginUser(
        @Body loginDto: UserLoginDto
    ): Response<TokenDto>

    @GET("api/profile")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): Response<UserDto>

    @GET("api/profile/links")
    suspend fun getLinks(
        @Header("Authorization") token: String
    ): Response<List<LinkDto>>

    @PUT("api/profile/link/{id}")
    suspend fun acceptLink(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body bodyId: LinkAcceptDto
    ): Response<Unit>

    @DELETE("api/profile/link/{id}")
    suspend fun removeLink(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>

    @DELETE("api/profile")
    suspend fun deleteAccount(
        @Header("Authorization") token: String
    ): Response<Unit>

    @PUT("api/profile")
    suspend fun updateAccount(
        @Header("Authorization") token: String,
        @Body user: UserDto
    ): Response<Unit>

}
