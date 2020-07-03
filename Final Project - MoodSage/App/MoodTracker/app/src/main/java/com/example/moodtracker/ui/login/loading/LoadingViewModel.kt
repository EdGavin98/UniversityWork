package com.example.moodtracker.ui.login.loading

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.account.AccountRepository
import com.example.moodtracker.repository.mood.MoodRepository
import com.example.moodtracker.repository.worry.WorryRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoadingViewModel @Inject constructor(
    private val moodRepo: MoodRepository,
    private val worryRepo: WorryRepository,
    private val accountRepo: AccountRepository
) : ViewModel() {

    private val _loadingStatus = MutableLiveData<LoadingState>(LoadingState.LOADING)
    val loadingStatus: LiveData<LoadingState>
        get() = _loadingStatus

    init {
        viewModelScope.launch {
            moodRepo.getLatestMoods().run {
                when (status) {
                    Status.SUCCESS -> getWorries()
                    Status.FAILURE -> {
                        accountRepo.logout()
                        _loadingStatus.postValue(LoadingState.ERROR)
                    }
                }
            }
        }
    }

    private suspend fun getWorries() {
        worryRepo.getLatestWorries().run {
            when (status) {
                Status.SUCCESS -> _loadingStatus.postValue(LoadingState.FINISHED)
                Status.FAILURE -> {
                    _loadingStatus.postValue(LoadingState.ERROR)
                }
            }
        }
    }

    enum class LoadingState {
        LOADING,
        FINISHED,
        ERROR
    }
}