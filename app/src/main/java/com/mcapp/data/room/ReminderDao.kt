package com.mcapp.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mcapp.data.entity.Reminder
import com.mcapp.data.entity.ReminderEntity

class ReminderDaoImpl(private val reminderDatabase: MCDatabase) : ReminderDao {
    override suspend fun insertOrUpdate(reminder: ReminderEntity) {
        reminderDatabase.reminderDao().insertOrUpdate(reminder)
    }

    override fun getAllReminders(creatorId: Long): LiveData<List<Reminder>> {
        return reminderDatabase.reminderDao().getAllReminders(creatorId)
    }

    override suspend fun delete(reminder: ReminderEntity) {
        reminderDatabase.reminderDao().delete(reminder)
    }
}

@Dao
interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(reminder: ReminderEntity)

    @Query("""SELECT * FROM reminders WHERE creatorId = :creatorId""")
    fun getAllReminders(creatorId: Long): LiveData<List<Reminder>>

    @Delete
    suspend fun delete(reminder: ReminderEntity)

}