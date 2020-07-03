package com.example.moodtracker.ui.home.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.account.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val accountRepo: AccountRepository,
    private val context: Context
) :
    ViewModel() {

    private var _logOutComplete = MutableLiveData<Boolean>()
    val logOutComplete: LiveData<Boolean>
        get() = _logOutComplete

    private var _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepo.logout()
            WorkManager.getInstance(context).cancelUniqueWork("NotificationWork")
            _logOutComplete.postValue(true)
        }
    }

    fun deleteAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepo.deleteAccount().run {
                when (status) {
                    Status.SUCCESS -> {
                        _message.postValue("Account deletion successful")
                        logout()
                    }

                    Status.FAILURE -> {
                        _message.postValue(this.message)
                    }
                }
            }
        }
    }
}