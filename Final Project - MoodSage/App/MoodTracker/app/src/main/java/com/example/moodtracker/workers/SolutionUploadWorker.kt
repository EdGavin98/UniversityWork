package com.example.moodtracker.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.solution.SolutionRepository
import javax.inject.Inject

class SolutionUploadWorker(
    appContext: Context,
    workerParams : WorkerParameters,
    private val solutionRepo: SolutionRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val id = inputData.getLong("SOLUTION_ID", -1)

        return solutionRepo.syncSolution(id).run {
            when (status) {
                Status.SUCCESS -> Result.success()

                Status.FAILURE -> {
                    if (message == "No solution") {
                        Result.failure()
                    } else {
                        Result.retry()
                    }
                }
            }
        }
    }

    class Factory @Inject constructor(
        private val solutionRepo: SolutionRepository
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): CoroutineWorker {
            return SolutionUploadWorker(
                appContext,
                params,
                solutionRepo
            )
        }
    }
}