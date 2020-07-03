package com.example.moodtracker.di.modules

import com.example.moodtracker.data.sharedprefs.SharedPreferencesManager
import com.example.moodtracker.data.sharedprefs.SharedPreferencesManagerImpl
import com.example.moodtracker.repository.account.AccountNetwork
import com.example.moodtracker.repository.account.AccountNetworkService
import com.example.moodtracker.repository.account.AccountRepository
import com.example.moodtracker.repository.account.AccountRepositoryImpl
import com.example.moodtracker.repository.mood.MoodNetwork
import com.example.moodtracker.repository.mood.MoodNetworkService
import com.example.moodtracker.repository.mood.MoodRepository
import com.example.moodtracker.repository.mood.MoodRepositoryImpl
import com.example.moodtracker.repository.solution.SolutionNetwork
import com.example.moodtracker.repository.solution.SolutionNetworkService
import com.example.moodtracker.repository.solution.SolutionRepository
import com.example.moodtracker.repository.solution.SolutionRepositoryImpl
import com.example.moodtracker.repository.worry.WorryNetwork
import com.example.moodtracker.repository.worry.WorryNetworkService
import com.example.moodtracker.repository.worry.WorryRepository
import com.example.moodtracker.repository.worry.WorryRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideAccountRepo(accountRepository: AccountRepositoryImpl): AccountRepository

    @Binds
    abstract fun provideAccountService(accountNetworkService: AccountNetworkService): AccountNetwork

    @Binds
    abstract fun provideMoodRepo(moodRepository: MoodRepositoryImpl): MoodRepository

    @Binds
    abstract fun provideMoodService(moodNetworkService: MoodNetworkService): MoodNetwork

    @Binds
    abstract fun provideWorryRepo(worryRepository: WorryRepositoryImpl): WorryRepository

    @Binds
    abstract fun provideWorryService(worryNetworkService: WorryNetworkService): WorryNetwork

    @Binds
    abstract fun provideSolutionRepo(solutionRepository: SolutionRepositoryImpl): SolutionRepository

    @Binds
    abstract fun provideSolutionService(solutionNetworkService: SolutionNetworkService): SolutionNetwork

    @Binds
    abstract fun provideSharedPreferences(sp: SharedPreferencesManagerImpl): SharedPreferencesManager

}