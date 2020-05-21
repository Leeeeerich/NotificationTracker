package com.guralnya.notification_tracker.model.repository

import androidx.lifecycle.LiveData
import com.guralnya.notification_tracker.model.models.NotifyInfo

interface Repository {

    suspend fun insertNewNotify(notifyInfo: NotifyInfo)
    fun getAllNotify(): LiveData<List<NotifyInfo>>
}