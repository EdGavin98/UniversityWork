package com.example.moodtracker.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.account.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val accountRepo: AccountRepository) : ViewModel() {

    private var _registerPressed = MutableLiveData<Boolean>()
    val registerPressed: LiveData<Boolean>
        get() = _registerPressed

    private var _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean>
        get() = _loginStatus

    private var _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    var username = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")

    var alreadyProcessing: Semaphore = Semaphore(1)

    fun onLoginPressed() {
        if (alreadyProcessing.tryAcquire()) {
            login()
        }
    }

    fun onRegisterPressed() {
        _registerPressed.value = true
    }

    fun onRegisterPressedComplete() {
        _registerPressed.value = false
    }

    fun onLoginComplete() {
        _loginStatus.value = false
    }

    private fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepo.login(username.value!!.trim(), password.value!!.trim()).run {
                when (this.status) {
                    Status.SUCCESS -> {
                        _loginStatus.postValue(true)
                    }

                    Status.FAILURE -> {
                        _loginStatus.postValue(false)
                        _message.postValue(this.message)
                    }
                }
            }
            alreadyProcessing.release()
        }
    }

}