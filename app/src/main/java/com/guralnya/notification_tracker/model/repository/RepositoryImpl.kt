package com.guralnya.notification_tracker.model.repository

import com.guralnya.notification_tracker.model.db.NotifyTrackerDb
import com.guralnya.notification_tracker.model.models.IgnorePackage
import com.guralnya.notification_tracker.model.models.NotifyInfo
import com.guralnya.notification_tracker.model.models.vo.IgnoreAppVo

class RepositoryImpl(private val db: NotifyTrackerDb) : Repository {
    override suspend fun insertNewNotify(notifyInfo: NotifyInfo) {
        db.notifyTracker().insert(notifyInfo)
    }

    override fun getAllNotify(filterDateTime: String, isGetRemoved: Boolean) =
        db.notifyTracker().getAllNotify(filterDateTime, isGetRemoved)

    override fun deleteNotifies(listDeletable: List<NotifyInfo>) {
        db.notifyTracker().delete(listDeletable)
    }

    override suspend fun insertIgnorePackages(ignorePackages: List<IgnorePackage>) {
        db.ignorePackages().insertIgnorePackages(ignorePackages)
    }

    override suspend fun deleteIgnorePackages(ignorePackages: List<IgnorePackage>) {
        db.ignorePackages().delete(ignorePackages)
    }

    override suspend fun clearAndInsertIgnorePackages(ignorePackages: List<IgnorePackage>) {
        db.ignorePackages().deleteAll()
        db.ignorePackages().insertIgnorePackages(ignorePackages)
    }

    override fun getIgnorePackagesWithIgnore(): List<IgnoreAppVo> =
        db.ignorePackages().getListPackagesWithIgnore()
}