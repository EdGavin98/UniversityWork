package com.example.moodtracker.repository.solution

import com.example.moodtracker.data.database.models.entities.Solution
import com.example.moodtracker.data.database.models.returnobjects.WorryWithSolutions
import org.threeten.bp.LocalDateTime

interface SolutionLocal {

    /***
     * Add a solution to the local data source
     * @param solution - Solution to be added
     * @return id of the solution
     */
    suspend fun addSolution(solution: Solution) : Long

    /***
     * Get a specific solution
     * @param id - id of the solution
     * @return The solution for that ID, null if nothing exists
     */
    suspend fun getSolutionById(id: Long) : Solution?

    /***
     * Get all of the worries in the database, with their solutions
     * @return List of all worries with solutions
     */
    suspend fun getAllSolutionsForWorries() : List<WorryWithSolutions>

    /***
     * Get a worry for a specific date with its solutions
     * @param date - Date of the worry to retrieve
     * @return The worry with solutions, null if nothing exists
     */
    suspend fun getWorryByDateWithSolutions(date: LocalDateTime): WorryWithSolutions?

    /***
     * Update a specific solution
     * @param solution - the solution to be updated
     */
    suspend fun updateSolution(solution: Solution)

}