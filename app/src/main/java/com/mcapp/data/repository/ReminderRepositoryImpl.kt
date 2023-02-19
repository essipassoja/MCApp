package com.mcapp.data.repository

import com.mcapp.data.entity.Reminder
import com.mcapp.data.datasource.ReminderDataSource
import kotlinx.coroutines.flow.Flow

class ReminderRepositoryImpl(private val reminderDataSource: ReminderDataSource):
    ReminderRepository {

    override suspend fun insertOrUpdate(reminder: Reminder) {
        reminderDataSource.insertOrUpdate(reminder)
    }

    override suspend fun getAllReminders(creatorId: Long): Flow<List<Reminder>> {
        return reminderDataSource.getAllReminders(creatorId)
    }

    override suspend fun delete(reminder: Reminder) {
        reminderDataSource.delete(reminder)
    }
}