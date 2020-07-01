package com.example.moodtracker.ui.home.settings.profile

import android.util.Patterns.EMAIL_ADDRESS
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodtracker.data.database.models.entities.User
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.account.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val accountRepo: AccountRepository) :
    ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    val currentUser = MutableLiveData<User>()

    init {
        viewModelScope.launch {
            accountRepo.getCurrentAccount().run {
                when (status) {
                    Status.SUCCESS -> currentUser.postValue(data!!)
                    Status.FAILURE -> TODO("Add error handling for this, shouldn't ever happen, but can't be too safe")
                }
            }
        }
    }

    fun onMessageDisplayed() {
        _message.value = ""
    }

    fun onSubmitChanges() {
        if (validateChanges()) {
           updateData()
        }
    }

    private fun validateChanges(): Boolean {
        val user = currentUser.value!!
        if (!user.email.matches(EMAIL_ADDRESS.toRegex())) {
            _message.value = "Email address is invalid"
            return false
        }

        if (user.forename.isBlank() || user.surname.isBlank()) {
            _message.value = "Invalid name"
            return false
        }

        return true
    }

    private fun updateData() {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepo.updateAccount(currentUser.value!!).run {
                when (status) {
                    Status.SUCCESS -> _message.postValue("Account successfully updated")
                    Status.FAILURE -> _message.postValue(message)
                }
            }
        }
    }


}