package com.example.moodtracker.data.network.models

import com.squareup.moshi.Json

data class UserDto(
    @field:Json(name = "_id") val id: String,
    @field:Json(name = "forename") val forename: String,
    @field:Json(name = "surname") val surname: String,
    @field:Json(name = "email") val email: String,
    @field:Json(name = "role") val role: String = "patient",
    @field:Json(name = "moodTarget") val moodTarget: Int = 0,
    @field:Json(name = "worryTarget") val worryTarget: Int = 0
)
