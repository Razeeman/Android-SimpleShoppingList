package com.example.util.simpleshoppinglist.data.db

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun toTimestamp(value: Date): Long {
        return value.time
    }

}