package com.example.moodtracker.repository.solution

import com.example.moodtracker.data.database.models.returnobjects.WorryWithSolutions
import com.example.moodtracker.repository.Resource
import org.threeten.bp.LocalDateTime

interface SolutionRepository {

    /***
     * Add a solution
     * @param description - The solutions description
     * @param advantages - The advantages of the solution
     * @param disadvantages - The disadvantages of the solution
     * @param worryDate - The date of the worry this solution is for
     * @return Resource object with the status of the operation and the id of the solution
     */
    suspend fun addSolution(
        description: String,
        advantages: String,
        disadvantages: String,
        worryDate: LocalDateTime
    ): Resource<Long>

    /***
     * Get all of the worries in the database with their corresponding solutions
     * @return Resource with success status and list if there are any, failure if there are not
     */
    suspend fun getAllSolutionsForWorries() : Resource<List<WorryWithSolutions>>

    /***
     *  Get a worry with all of its solutions
     *  @param date - Date of the worry
     *  @return Resource with success status and worry if worry exists, resource with failure status if it does not
     */
    suspend fun getSolutionsForWorry(date: LocalDateTime) : Resource<WorryWithSolutions>

    /***
     * Synchronise a solution with the network
     *  @param id - Id of the solution to sync
     *  @return Resource object with the status of the operation
     */
    suspend fun syncSolution(id: Long) : Resource<Unit>

}
