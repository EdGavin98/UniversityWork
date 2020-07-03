package com.example.moodtracker.repository.solution

import com.example.moodtracker.data.database.models.entities.Solution
import com.example.moodtracker.data.database.models.returnobjects.WorryWithSolutions
import com.example.moodtracker.data.network.ApiCallState
import com.example.moodtracker.data.sharedprefs.SharedPreferencesManager
import com.example.moodtracker.repository.Resource
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class SolutionRepositoryImpl @Inject constructor(
    private val local: SolutionLocal,
    private val remote: SolutionNetwork,
    private val sp : SharedPreferencesManager
) : SolutionRepository {


    override suspend fun addSolution(
        description: String,
        advantages: String,
        disadvantages: String,
        worryDate: LocalDateTime
    ): Resource<Long> {

        val solution = Solution(
            timeLogged = LocalDateTime.now(),
            worryDate = worryDate,
            description = description,
            advantages = advantages,
            disadvantages = disadvantages
        )

        val id = local.addSolution(solution)
        return Resource.success(id)

    }

    override suspend fun getAllSolutionsForWorries(): Resource<List<WorryWithSolutions>> {
        return local.getAllSolutionsForWorries().let { list ->
            if (list.isNullOrEmpty()) {
                Resource.failure("No worries logged yet", list)
            } else {
                Resource.success(list)
            }
        }
    }

    override suspend fun getSolutionsForWorry(date: LocalDateTime): Resource<WorryWithSolutions> {
        return local.getWorryByDateWithSolutions(date).let { worry ->
            if (worry != null) {
                Resource.success(worry)
            } else {
                Resource.failure("No worry exists on this date", null)
            }
        }
    }

    override suspend fun syncSolution(id: Long): Resource<Unit> {
        val solution = local.getSolutionById(id)
        return if (solution != null) {
            remote.addSolutionToWorry(solution, "Bearer ${sp.getToken()}").run {
                when (state) {
                    ApiCallState.SUCCESS -> {
                        solution.uploaded = true
                        local.updateSolution(solution)
                        Resource.success(Unit)
                    }
                    ApiCallState.ERROR -> Resource.failure("Server error while syncing solution", null)
                    ApiCallState.NETWORK_ERROR -> Resource.failure("Network error while syncing solution", null)
                }
            }
        } else {
            return Resource.failure("No solution", null)
        }
    }

}