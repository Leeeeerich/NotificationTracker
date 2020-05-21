package com.guralnya.notification_tracker.model.converters

import androidx.room.TypeConverter
import org.joda.time.DateTime

class DateTimeToLong {
    @TypeConverter
    fun from(dateTime: DateTime?): Long? {
        return dateTime?.millis
    }

    @TypeConverter
    fun to(data: Long?): DateTime? {
        return DateTime(data)
    }
}