package com.example.moodtracker.data.network.models

import com.squareup.moshi.Json
import org.threeten.bp.LocalDateTime

data class MoodDto(
    @field:Json(name = "user") var user: String,
    @field:Json(name = "rating") var rating: Int,
    @field:Json(name = "comment") var comment: String,
    @field:Json(name = "date") var date: LocalDateTime,
    @field:Json(name = "private") var isPrivate : Boolean = false
)