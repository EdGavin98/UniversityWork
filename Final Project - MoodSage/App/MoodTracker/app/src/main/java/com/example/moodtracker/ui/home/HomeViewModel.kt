package com.example.moodtracker.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.moodtracker.workers.NotificationWorker
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val context : Context) : ViewModel() {

    private var _fabOpen = MutableLiveData<Boolean>(false)
    val fabOpen: LiveData<Boolean>
        get() = _fabOpen

    init {
        startNotificationWorkerIfNotRunning()
    }

    fun onToggleFab() {
        _fabOpen.value = !_fabOpen.value!!
    }

    private fun startNotificationWorkerIfNotRunning() {
        val now = LocalDateTime.now()
        val timeToWait = if (now.hour >= 23) {  //Want to launch at roughly five each day
            Duration.between(now, LocalDate.now().plusDays(1).atTime(17, 0)).toMinutes()
        } else {
            Duration.between(now, LocalDate.now().atTime(17, 0)).toMinutes()

        }

        val request = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(timeToWait, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "NotificationWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }

}