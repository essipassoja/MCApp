package com.mcapp.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "reminders",
    indices = [
        Index("creatorId"),
    ],
)
data class ReminderEntity(
    val message: String,
    val locationX: Long,
    val locationY: Long,
    val reminderTime: LocalDateTime,
    val creationTime: LocalDateTime,
    @PrimaryKey(autoGenerate = true)
    val creatorId: Long,
    val reminderSeen: Boolean,
)
