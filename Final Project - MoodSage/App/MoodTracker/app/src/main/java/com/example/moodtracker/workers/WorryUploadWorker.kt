package com.example.moodtracker.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.worry.WorryRepository
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

/**
 * WorkManager to handle uploading of any moods, will wait and run when a network connection is available
 */
class WorryUploadWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val worryRepo: WorryRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val date = inputData.getString("WORRY_DATE").let { dateString ->
            LocalDateTime.parse(dateString)
        }

        return worryRepo.syncWorry(date).run {
            when (status) {
                Status.SUCCESS -> Result.success()

                Status.FAILURE -> {
                    if (message == "No Worry") {
                        Result.failure()
                    } else {
                        Result.retry()
                    }
                }
            }
        }
    }

    class Factory @Inject constructor(
        private val worryRepo: WorryRepository
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): CoroutineWorker {
            return WorryUploadWorker(
                appContext,
                params,
                worryRepo
            )
        }
    }

}