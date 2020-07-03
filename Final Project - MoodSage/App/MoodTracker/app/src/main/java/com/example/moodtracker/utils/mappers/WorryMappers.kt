package com.example.moodtracker.utils.mappers

import com.example.moodtracker.data.database.models.entities.Worry
import com.example.moodtracker.data.network.models.WorryDto

fun WorryDto.convertToWorryEntity(): Worry {
    return Worry(
        user = this.user,
        severity = this.severity,
        date = this.date,
        description = this.description,
        type = this.type,
        isPrivate = this.isPrivate,
        uploaded = true
    )
}

fun Worry.convertToWorryDto(): WorryDto {
    return WorryDto(
        user = this.user,
        severity = this.severity,
        date = this.date,
        description = this.description,
        isPrivate = this.isPrivate,
        type = this.type,
        solutions = emptyList()
    )
}