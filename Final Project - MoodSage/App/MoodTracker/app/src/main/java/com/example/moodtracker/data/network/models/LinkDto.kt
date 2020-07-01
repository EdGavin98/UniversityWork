package com.example.moodtracker.data.network.models

import com.squareup.moshi.Json

data class LinkDto(
    @Json(name = "status") val status: Int,
    @Json(name = "user") val user: UserDto
)