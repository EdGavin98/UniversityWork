package com.example.moodtracker.di.components

import com.example.moodtracker.di.ActivityScope
import com.example.moodtracker.ui.login.LoginActivity
import com.example.moodtracker.ui.login.LoginFragment
import com.example.moodtracker.ui.login.loading.LoadingFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface LoginComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): LoginComponent
    }

    fun inject(activity: LoginActivity)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: LoadingFragment)
}