package com.example.moodtracker.data.sharedprefs

import android.content.Context
import android.preference.PreferenceManager
import javax.inject.Inject

class SharedPreferencesManagerImpl @Inject constructor(context: Context) :
    SharedPreferencesManager {

    private val user = "user"
    private val token = "token"
    private val sp = context.getSharedPreferences("MoodTracker", Context.MODE_PRIVATE)
    private val options = PreferenceManager.getDefaultSharedPreferences(context)

    override fun addToken(token: String) {
        with(sp.edit()) {
            putString(this@SharedPreferencesManagerImpl.token, token)
            apply()
        }
    }

    override fun deleteToken() {
        with(sp.edit()) {
            remove(token)
            apply()
        }
    }

    override fun getToken(): String {
        return sp.getString(token, "None")!!
    }

    override fun addUser(id: String) {
        with(sp.edit()) {
            putString(user, id)
            apply()
        }
    }

    override fun deleteUser() {
        with(sp.edit()) {
            remove(user)
            apply()
        }
    }

    override fun getUser(): String {
        return sp.getString(user, "No User")!!
    }

    override fun clearAll() {
        sp.edit().clear().apply()
    }

    override fun getBooleanPreference(item: String): Boolean {
        return options.getBoolean(item, true)
    }

    override fun getStringPreference(item: String): String {
        return options.getString(item, "No Value")!!
    }
}