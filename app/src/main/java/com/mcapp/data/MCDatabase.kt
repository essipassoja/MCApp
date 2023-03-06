package com.mcapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mcapp.data.entity.ReminderEntity
import com.mcapp.data.room.ReminderDao
import com.mcapp.util.LocalDateTimeConverter

@Database(
    entities = [ReminderEntity::class],
    version = 1)
@TypeConverters(LocalDateTimeConverter::class)
abstract class MCDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}