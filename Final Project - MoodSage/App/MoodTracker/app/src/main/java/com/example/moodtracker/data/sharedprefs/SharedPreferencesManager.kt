package com.example.moodtracker.data.sharedprefs

interface SharedPreferencesManager {

    fun addToken(token: String)
    fun deleteToken()
    fun getToken(): String
    fun addUser(id: String)
    fun deleteUser()
    fun getUser(): String
    fun clearAll()

    fun getBooleanPreference(item : String): Boolean
    fun getStringPreference(item : String): String

}