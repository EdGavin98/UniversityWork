package com.example.moodtracker.ui.splashscreen

import androidx.lifecycle.ViewModel
import com.example.moodtracker.repository.account.AccountRepository
import javax.inject.Inject

class SplashViewModel @Inject constructor(val accountRepo: AccountRepository) : ViewModel() {

    fun isLoggedIn(): Boolean {
        return accountRepo.getLoginStatus().data ?: false
    }

}