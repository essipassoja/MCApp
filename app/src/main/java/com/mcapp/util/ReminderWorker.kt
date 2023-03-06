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
import com.mcapp.ui.MainActivity
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
        val reminderId = inputData.getLong("reminderId", 0)
        val notificationId = inputData.getInt("notificationId", -1)

        Log.d("WORKER REMINDER ID", "$reminderId")

        if (reminderId != 0L && notificationId != -1) {
            viewModel.getReminder(reminderId) { reminder ->
                reminder?.let {
                    createNotification(applicationContext, reminder, notificationId)
                }
            }
        }
        return Result.success()
    }
}

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationId = intent?.getIntExtra("notificationId", -1) ?: -1

        Log.d("NOTIFICATION ID", "$notificationId")

        if (notificationId != -1) {
            val homeIntent = Intent(context, MainActivity::class.java)
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(homeIntent)

            // Remove the notification
            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId)
        }
    }
}

fun makeReminderRequest(
    context: Context,
    reminder: Reminder,
) {
    val timeZone = ZoneId.systemDefault()
    val notificationIds = List(reminder.reminderTimes.size) { Random().nextInt() }

    for ((index, reminderTime) in reminder.reminderTimes.withIndex()) {
        val zonedDateTime = reminderTime.atZone(timeZone)
        val reminderTimeInMillis = zonedDateTime.toInstant().toEpochMilli()

        val now = Calendar.getInstance().timeInMillis
        val delay = reminderTimeInMillis - now

        val reminderRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInputData(Data.Builder()
                .putLong("reminderId", reminder.reminderId)
                .putInt("notificationId", notificationIds[index])
                .build()
            )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(reminderRequest)
    }
}

@SuppressLint("LaunchActivityFromNotification", "UnspecifiedImmutableFlag")
fun createNotification(context: Context, reminder: Reminder, notificationId: Int) {
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
        putExtra("notificationId", notificationId)
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
    notificationManager.notify(notificationId, builder.build())
}