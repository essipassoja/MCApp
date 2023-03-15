package com.mcapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.google.android.gms.maps.model.LatLng
import com.mcapp.data.entity.Reminder
import com.mcapp.data.repository.ReminderRepository
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent.getKoin
import java.util.concurrent.TimeUnit
import kotlin.math.*

class LocationNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams
) {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun doWork(): Result {
        val reminders = getRemindersFromDatabase()

        val currentLocation = getCurrentLocation()

        for (reminder in reminders) {
            val location = reminder.locationX?.let {
                reminder.locationY?.let {
                        it1 -> LatLng(it, it1)
                }
            }
            val distance = location?.let { calculateDistance(currentLocation, it) }
            Log.d("DISTANCE", "$distance")
            if (distance != null) {
                Log.d("DISTANCE", "$distance")
                if (distance <= 1000) {
                    showNotification(reminder)
                }
            }
        }

        return Result.success()
    }
    private fun getRemindersFromDatabase(): List<Reminder> {
        // Retrieve reminder locations from Room database
        val reminderRepository = getKoin().get<ReminderRepository>()

        var reminderList = listOf<Reminder>()

        runBlocking {
            reminderRepository.getAllReminders(0).collect { reminders ->
                reminderList = reminders
            }
        }
        Log.d("TEST", "$reminderList")
        return reminderList
    }

    private fun getCurrentLocation(): LatLng {
        // Set dummy location for app
        return LatLng(65.06, 25.47)
    }

    private fun calculateDistance(location1: LatLng, location2: LatLng): Double {
        // Calculate distance between two locations using Haversine formula
        val earthRadius = 6371000.0 // in meters
        val lat1 = Math.toRadians(location1.latitude)
        val lat2 = Math.toRadians(location2.latitude)
        val deltaLat = Math.toRadians(location2.latitude - location1.latitude)
        val deltaLon = Math.toRadians(location2.longitude - location1.longitude)
        val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
                cos(lat1) * cos(lat2) *
                sin(deltaLon / 2) * sin(deltaLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun showNotification(reminder: Reminder) {
        val channelId = "channelId"
        val notificationId = 0

        val notificationManager = applicationContext.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create NotificationChannel for devices running Android 8.0 or higher
        val channelName = "Channel Name"
        val channelDescription = "Channel Description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }

        notificationManager.createNotificationChannel(channel)

        // Create intent to open the app when the notification is clicked
        val intent = Intent(applicationContext, LocationNotificationWorker::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

        // Create notification using NotificationCompat.Builder
        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.btn_star_big_on)
            .setContentTitle("MCApp Location Notification")
            .setContentText(reminder.message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Show notification using NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }

    companion object {
        private const val WORK_TAG = "com.mcapp.locationNotificationWorker"

        fun startPeriodicWork(context: Context) {
            Log.d("NOTIF", "Starting periodic work")
            val workRequest = PeriodicWorkRequest.Builder(
                LocationNotificationWorker::class.java,
                15,
                TimeUnit.MINUTES)
                .addTag(WORK_TAG)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_TAG, ExistingPeriodicWorkPolicy.KEEP, workRequest)
        }

        fun cancelWork(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG)
        }
    }
}
