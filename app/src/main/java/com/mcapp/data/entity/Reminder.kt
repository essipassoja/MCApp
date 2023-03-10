package com.mcapp.data.entity

import java.time.LocalDateTime

data class Reminder(
    val reminderId: Long = 0,
    val message: String,
    val locationX: Double?=null,
    val locationY: Double?=null,
    val reminderTimes: List<LocalDateTime>?= null,
    val creationTime: LocalDateTime,
    val creatorId: Long,
    var reminderSeen: Boolean,
    )