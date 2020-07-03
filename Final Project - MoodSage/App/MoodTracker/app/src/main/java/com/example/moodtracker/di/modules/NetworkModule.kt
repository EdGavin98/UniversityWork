package com.example.moodtracker.di.modules

import com.example.moodtracker.data.network.retrofit.AccountRetrofitService
import com.example.moodtracker.data.network.retrofit.MoodRetrofitService
import com.example.moodtracker.data.network.retrofit.SolutionRetrofitService
import com.example.moodtracker.data.network.retrofit.WorryRetrofitService
import com.example.moodtracker.utils.MoshiDateConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
object NetworkModule {

    @Provides
    @Reusable
    @JvmStatic
    fun provideRetrofitInstance(): Retrofit {
        val moshi = Moshi.Builder()
            .add(MoshiDateConverter())
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.stsoft.tech/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Reusable
    @JvmStatic
    fun provideAccountApi(retrofit: Retrofit): AccountRetrofitService {
        return retrofit.create(AccountRetrofitService::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    fun provideMoodApi(retrofit: Retrofit): MoodRetrofitService {
        return retrofit.create(MoodRetrofitService::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    fun provideWorryApi(retrofit: Retrofit): WorryRetrofitService {
        return retrofit.create(WorryRetrofitService::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    fun provideSolutionApi(retrofit: Retrofit): SolutionRetrofitService {
        return retrofit.create(SolutionRetrofitService::class.java)
    }
}