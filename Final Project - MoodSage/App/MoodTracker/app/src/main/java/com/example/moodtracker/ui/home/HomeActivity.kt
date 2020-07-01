package com.example.moodtracker.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.moodtracker.MoodApplication
import com.example.moodtracker.R
import com.example.moodtracker.databinding.ActivityHomeBinding
import com.example.moodtracker.di.components.HomeComponent
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    lateinit var homeComponent: HomeComponent
    private lateinit var navController: NavController
    private lateinit var binding: ActivityHomeBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: HomeViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeComponent = (application as MoodApplication).appComponent.homeComponent().create()
            .also { it.inject(this) }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.viewModel = viewModel
        createNotificationChannel()
        initNavComponent()
        initObservers()
    }

    private fun initNavComponent() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val navView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navView.setupWithNavController(navController)
    }

    private fun initObservers() {
        viewModel.fabOpen.observe(this, Observer { open ->
            if (open) {
                openFab()
            } else {
                closeFab()
            }
        })

        binding.moodFab.setOnClickListener {
            navController.navigate(R.id.action_global_newMoodFragment)
            viewModel.onToggleFab()
        }

        binding.worryFab.setOnClickListener {
            navController.navigate(R.id.action_global_newThoughtFragment)
            viewModel.onToggleFab()
        }
    }

    private fun openFab() {
        val distance = resources.getDimensionPixelSize(R.dimen.fab_default_distance).toFloat()
        binding.moodFab.visibility = View.VISIBLE
        binding.worryFab.visibility = View.VISIBLE
        binding.moodFab.animate().translationYBy(-distance)
        binding.worryFab.animate().translationYBy(-distance * 2)
        binding.fab.animate().rotationBy(45f)
    }

    private fun closeFab() {

        binding.moodFab.animate().translationY(0f).withEndAction {
            binding.moodFab.visibility = View.GONE
        }
        binding.worryFab.animate().translationY(0f).withEndAction {
            binding.worryFab.visibility = View.GONE
        }

        binding.fab.animate().rotation(0f)
    }

    //Api 26 and above requires a notification channel, create it here
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "MoodsageReminderChannel",
                "Daily Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                enableLights(true)
                lightColor = Color.MAGENTA
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)


        }
    }
}
