package com.example.moodtracker.workers

import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.moodtracker.data.sharedprefs.SharedPreferencesManager
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.mood.MoodRepository
import com.example.moodtracker.utils.sendReminder
import org.threeten.bp.LocalDate
import javax.inject.Inject

class NotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val sp : SharedPreferencesManager,
    private val moodRepo : MoodRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return if (sp.getBooleanPreference("notifications")) {
            checkForMoodAndNotify()
        } else {
            Result.success()
        }
    }

    private suspend fun checkForMoodAndNotify(): Result {
        return moodRepo.getMoodByDate(LocalDate.now().atStartOfDay()).run {
            when(status) {
                Status.SUCCESS -> {
                    Result.success()
                }
                Status.FAILURE -> {
                    val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.sendReminder(applicationContext)
                    Result.success()
                }
            }
        }
    }

    class Factory @Inject constructor(
        private val sp: SharedPreferencesManager,
        private val moodRepo : MoodRepository
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): CoroutineWorker {
            return NotificationWorker(
                appContext,
                params,
                sp,
                moodRepo
            )
        }
    }

}