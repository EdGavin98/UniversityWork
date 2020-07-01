package com.example.moodtracker.utils.mappers

import com.example.moodtracker.data.database.models.entities.Solution
import com.example.moodtracker.data.network.models.SolutionDto
import org.threeten.bp.LocalDateTime

fun Solution.convertToSolutionDto(): SolutionDto {
    return SolutionDto(
        timeLogged = this.timeLogged,
        description = this.description,
        advantages = this.advantages,
        disadvantages = this.advantages
    )
}

fun SolutionDto.convertToSolutionEntity(worryDate: LocalDateTime): Solution {
    return Solution(
        worryDate = worryDate,
        timeLogged = this.timeLogged,
        description = this.description,
        advantages = this.advantages,
        disadvantages = this.advantages
    )
}
