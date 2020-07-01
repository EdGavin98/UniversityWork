package com.example.moodtracker.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.mood.MoodRepository
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

/**
 * WorkManager to handle uploading of any moods, will wait and run when a network connection is available
 */
class MoodUploadWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val moodRepo: MoodRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val date = inputData.getString("MOOD_DATE").let { dateString ->
            LocalDateTime.parse(dateString)
        }

        return moodRepo.syncMood(date).run {
            when (status) {
                Status.SUCCESS -> Result.success()

                Status.FAILURE -> {
                    if (message == "No Mood") {
                        Result.failure()
                    } else {
                        Result.retry()
                    }
                }
            }
        }
    }

    class Factory @Inject constructor(
        private val moodRepo: MoodRepository
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): CoroutineWorker {
            return MoodUploadWorker(
                appContext,
                params,
                moodRepo
            )
        }
    }

}