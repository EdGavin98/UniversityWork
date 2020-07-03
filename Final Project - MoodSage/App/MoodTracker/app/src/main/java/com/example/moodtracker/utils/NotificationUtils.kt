package com.example.moodtracker.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.moodtracker.R
import com.example.moodtracker.ui.splashscreen.MainActivity

private const val CHANNEL = "MoodsageReminderChannel"
private const val ID = 1

/***
 * Extension function to send a reminder notification
 * @param context The application context for the notification
 */
fun NotificationManager.sendReminder(context: Context) {

    //Create intent, notification when launched
    //Using MainActivity instead of home just in case the user is not logged in and the notification wasn't cancelled correctly
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        context,
        1,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(context, CHANNEL)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(context.getString(R.string.notification_title))
        .setContentText(context.getString(R.string.notification_body))
        .setStyle(NotificationCompat.BigTextStyle()
            .bigText(context.getString(R.string.notification_body))
        )
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    notify(ID, builder.build())

}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}
