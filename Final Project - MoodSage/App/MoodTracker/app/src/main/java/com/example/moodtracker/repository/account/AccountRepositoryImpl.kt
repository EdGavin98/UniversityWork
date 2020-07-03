package com.example.moodtracker.repository.account

import com.example.moodtracker.data.database.models.entities.User
import com.example.moodtracker.data.network.ApiCallState
import com.example.moodtracker.data.network.models.LinkDto
import com.example.moodtracker.data.network.models.UserRegisterDto
import com.example.moodtracker.data.sharedprefs.SharedPreferencesManager
import com.example.moodtracker.repository.Resource
import com.example.moodtracker.utils.mappers.convertToUserDto
import com.example.moodtracker.utils.mappers.convertToUserEntity
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val remote: AccountNetwork,
    private val sp: SharedPreferencesManager,
    private val local: AccountLocal
) : AccountRepository {


    override suspend fun getCurrentAccount(): Resource<User> {
        val user = local.getAccount(sp.getUser())

        return if (user != null) {
            Resource.success(user)
        } else {
            Resource.failure("No user", null)
        }
    }

    override suspend fun acceptLink(id: String): Resource<Unit> {
        return remote.acceptLink(id, "Bearer ${sp.getToken()}").run {
            when (this.state) {
                ApiCallState.SUCCESS -> Resource.success(Unit)
                ApiCallState.ERROR -> Resource.failure("Error accepting link", null)
                ApiCallState.NETWORK_ERROR -> Resource.failure("Network error", null)
            }
        }
    }

    override suspend fun removeLink(id: String): Resource<Unit> {
        return remote.removeLink(id, "Bearer ${sp.getToken()}").run {
            when (this.state) {
                ApiCallState.SUCCESS -> Resource.success(Unit)
                ApiCallState.ERROR -> Resource.failure("Error removing link", null)
                ApiCallState.NETWORK_ERROR -> Resource.failure("Network error", null)
            }
        }
    }


    override suspend fun getLinks(): Resource<List<LinkDto>> {
        return remote.getLinks("Bearer ${sp.getToken()}").run {
            when (this.state) {
                ApiCallState.SUCCESS -> Resource.success(this.data!!)
                ApiCallState.ERROR -> Resource.failure("No links", emptyList())
                ApiCallState.NETWORK_ERROR -> Resource.failure("Network error", emptyList())
            }
        }
    }

    override fun getLoginStatus(): Resource<Boolean> {
        return Resource.success(sp.getToken() != "None")
    }

    override suspend fun login(email: String, password: String): Resource<Unit> {
        return remote.login(email, password).run {
            when (this.state) {
                ApiCallState.SUCCESS -> getUserDetailsAndValidate(this.data!!.token)
                ApiCallState.ERROR -> Resource.failure("Invalid email/password", null)
                ApiCallState.NETWORK_ERROR -> Resource.failure("Network error", null)
            }
        }
    }

    override suspend fun logout(): Resource<Unit> {
        return try {
            local.deleteAccount(sp.getUser())
            sp.clearAll()
            Resource.success(Unit)
        } catch (e: Exception) {
            Resource.failure("Error logging out", null)
        }

    }

    override suspend fun deleteAccount(): Resource<Unit> {
        return remote.deleteAccount("Bearer ${sp.getToken()}").run {
            when (this.state) {
                ApiCallState.SUCCESS -> Resource.success(Unit)
                ApiCallState.ERROR -> Resource.failure(
                    "There was an error deleting your account",
                    null
                )
                ApiCallState.NETWORK_ERROR -> Resource.failure(
                    "Network Error: A network connection is required to delete an account",
                    null
                )
            }
        }
    }

    override suspend fun createAccount(userDto: UserRegisterDto): Resource<Unit> {
        return remote.createAccount(userDto).run {
            when (this.state) {
                ApiCallState.SUCCESS -> Resource.success(Unit)
                ApiCallState.ERROR -> {
                    if (this.code == 409) {
                        Resource.failure("Invalid details, this account may already exist", null)
                    } else {
                        Resource.failure("Server error, please try again later", null)
                    }
                }
                ApiCallState.NETWORK_ERROR -> Resource.failure("Network error", null)
            }
        }
    }

    override suspend fun updateAccount(user: User): Resource<Unit> {
        return remote.updateAccount(user.convertToUserDto(), "Bearer ${sp.getToken()}").run {
            when (state) {
                ApiCallState.SUCCESS -> {
                    local.updateAccount(user)
                    Resource.success(Unit)
                }
                ApiCallState.ERROR -> Resource.failure("Server error: please try again later", null)
                ApiCallState.NETWORK_ERROR -> Resource.failure(
                    "Network Error: A network connection is required to update your account",
                    null
                )
            }
        }
    }

    // Private helper functions

    /***
     * Helper function to validate that the user exists and is of the correct role
     * @param token - Token received from the login attempt
     */
    private suspend fun getUserDetailsAndValidate(token: String): Resource<Unit> {
        return remote.getAccount("Bearer $token").run {
            when (this.state) {

                ApiCallState.SUCCESS -> {
                    sp.addToken(token)
                    sp.addUser(this.data!!.id)
                    local.addAccount(this.data.convertToUserEntity())
                    Resource.success(Unit)
                }

                ApiCallState.ERROR -> {
                    if (this.code!! == -1) {
                        Resource.failure("Incorrect user type", null)
                    } else {
                        Resource.failure("Error retrieving user from server", null)
                    }
                }

                ApiCallState.NETWORK_ERROR -> Resource.failure("Network error", null)

            }
        }
    }
}