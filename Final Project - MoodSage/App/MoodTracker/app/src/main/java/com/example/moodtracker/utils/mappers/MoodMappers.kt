package com.example.moodtracker.utils.mappers

import com.example.moodtracker.data.database.models.entities.Mood
import com.example.moodtracker.data.network.models.MoodDto

fun Mood.convertToMoodDto(): MoodDto {
    return MoodDto(
        user = userId,
        rating = rating,
        comment = comment,
        date = date,
        isPrivate = isPrivate
    )
}

fun MoodDto.convertToMoodEntity(): Mood {
    return Mood(
        userId = user,
        rating = rating,
        comment = comment,
        date = date,
        isPrivate = isPrivate
    )
}