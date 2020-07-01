package com.example.moodtracker.data.database.models.returnobjects

import org.threeten.bp.LocalDateTime

data class DateRating(
    val date: LocalDateTime,
    val value: Int
)