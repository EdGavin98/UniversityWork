package com.example.moodtracker.repository.solution

import com.example.moodtracker.data.database.models.entities.Solution
import com.example.moodtracker.data.network.ApiCall

interface SolutionNetwork {

    /***
     * Send a specific worry to the remote data source
     * @param solution - the solution to be sent
     * @param token - the users API token
     * @return ApiCall object with the status of the call
     */
    suspend fun addSolutionToWorry(solution: Solution, token: String): ApiCall<Unit>

}