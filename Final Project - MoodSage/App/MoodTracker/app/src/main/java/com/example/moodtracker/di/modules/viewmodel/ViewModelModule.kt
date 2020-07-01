package com.example.moodtracker.di.modules.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moodtracker.ui.home.HomeViewModel
import com.example.moodtracker.ui.home.calendar.CalendarViewModel
import com.example.moodtracker.ui.home.dashboard.DashboardViewModel
import com.example.moodtracker.ui.home.newmood.NewMoodViewModel
import com.example.moodtracker.ui.home.newthought.NewThoughtViewModel
import com.example.moodtracker.ui.home.settings.SettingsViewModel
import com.example.moodtracker.ui.home.settings.links.LinkViewModel
import com.example.moodtracker.ui.home.settings.profile.ProfileViewModel
import com.example.moodtracker.ui.home.worries.WorryDiaryViewModel
import com.example.moodtracker.ui.home.worries.details.WorryDetailsViewModel
import com.example.moodtracker.ui.home.worries.solutions.NewSolutionViewModel
import com.example.moodtracker.ui.login.LoginViewModel
import com.example.moodtracker.ui.login.loading.LoadingViewModel
import com.example.moodtracker.ui.register.RegisterViewModel
import com.example.moodtracker.ui.splashscreen.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindRegister(myViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindLogin(myViewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplash(myViewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WorryDiaryViewModel::class)
    abstract fun bindWorry(myViewModel: WorryDiaryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindDashboard(myViewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettings(myViewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewMoodViewModel::class)
    abstract fun bindNewMood(myViewModel: NewMoodViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CalendarViewModel::class)
    abstract fun bindCalendar(myViewModel: CalendarViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LinkViewModel::class)
    abstract fun bindLinks(myViewModel: LinkViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHome(myViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewThoughtViewModel::class)
    abstract fun bindNewThought(myViewModel: NewThoughtViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoadingViewModel::class)
    abstract fun bindLoading(myViewModel: LoadingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfile(myViewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WorryDetailsViewModel::class)
    abstract fun bindWorryDetails(myViewModel: WorryDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewSolutionViewModel::class)
    abstract fun bindNewSolution(myViewModel: NewSolutionViewModel): ViewModel


    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory


}