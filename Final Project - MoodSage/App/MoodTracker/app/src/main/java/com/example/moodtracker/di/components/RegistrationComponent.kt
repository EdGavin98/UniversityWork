package com.example.moodtracker.di.components

import com.example.moodtracker.di.ActivityScope
import com.example.moodtracker.ui.register.RegisterActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface RegistrationComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RegistrationComponent
    }

    fun inject(activity: RegisterActivity)
}
