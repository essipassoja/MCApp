package com.mcapp.data.datasource

import com.mcapp.data.entity.Reminder
import com.mcapp.data.entity.ReminderEntity
import com.mcapp.data.room.ReminderDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReminderDataSourceImpl constructor(private val reminderDao: ReminderDao) : ReminderDataSource {

    override suspend fun insertOrUpdate(reminder: Reminder) {
        reminderDao.insertOrUpdate(reminder.toEntity())
    }

    override suspend fun getAllReminders(creatorId: Long): Flow<List<Reminder>> = flow {
        emit(
            reminderDao.getAllReminders(creatorId).map {
                it.fromEntity()
            }
        )
    }

    override suspend fun getReminder(reminderId: Long): Flow<Reminder> = flow {
        emit(reminderDao.getReminder(reminderId).fromEntity())
    }

    override suspend fun getNewestReminder(): Flow<Reminder> = flow {
        emit(reminderDao.getNewestReminder().fromEntity())
    }

    override suspend fun delete(reminder: Reminder) {
        reminderDao.delete(reminder.toEntity())
    }

    private fun Reminder.toEntity(): ReminderEntity {
        return ReminderEntity(
            reminderId = reminderId,
            message = message,
            locationX = locationX,
            locationY = locationY,
            reminderTimes = reminderTimes,
            creationTime = creationTime,
            creatorId = creatorId,
            reminderSeen = reminderSeen
        )
    }

    private fun ReminderEntity.fromEntity(): Reminder {
        return Reminder(
            reminderId = reminderId,
            message = message,
            locationX = locationX,
            locationY = locationY,
            reminderTimes = reminderTimes,
            creationTime = creationTime,
            creatorId = creatorId,
            reminderSeen = reminderSeen
        )
    }
}
