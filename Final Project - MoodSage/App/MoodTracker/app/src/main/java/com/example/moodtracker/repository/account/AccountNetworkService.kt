package com.example.moodtracker.repository.account

import com.example.moodtracker.data.network.ApiCall
import com.example.moodtracker.data.network.ApiCallState
import com.example.moodtracker.data.network.makeCall
import com.example.moodtracker.data.network.models.*
import com.example.moodtracker.data.network.retrofit.AccountRetrofitService
import javax.inject.Inject

class AccountNetworkService @Inject constructor(private val retrofit: AccountRetrofitService) :
    AccountNetwork {

    override suspend fun acceptLink(id: String, token: String): ApiCall<Unit> {
        return makeCall {
            retrofit.acceptLink(token, id, LinkAcceptDto(id))
        }
    }

    override suspend fun removeLink(id: String, token: String): ApiCall<Unit> {
        return makeCall {
            retrofit.removeLink(token, id)
        }
    }

    override suspend fun getLinks(token: String): ApiCall<List<LinkDto>> {
        return makeCall {
            retrofit.getLinks(token)
        }
    }

    override suspend fun getAccount(token: String): ApiCall<UserDto> {
        val res = makeCall {
            retrofit.getUser(token)
        }

        return if (res.state == ApiCallState.SUCCESS && res.data?.role ?: "" != "patient") {
            ApiCall.error(-1) //Wrong user type for the app
        } else {
            res
        }
    }

    override suspend fun deleteAccount(token: String): ApiCall<Unit> {
        return makeCall {
            retrofit.deleteAccount(token)
        }
    }

    override suspend fun login(email: String, password: String): ApiCall<TokenDto> {
        return makeCall {
            retrofit.loginUser(UserLoginDto(email, password))
        }
    }

    override suspend fun createAccount(userDto: UserRegisterDto): ApiCall<Unit> {
        return makeCall {
            retrofit.registerUser(userDto)
        }
    }

    override suspend fun updateAccount(user: UserDto, token: String): ApiCall<Unit> {
        return makeCall {
            retrofit.updateAccount(token, user)
        }
    }
}