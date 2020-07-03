package com.example.moodtracker.di

import android.content.Context
import com.example.moodtracker.MoodApplication
import com.example.moodtracker.di.components.AppSubcomponents
import com.example.moodtracker.di.components.HomeComponent
import com.example.moodtracker.di.components.LoginComponent
import com.example.moodtracker.di.components.RegistrationComponent
import com.example.moodtracker.di.modules.NetworkModule
import com.example.moodtracker.di.modules.RepositoryModule
import com.example.moodtracker.di.modules.StorageModule
import com.example.moodtracker.di.modules.viewmodel.ViewModelModule
import com.example.moodtracker.di.modules.workers.WorkerModule
import com.example.moodtracker.ui.splashscreen.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
        StorageModule::class,
        WorkerModule::class,
        AppSubcomponents::class]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(application: MoodApplication)
    fun inject(activity: MainActivity)
    fun registrationComponent(): RegistrationComponent.Factory
    fun loginComponent(): LoginComponent.Factory
    fun homeComponent(): HomeComponent.Factory

}