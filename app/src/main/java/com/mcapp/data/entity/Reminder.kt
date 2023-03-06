package com.mcapp.data.entity

import java.time.LocalDateTime

data class Reminder(
    val reminderId: Long = 0,
    val message: String,
    val locationX: Long,
    val locationY: Long,
    val reminderTimes: List<LocalDateTime> = emptyList(),
    val creationTime: LocalDateTime,
    val creatorId: Long,
    var reminderSeen: Boolean,
    )