package com.example.moodtracker.repository.solution

import com.example.moodtracker.data.database.models.entities.Solution
import com.example.moodtracker.data.database.models.entities.Worry
import com.example.moodtracker.data.database.models.returnobjects.WorryWithSolutions
import org.threeten.bp.LocalDateTime

class LocalMockSource : SolutionLocal {

    private val solutions = mutableListOf(
        Solution(
            id = 0,
            worryDate = LocalDateTime.of(2020, 3, 20, 0, 0),
            timeLogged = LocalDateTime.of(2020, 3, 20, 0, 1),
            description = "Test Description",
            advantages = "Test Advantages,",
            disadvantages = "Test Disadvantages",
            uploaded = false
        ),
        Solution(
            id = 1,
            worryDate = LocalDateTime.of(2020, 3, 20, 0, 0),
            timeLogged = LocalDateTime.of(2020, 3, 20, 0, 2),
            description = "Test Description",
            advantages = "Test Advantages,",
            disadvantages = "Test Disadvantages",
            uploaded = false
        )
    )

    private val worriesWith = mutableListOf(
        WorryWithSolutions(
            worry = Worry(
                date = LocalDateTime.of(2020, 3, 20, 0, 0),
                user = "User",
                severity = 5,
                description = "TestDesc",
                isPrivate = false,
                type = "Current",
                uploaded = false
            ),
            solutions = solutions
        )
    )

    override suspend fun addSolution(solution: Solution): Long {
        solutions.add(solution)
        return 2
    }

    override suspend fun getSolutionById(id: Long): Solution? {
        return solutions.find {
            it.id == 1L
        }
    }

    override suspend fun getAllSolutionsForWorries(): List<WorryWithSolutions> {
        return worriesWith
    }

    override suspend fun getWorryByDateWithSolutions(date: LocalDateTime): WorryWithSolutions? {
        return worriesWith.find {
            it.worry!!.date.isEqual(date)
        }
    }

    override suspend fun updateSolution(solution: Solution) {
        solutions[0] = solution
    }
}