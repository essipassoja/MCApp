package com.mcapp.data.room

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.mcapp.data.entity.ReminderEntity
import java.time.LocalDateTime

@Database(
    entities = [ReminderEntity::class],
    version = 1)
@TypeConverters(LocalDateTimeConverter::class)
abstract class MCDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}

class LocalDateTimeConverter {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDateTime(date: String): LocalDateTime = LocalDateTime.parse(date)

    @TypeConverter
    fun toDateString(date: LocalDateTime): String = date.toString()
}