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
    val locationX: Long,
    val locationY: Long,
    val reminderTimes: List<LocalDateTime> = emptyList(),
    val creationTime: LocalDateTime,
    val creatorId: Long,
    val reminderSeen: Boolean,
)
