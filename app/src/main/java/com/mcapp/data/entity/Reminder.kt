package com.mcapp.data.entity

import java.time.LocalDateTime

data class Reminder(
    val message: String,
    val locationX: Long,
    val locationY: Long,
    val reminderTime: LocalDateTime,
    val creationTime: LocalDateTime,
    val creatorId: Long,
    val reminderSeen: Boolean,
    )