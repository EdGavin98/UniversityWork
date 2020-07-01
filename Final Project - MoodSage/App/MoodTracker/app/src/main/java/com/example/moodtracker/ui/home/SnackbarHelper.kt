package com.example.moodtracker.ui.home

import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*

/***
 * Package level helper function to create a snackbar message
 * @param message - Message to be displayed to the user
 * @param activity - Activity context for snackbar
 */
fun makeSnackbar(message: String, activity: HomeActivity) {
    Snackbar.make(activity.homeCoordinator, message, Snackbar.LENGTH_SHORT).apply {
        anchorView = activity.bar
        show()
    }
}