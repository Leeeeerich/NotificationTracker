package com.guralnya.notification_tracker.model.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.guralnya.notification_tracker.model.models.NotifyInfo

@Dao
interface NotifyTrackerDao {

    @Query("SELECT * FROM notifyInfo WHERE dateTimeShow > :filterDateTime ORDER BY dateTimeShow DESC")
    fun getAllNotify(filterDateTime: String): LiveData<List<NotifyInfo>>

    @Insert
    fun insert(data: NotifyInfo)

    @Delete
    fun delete(listData: List<NotifyInfo>)
}