package com.guralnya.notification_tracker.model.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.guralnya.notification_tracker.model.models.NotifyInfo

@Dao
interface NotifyTrackerDao {

    @Query(
        "SELECT * FROM notifyInfo ni LEFT JOIN ignorePackages ip ON (ip.packageName = ni.appPackageName) WHERE (SELECT CASE WHEN :isEnabledPackageFilter THEN (ip.packageName IS NULL) ELSE 1 END) AND (dateTimeShow > :filterDateTime) AND (isAdding BETWEEN :isGetRemoved AND 1) ORDER BY dateTimeShow DESC"
    )
    fun getAllNotify(
        filterDateTime: String,
        isGetRemoved: Boolean,
        isEnabledPackageFilter: Boolean
    ): LiveData<List<NotifyInfo>>

    @Insert()
    fun insert(data: NotifyInfo)

    @Delete
    fun delete(listData: List<NotifyInfo>)
}