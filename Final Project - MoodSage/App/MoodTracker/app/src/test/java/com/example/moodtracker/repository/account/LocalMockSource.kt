package com.example.moodtracker.repository.account

import com.example.moodtracker.data.database.models.entities.User

class LocalMockSource : AccountLocal {

    private var accounts: MutableList<User> = mutableListOf(
        User(
            id = "id",
            forename = "Test",
            surname = "Test",
            email = "test@test.com"
        )
    )
    override suspend fun addAccount(user: User) {
        accounts.add(user)
    }

    override suspend fun getAccount(id: String): User? {
        return accounts.find {
            it.id == id
        }
    }

    override suspend fun updateAccount(user: User) {
        accounts[0] = user
    }

    override suspend fun deleteAccount(id: String) {
        accounts.removeIf {
            it.id == id
        }
    }
}