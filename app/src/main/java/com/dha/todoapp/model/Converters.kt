package com.dha.todoapp.model

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromDateToLong(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromLongToDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}