package com.mcapp.data.room

import com.mcapp.data.entity.Reminder
import com.mcapp.data.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReminderDataSourceImpl(private val reminderDao: ReminderDao): ReminderDataSource {

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

    override suspend fun delete(reminder: Reminder) {
        reminderDao.delete(reminder.toEntity())
    }

    private fun Reminder.toEntity() = ReminderEntity(
        message = this.message,
        locationX = this.locationX,
        locationY = this.locationY,
        reminderTime = this.reminderTime,
        creationTime = this.creationTime,
        creatorId = this.creatorId,
        reminderSeen = this.reminderSeen,
    )

    private fun ReminderEntity.fromEntity() = Reminder(
        message = this.message,
        locationX = this.locationX,
        locationY = this.locationY,
        reminderTime = this.reminderTime,
        creationTime = this.creationTime,
        creatorId = this.creatorId,
        reminderSeen = this.reminderSeen,
    )
}