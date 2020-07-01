package com.example.moodtracker.data.network.models

import com.squareup.moshi.Json
import org.threeten.bp.LocalDateTime

data class SolutionDto(
    @field:Json(name = "timeLogged") var timeLogged: LocalDateTime,
    @field:Json(name = "description") var description: String,
    @field:Json(name = "advantages") var advantages: String,
    @field:Json(name = "disadvantages") var disadvantages: String
)