package com.mcapp.data.repository

import com.mcapp.data.entity.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    suspend fun insertOrUpdate(reminder: Reminder)
    suspend fun getAllReminders(creatorId: Long): Flow<List<Reminder>>
    suspend fun getReminder(reminderId: Long): Flow<Reminder>
    suspend fun getNewestReminder(): Flow<Reminder>
    suspend fun delete(reminder: Reminder)
}
