package com.mcapp.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mcapp.data.entity.Reminder
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters) :
    Worker(context, workerParams
    ) {
    override fun doWork(): Result {
        return Result.success()
    }
}

fun makeReminderRequest(
    context: Context,
    reminder: Reminder,
) {
    val timeZone = ZoneId.systemDefault()
    val zonedDateTime = reminder.reminderTime.atZone(timeZone)
    val reminderTimeInMillis = zonedDateTime.toInstant().toEpochMilli()

    val now = Calendar.getInstance().timeInMillis
    val delay = reminderTimeInMillis - now

    val reminderRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .build()

    createNotification(context, reminder)

    WorkManager.getInstance(context).enqueue(reminderRequest)
}

@SuppressLint("LaunchActivityFromNotification", "UnspecifiedImmutableFlag")
fun createNotification(context: Context, reminder: Reminder) {
    val channelId = "default_channel_id"
    val channelName = "Default Channel"
    val importance = NotificationManager.IMPORTANCE_HIGH
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val channel =
        NotificationChannel(channelId, channelName, importance).apply {
            description = "Channel description"
        }
    notificationManager.createNotificationChannel(channel)

    // Create an intent that will be triggered when the user taps on the notification
    val intent = Intent(context, ReminderWorker::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Create the notification
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.btn_star_big_on)
        .setContentTitle("MCApp")
        .setContentText(reminder.message)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    // Show the notification
    val notificationId = 1
    notificationManager.notify(notificationId, builder.build())
}