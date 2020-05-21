package com.guralnya.notification_tracker.model.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.guralnya.notification_tracker.model.converters.DateTimeToLong
import org.joda.time.DateTime

@Entity(tableName = "notifyInfo")
@TypeConverters(DateTimeToLong::class)
data class NotifyInfo(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val iconApp: String,
    val appName: String,
    val notifyText: String,
    val dateTime: DateTime
)