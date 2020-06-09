package com.guralnya.notification_tracker.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.guralnya.notification_tracker.model.db.dao.IgnorePackagesDao
import com.guralnya.notification_tracker.model.db.dao.NotifyTrackerDao
import com.guralnya.notification_tracker.model.models.IgnorePackage
import com.guralnya.notification_tracker.model.models.NotifyInfo
import com.guralnya.notification_tracker.model.models.vo.IgnoreAppVo

@Database(
    entities = [NotifyInfo::class, IgnorePackage::class],
    version = 3,
    exportSchema = true,
    views = [IgnoreAppVo::class]
)
abstract class NotifyTrackerDb : RoomDatabase() {
    abstract fun notifyTracker(): NotifyTrackerDao
    abstract fun ignorePackages(): IgnorePackagesDao
}