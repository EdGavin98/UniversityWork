package com.example.moodtracker.ui.home.settings

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.moodtracker.R
import com.example.moodtracker.ui.home.HomeActivity
import com.example.moodtracker.ui.home.makeSnackbar
import com.example.moodtracker.ui.login.LoginActivity
import javax.inject.Inject


class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        (activity!! as HomeActivity).homeComponent.inject(this)
        setPreferencesFromResource(R.xml.preferences, rootKey)
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        val profile = findPreference<Preference>("profile")
        profile?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_profileFragment)
            true
        }

        val links = findPreference<Preference>("links")
        links?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_linkFragment)
            true
        }

        val logOut = findPreference<Preference>("log_out")
        logOut?.setOnPreferenceClickListener {
            makeConfirmDialog(
                successFunction = viewModel::logout,
                title = "Confirm log out",
                message = "Are you sure you would like to log out? \n\nDoing this will clear the local files, unsynced data may be lost \n\n"
            )
            true
        }

        val delete = findPreference<Preference>("delete_account")
        delete?.setOnPreferenceClickListener {
            makeConfirmDialog(
                successFunction = viewModel::deleteAccount,
                title = "Confirm account deletion",
                message = "Are you sure you want to delete your account? \n\nDoing this will clear all of your data from our servers and is not reversible.\n\nYou must have a network connection to proceed"
            )
            true
        }

    }

    private fun initObservers() {
        viewModel.logOutComplete.observe(this, Observer { loggedOut ->
            if (loggedOut) {
                Intent(requireContext(), LoginActivity::class.java).also {
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(it)
                }
            }
        })

        viewModel.message.observe(this, Observer { message ->
            if (message.contains("successful")) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
                    .show() //Use toast here as user will most likely be taken to the home screen before the snackbar can be shown
            } else {
                makeSnackbar(message, (requireActivity() as HomeActivity))
            }
        })
    }


    private fun makeConfirmDialog(successFunction : () -> Unit, message: String, title : String) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("Yes") { dialog, id ->
                successFunction()
            }
            setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }
            show()
        }
    }
}