package com.example.moodtracker.data.network.models

import com.squareup.moshi.Json
import org.threeten.bp.LocalDateTime

data class WorryDto(
    @field:Json(name = "user")var user: String,
    @field:Json(name = "type")var type: String,
    @field:Json(name = "severity")var severity: Int,
    @field:Json(name = "description")var description: String,
    @field:Json(name = "private") var isPrivate: Boolean = false,
    @field:Json(name = "date")var date: LocalDateTime,
    @field:Json(name = "solutions")var solutions : List<SolutionDto>
)