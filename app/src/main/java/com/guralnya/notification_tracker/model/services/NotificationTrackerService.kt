package com.guralnya.notification_tracker.model.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.guralnya.notification_tracker.model.models.NotifyInfo
import com.guralnya.notification_tracker.model.repository.Repository
import com.guralnya.notification_tracker.model.settings.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.koin.android.ext.android.inject

class NotificationTrackerService : NotificationListenerService() {

    private val repository: Repository by inject()
    private val pref: PreferencesManager by inject()

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (pref.getTrackingStatus() == true) {
            GlobalScope.launch(Dispatchers.IO) {
                repository.insertNewNotify(
                    NotifyInfo(
                        id = 0,
                        notifyId = sbn.id,
                        appPackageName = sbn.packageName,
                        notifyText = sbn.notification.tickerText?.toString() ?: "",
                        dateTimeShow = DateTime(sbn.notification.`when`)
                    )
                )
            }
        }
    }
}