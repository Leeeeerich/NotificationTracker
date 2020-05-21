package com.guralnya.notification_tracker.model.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.guralnya.notification_tracker.model.models.NotifyInfo

@Dao
interface NotifyTrackerDao {

    @Query("SELECT * FROM notifyInfo")
    fun getAllNotify(): LiveData<List<NotifyInfo>>

    @Insert
    fun insert(data: NotifyInfo)
}