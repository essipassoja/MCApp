package com.mcapp.data.room

import androidx.room.*
import com.mcapp.data.entity.ReminderEntity

@Dao
interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(reminder: ReminderEntity) {
        println("Making a new reminder: " + reminder.message)
    }

    @Query("SELECT * FROM reminders WHERE creatorId LIKE :creatorId")
    fun getAllReminders(creatorId: Long): List<ReminderEntity>

    @Delete
    fun delete(reminder: ReminderEntity)

}