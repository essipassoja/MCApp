package com.mcapp.data.room

import com.mcapp.data.entity.Reminder
import com.mcapp.data.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReminderDataSourceImpl(private val reminderDao: ReminderDao) : ReminderDataSource {

    override suspend fun insertOrUpdate(reminder: Reminder) {
        reminderDao.insertOrUpdate(reminder.toEntity())
    }

    override suspend fun delete(reminder: Reminder) {
        reminderDao.delete(reminder.toEntity())
    }

    override suspend fun getAllReminders(creatorId: Long): Flow<List<Reminder>> = flow {
        emit(
            reminderDao.getAllReminders(creatorId).map {
                it.fromEntity()
            }
        )
    }

    private fun Reminder.toEntity(): ReminderEntity {
        return ReminderEntity(
            message = message,
            locationX = locationX,
            locationY = locationY,
            reminderTime = reminderTime,
            creationTime = creationTime,
            creatorId = creatorId,
            reminderSeen = reminderSeen
        )
    }

    private fun ReminderEntity.fromEntity(): Reminder {
        return Reminder(
            message = message,
            locationX = locationX,
            locationY = locationY,
            reminderTime = reminderTime,
            creationTime = creationTime,
            creatorId = creatorId,
            reminderSeen = reminderSeen
        )
    }
}
