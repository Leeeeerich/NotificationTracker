package com.guralnya.notification_tracker.model.repository

import androidx.lifecycle.LiveData
import com.guralnya.notification_tracker.model.models.IgnorePackage
import com.guralnya.notification_tracker.model.models.NotifyInfo
import com.guralnya.notification_tracker.model.models.vo.IgnoreAppVo

interface Repository {

    suspend fun insertNewNotify(notifyInfo: NotifyInfo)
    fun getAllNotify(
        filterDateTime: String = "0",
        isGetRemoved: Boolean = true
    ): LiveData<List<NotifyInfo>>

    fun deleteNotifies(listDeletable: List<NotifyInfo>)

    suspend fun insertIgnorePackages(ignorePackages: List<IgnorePackage>)
    suspend fun deleteIgnorePackages(ignorePackages: List<IgnorePackage>)
    suspend fun clearAndInsertIgnorePackages(ignorePackages: List<IgnorePackage>)
    fun getIgnorePackagesWithIgnore(): List<IgnoreAppVo>
}