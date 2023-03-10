package com.mcapp.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "reminders",
    indices = [
        Index("reminderId", unique = true),
    ],
)
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val reminderId: Long = 0,
    val message: String,
    val locationX: Double?=null,
    val locationY: Double?=null,
    var reminderTimes: List<LocalDateTime>?= null,
    val creationTime: LocalDateTime,
    val creatorId: Long,
    val reminderSeen: Boolean,
) {
    init {
        // Check if reminderTimes is null and assign an empty list if so
        if (reminderTimes == null) {
            this.reminderTimes = emptyList()
        }
    }
}