package com.example.moodtracker

import android.app.Application
import androidx.preference.PreferenceManager
import androidx.work.Configuration
import com.example.moodtracker.di.AppComponent
import com.example.moodtracker.di.DaggerAppComponent
import com.example.moodtracker.di.modules.workers.CustomWorkerFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import javax.inject.Inject

open class MoodApplication : Application(), Configuration.Provider {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var workerFactory: CustomWorkerFactory

    override fun onCreate() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(applicationContext)
        appComponent.inject(this)
        AndroidThreeTen.init(this)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}