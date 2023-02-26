package com.mcapp.util

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        // Implement reminder logic here
        return Result.success()
    }
}

fun makeReminderRequest(
    reminderTime: LocalDateTime,
    context: Context) {

    val timeZone = ZoneId.systemDefault()
    val zonedDateTime = reminderTime.atZone(timeZone)
    val reminderTimeInMillis = zonedDateTime.toInstant().toEpochMilli()

    val now = Calendar.getInstance().timeInMillis
    val delay = reminderTimeInMillis - now

    val reminderRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueue(reminderRequest)
}