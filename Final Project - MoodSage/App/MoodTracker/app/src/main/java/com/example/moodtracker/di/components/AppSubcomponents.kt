package com.example.moodtracker.di.components

import dagger.Module

@Module(subcomponents = [HomeComponent::class, LoginComponent::class, RegistrationComponent::class])
class AppSubcomponents