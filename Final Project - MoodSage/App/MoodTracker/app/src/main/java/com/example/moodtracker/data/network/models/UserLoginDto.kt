package com.example.moodtracker.data.network.models

import com.squareup.moshi.Json

data class UserLoginDto(
    @field:Json(name = "email" )val email: String,
    @field:Json(name = "password") val password: String
)