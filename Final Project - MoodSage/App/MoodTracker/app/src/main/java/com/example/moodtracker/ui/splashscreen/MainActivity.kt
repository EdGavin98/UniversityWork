package com.example.moodtracker.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.moodtracker.MoodApplication
import com.example.moodtracker.R
import com.example.moodtracker.di.modules.viewmodel.ViewModelFactory
import com.example.moodtracker.ui.home.HomeActivity
import com.example.moodtracker.ui.login.LoginActivity
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: SplashViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MoodApplication).appComponent.inject(this)
        if (viewModel.isLoggedIn()) {
            Intent(this, HomeActivity::class.java).also {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(it)
            }
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        setContentView(R.layout.activity_main)
    }
}
