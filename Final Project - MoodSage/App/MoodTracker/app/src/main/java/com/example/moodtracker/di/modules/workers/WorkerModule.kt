package com.example.moodtracker.di.modules.workers

import androidx.work.WorkerFactory
import com.example.moodtracker.workers.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(MoodUploadWorker::class)
    abstract fun bindMoodUploadWorker(factory: MoodUploadWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(NotificationWorker::class)
    abstract fun bindNotificationWorker(factory: NotificationWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(WorryUploadWorker::class)
    abstract fun bindWorryUploadWorker(factory: WorryUploadWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(SolutionUploadWorker::class)
    abstract fun bindSolutionUploadWorker(factory: SolutionUploadWorker.Factory): ChildWorkerFactory

    @Binds
    abstract fun provideFactory(factory: CustomWorkerFactory): WorkerFactory
}