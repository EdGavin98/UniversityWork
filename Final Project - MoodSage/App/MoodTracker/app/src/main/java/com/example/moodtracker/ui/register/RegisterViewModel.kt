package com.example.moodtracker.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodtracker.data.network.models.UserRegisterDto
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.account.AccountRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel @Inject constructor(private val repo: AccountRepository) : ViewModel() {

    var forename = MutableLiveData<String>("")
    var surname = MutableLiveData<String>("")
    var email = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")
    var passwordConfirm = MutableLiveData<String>("")

    private var _registrationSuccessful = MutableLiveData<Boolean>()
    val registrationSuccessful: LiveData<Boolean>
        get() = _registrationSuccessful

    private var _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message


    fun onRegister() {
        val user = UserRegisterDto(
            forename.value!!.trim(),
            surname.value!!.trim(),
            password.value!!.trim(),
            email.value!!.trim()
        )
        if (validateDetails(user)) {
            registerUser(user)
        } else {
            _registrationSuccessful.value = false
        }
    }

    private fun validateDetails(user: UserRegisterDto): Boolean {
            if (user.forename.isBlank() || user.surname.isBlank()) {
                _message.value = "Please enter a name"
                return false
            }

            val emailRegex = android.util.Patterns.EMAIL_ADDRESS.toRegex()
            if (!user.email.matches(emailRegex)) {
                _message.value = "Invalid email address"
                return false
            }

            if (user.password.isBlank()) {
                _message.value = "Please enter a password"
                return false
            }

            if (user.password != passwordConfirm.value) {
                _message.value = "Passwords do not match"
                return false
            }

            val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}$")
            if (!user.password.matches(passwordRegex)) {
                _message.value = "Invalid password"
                return false
            }

        return true

    }

    private fun registerUser(user: UserRegisterDto) {
        viewModelScope.launch {
            repo.createAccount(user).run {
                when (status) {
                    Status.SUCCESS -> {
                        _registrationSuccessful.postValue(true)
                        _message.postValue("Account creation successful")

                    }
                    Status.FAILURE -> {
                        _registrationSuccessful.postValue(false)
                        _message.postValue(this.message)
                    }
                }
            }
        }
    }

}