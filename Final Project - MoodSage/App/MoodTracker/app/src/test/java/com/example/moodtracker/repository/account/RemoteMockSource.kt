package com.example.moodtracker.repository.account

import com.example.moodtracker.data.network.ApiCall
import com.example.moodtracker.data.network.models.LinkDto
import com.example.moodtracker.data.network.models.TokenDto
import com.example.moodtracker.data.network.models.UserDto
import com.example.moodtracker.data.network.models.UserRegisterDto

class RemoteMockSource : AccountNetwork {

    private val testEmail = "Test@User.com"
    private val testPassword = "TestPassword"
    private val testToken = "Bearer TestToken"
    private val testTokenReturnVersion = "TestToken"

    private val links : List<LinkDto> = listOf(
        LinkDto(
            status = 1,
            user = UserDto(
                id = "tid",
                forename = "bob",
                surname = "therapist",
                email = "test@therapist.com",
                role = "therapist"
            )
        )
    )

    override suspend fun login(email: String, password: String): ApiCall<TokenDto> {
        return if (email == testEmail && password == testPassword) {
            ApiCall.success(TokenDto(testTokenReturnVersion), 200)
        } else {
            ApiCall.error(400)
        }
    }

    override suspend fun createAccount(userDto: UserRegisterDto): ApiCall<Unit> {
        return if (userDto.email != testEmail) {
            ApiCall.success(Unit, 201)
        } else {
            ApiCall.error(409)
        }
    }

    override suspend fun updateAccount(user: UserDto, token: String): ApiCall<Unit> {
        return if (token == testToken) {
            ApiCall.success(Unit, 204)
        } else {
            ApiCall.error(404)
        }
    }

    override suspend fun getAccount(token: String): ApiCall<UserDto> {
        return if (token == testToken) {
            ApiCall.success(UserDto(
                email = testEmail,
                forename = "Test",
                surname = "Test",
                id = "id"
            ), 200)
        } else {
            ApiCall.error(401)
        }
    }

    override suspend fun deleteAccount(token: String): ApiCall<Unit> {
        return if (token == testToken) {
            ApiCall.success(Unit, 204)
        } else {
            ApiCall.error(401)
        }
    }

    override suspend fun getLinks(token: String): ApiCall<List<LinkDto>> {
        return if (token == testToken) {
            ApiCall.success(links, 200)
        } else {
            ApiCall.error(401)
        }
    }

    override suspend fun acceptLink(id: String, token: String): ApiCall<Unit> {
        return if (token == testToken) {
            if (id == links[0].user.id) {
                ApiCall.success(Unit, 201)
            } else {
                ApiCall.error(404)
            }
        } else {
            ApiCall.error(401)
        }
    }

    override suspend fun removeLink(id: String, token: String): ApiCall<Unit> {
        return if (links[0].user.id == "tid" && token == testToken) {
            if (id == links[0].user.id) {
                ApiCall.success(Unit, 204)
            } else {
                ApiCall.error(404)
            }
        } else {
            ApiCall.error(401)
        }
    }
}