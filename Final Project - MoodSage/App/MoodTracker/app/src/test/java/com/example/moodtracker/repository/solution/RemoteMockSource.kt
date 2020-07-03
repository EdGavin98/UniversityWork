package com.example.moodtracker.repository.solution

import com.example.moodtracker.data.database.models.entities.Solution
import com.example.moodtracker.data.network.ApiCall

class RemoteMockSource : SolutionNetwork {

    private val testToken = "Bearer TestToken"


    override suspend fun addSolutionToWorry(solution: Solution, token: String): ApiCall<Unit> {
        return if (token == testToken) {
            ApiCall.success(Unit, 200)
        } else {
            ApiCall.error(403)
        }
    }

}