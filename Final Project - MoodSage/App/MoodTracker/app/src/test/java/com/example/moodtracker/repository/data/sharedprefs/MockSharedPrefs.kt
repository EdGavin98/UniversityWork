package com.example.moodtracker.repository.data.sharedprefs

import com.example.moodtracker.data.sharedprefs.SharedPreferencesManager

class MockSharedPrefs : SharedPreferencesManager {

    private var token = "TestToken"
    private var user = "id"


    override fun addToken(token: String) {
        this.token = token
    }

    override fun deleteToken() {
        this.token = "None"
    }

    override fun getToken(): String {
        return this.token
    }

    override fun addUser(id: String) {
        this.user = id
    }

    override fun deleteUser() {
        this.user = "None"
    }

    override fun getUser(): String {
        return this.user
    }

    override fun clearAll() {
        deleteUser()
        deleteToken()
    }

    override fun getBooleanPreference(item: String): Boolean {
        return item == "notifications"
    }

    override fun getStringPreference(item: String): String {
        return "StringPreference"
    }

}