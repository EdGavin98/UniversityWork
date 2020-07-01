package com.example.moodtracker.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moodtracker.MoodApplication
import com.example.moodtracker.R
import com.example.moodtracker.di.components.LoginComponent


class LoginActivity : AppCompatActivity() {

    lateinit var loginComponent: LoginComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginComponent = (application as MoodApplication).appComponent.loginComponent().create()
            .also { it.inject(this) }
        setContentView(R.layout.activity_login)
    }

}
