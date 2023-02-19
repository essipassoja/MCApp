package com.mcapp.data.datasource

import com.mcapp.data.entity.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderDataSource {
    suspend fun insertOrUpdate(reminder: Reminder)
    suspend fun delete(reminder: Reminder)
    suspend fun getAllReminders(creatorId: Long): Flow<List<Reminder>>
}
