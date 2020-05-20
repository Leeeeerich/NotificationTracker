package com.guralnya.notification_tracker.model.models

import org.joda.time.DateTime

data class NotifyInfo(
    val id: Long,
    val iconApp: String,
    val appName: String,
    val notifyText: String,
    val dateTime: DateTime
)