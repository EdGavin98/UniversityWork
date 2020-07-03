package com.example.moodtracker.data.network.models

import com.squareup.moshi.Json

data class LinkAcceptDto(
    @Json(name = "user") val user: String
)