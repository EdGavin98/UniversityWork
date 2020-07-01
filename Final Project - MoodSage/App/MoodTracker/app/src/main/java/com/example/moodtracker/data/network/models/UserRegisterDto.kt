package com.example.moodtracker.data.network.models

import com.squareup.moshi.Json

data class UserRegisterDto(
    @field:Json(name = "forename") var forename: String,
    @field:Json(name = "surname") var surname: String,
    @field:Json(name = "password") var password: String,
    @field:Json(name = "email") var email: String,
    @field:Json(name = "role") var role: String = "patient"
)