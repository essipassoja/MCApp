package com.mcapp.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.mcapp.data.entity.Reminder
import com.mcapp.ui.reminder.ReminderViewModel
import org.koin.java.KoinJavaComponent.getKoin
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val viewModel = getKoin().get<ReminderViewModel>()
        val reminderId = inputData.getLong("reminderId", -1)

        Log.d("WORKER REMINDER ID", "$reminderId")

        if (reminderId != -1L) {
            viewModel.getReminder(reminderId) { reminder ->
                reminder?.let {
                    createNotification(applicationContext, reminder)
                }
            }
        }
        return Result.success()
    }
}

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("INSIDE FUN", "Inside onReceive!")
        val reminderId = intent?.getLongExtra("reminderId", -1) ?: -1
        val notificationId = intent?.getIntExtra("notificationId", -1) ?: -1

        Log.d("REMINDER ID", "$reminderId")
        Log.d("NOTIFICATION ID", "$notificationId")

        if (reminderId != -1L && notificationId != -1) {
            val viewModel = getKoin().get<ReminderViewModel>()
            viewModel.getReminder(reminderId) { reminder ->
                reminder?.let {
                    Log.d("REMINDER IT", "$it")
                    if (!it.reminderSeen) {
                        it.reminderSeen = true
                        updateReminderInDatabase(viewModel, it)
                    }
                }
            }

            // Remove the notification
            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId)
        }
    }

    private fun updateReminderInDatabase(
        viewModel: ReminderViewModel,
        reminder: Reminder
    ) {
        Log.d("UPDATED REMINDER", "$reminder")
        viewModel.insertOrUpdateReminder(reminder)
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
        .setInputData(Data.Builder()
            .putLong("reminderId", reminder.reminderId)
            .putInt("notificationId", reminder.reminderId.toInt())
            .build()
        )
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .build()

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
    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("reminderId", reminder.reminderId)
        putExtra("notificationId", reminder.reminderId.toInt())
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context, reminder.reminderId.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Create the notification
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.btn_star_big_on)
        .setContentTitle("MCApp")
        .setContentText(reminder.message)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    // Show the notification
    notificationManager.notify(reminder.reminderId.toInt(), builder.build())
}