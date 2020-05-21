package com.guralnya.notification_tracker.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.guralnya.notification_tracker.model.db.dao.NotifyTrackerDao
import com.guralnya.notification_tracker.model.models.NotifyInfo

@Database(entities = [NotifyInfo::class], version = 1)
abstract class NotifyTrackerDb : RoomDatabase() {
    abstract fun notifyTracker(): NotifyTrackerDao
}