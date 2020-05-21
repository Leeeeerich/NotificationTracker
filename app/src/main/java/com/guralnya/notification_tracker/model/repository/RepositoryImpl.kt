package com.guralnya.notification_tracker.model.repository

import com.guralnya.notification_tracker.model.db.NotifyTrackerDb
import com.guralnya.notification_tracker.model.models.NotifyInfo

class RepositoryImpl(private val db: NotifyTrackerDb) : Repository {
    override suspend fun insertNewNotify(notifyInfo: NotifyInfo) {
        db.notifyTracker().insert(notifyInfo)
    }

    override fun getAllNotify(filterDateTime: String) =
        db.notifyTracker().getAllNotify(filterDateTime)
}