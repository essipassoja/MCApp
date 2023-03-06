package com.mcapp.util

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter  {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromTimestamp(value: String?): List<LocalDateTime>? {
        return value?.takeIf { it.isNotBlank() }?.split(",")?.map { LocalDateTime.parse(it, formatter) }
    }

    @TypeConverter
    fun toTimestamp(values: List<LocalDateTime>?): String? {
        return values?.joinToString(",") { it.format(formatter) }
    }

    @TypeConverter
    fun toLocalDateTime(date: String): LocalDateTime = LocalDateTime.parse(date)

    @TypeConverter
    fun toDateString(date: LocalDateTime): String = date.toString()
}