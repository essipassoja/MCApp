package com.mcapp.data.room

import androidx.room.*
import com.mcapp.data.entity.ReminderEntity

@Dao
interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(reminder: ReminderEntity)

    @Query("SELECT * FROM reminders WHERE creatorId LIKE :creatorId")
    suspend fun getAllReminders(creatorId: Long): List<ReminderEntity>

    @Query("SELECT * FROM reminders WHERE reminderId LIKE :reminderId")
    suspend fun getReminder(reminderId: Long): ReminderEntity

    @Query("SELECT * FROM reminders ORDER BY reminderId DESC LIMIT 1")
    suspend fun getNewestReminder(): ReminderEntity

    @Delete
    suspend fun delete(reminder: ReminderEntity)

}