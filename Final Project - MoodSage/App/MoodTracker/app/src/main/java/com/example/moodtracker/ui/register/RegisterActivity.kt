package com.example.moodtracker.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.moodtracker.MoodApplication
import com.example.moodtracker.R
import com.example.moodtracker.databinding.ActivityRegisterBinding
import com.example.moodtracker.di.components.RegistrationComponent
import com.example.moodtracker.di.modules.viewmodel.ViewModelFactory
import com.example.moodtracker.ui.login.LoginActivity
import javax.inject.Inject

class RegisterActivity : AppCompatActivity() {

    private lateinit var registrationComponent: RegistrationComponent

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: RegisterViewModel by viewModels { viewModelFactory }
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registrationComponent =
            (application as MoodApplication).appComponent.registrationComponent().create()
                .also { it.inject(this) }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.viewModel = viewModel
        initObservers()
    }

    private fun initObservers() {
        viewModel.registrationSuccessful.observe(this, Observer { success ->
            if (success) {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        })

        viewModel.message.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
    }
}
