package com.example.moodtracker.data.network.models

import com.squareup.moshi.Json

data class TokenDto(
    @field:Json(name = "token") val token: String
)
