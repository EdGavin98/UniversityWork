package com.example.moodtracker.di.components

import com.example.moodtracker.di.ActivityScope
import com.example.moodtracker.ui.home.HomeActivity
import com.example.moodtracker.ui.home.calendar.CalendarFragment
import com.example.moodtracker.ui.home.dashboard.DashboardFragment
import com.example.moodtracker.ui.home.newmood.NewMoodFragment
import com.example.moodtracker.ui.home.newthought.NewThoughtFragment
import com.example.moodtracker.ui.home.settings.SettingsFragment
import com.example.moodtracker.ui.home.settings.links.LinkFragment
import com.example.moodtracker.ui.home.settings.profile.ProfileFragment
import com.example.moodtracker.ui.home.worries.WorryDiaryFragment
import com.example.moodtracker.ui.home.worries.details.WorryDetailsFragment
import com.example.moodtracker.ui.home.worries.solutions.NewSolutionFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface HomeComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeComponent
    }

    fun inject(activity: HomeActivity)
    fun inject(fragment: WorryDiaryFragment)
    fun inject(fragment: NewMoodFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: DashboardFragment)
    fun inject(fragment: CalendarFragment)
    fun inject(fragment: LinkFragment)
    fun inject(fragment: NewThoughtFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: WorryDetailsFragment)
    fun inject(fragment: NewSolutionFragment)

}