package com.example.moodtracker.repository.solution

import com.example.moodtracker.data.database.models.entities.Solution
import com.example.moodtracker.data.network.ApiCall
import com.example.moodtracker.data.network.makeCall
import com.example.moodtracker.data.network.retrofit.SolutionRetrofitService
import com.example.moodtracker.utils.mappers.convertToSolutionDto
import javax.inject.Inject

class SolutionNetworkService @Inject constructor(private val retrofit : SolutionRetrofitService) : SolutionNetwork {

    override suspend fun addSolutionToWorry(solution: Solution, token: String): ApiCall<Unit> {
        return makeCall {
            retrofit.addSolutionToWorry(
                token = token,
                date = solution.worryDate.toString(),
                solution = solution.convertToSolutionDto()
            )
        }
    }


}