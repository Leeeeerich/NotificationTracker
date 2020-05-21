package com.guralnya.notification_tracker.model.services

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import com.guralnya.notification_tracker.model.models.NotifyInfo
import com.guralnya.notification_tracker.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class NotificationTrackerService : NotificationListenerService() {

    private val repository: Repository by inject()
    private val TAG = "NotificationTracker"

    override fun onCreate() {
        super.onCreate()

        val repository: Repository = get()
        Log.e(TAG, "Service on Create")
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.e(TAG, "onListenerConnected")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.e(TAG, "onListenerDisconnected")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "Service onDestroy")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Toast.makeText(this, "onNotificationPosted", Toast.LENGTH_LONG).show()
        Log.e(TAG, "**********  onNotificationPosted")
        Log.e(
            TAG,
            "ID :" + sbn.id + "\t" + sbn.notification.tickerText + "\t" + sbn.packageName
        )

        GlobalScope.launch(Dispatchers.IO) {
            repository.insertNewNotify(
                NotifyInfo(
                    0,
                    "",
                    sbn.packageName,
                    sbn.notification.tickerText.toString(),
                    DateTime(sbn.notification.`when`)
                )
            )
            Log.e(TAG, "Writed = ${repository.getAllNotify().value.toString()}")
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Toast.makeText(this, "onNotificationRemoved", Toast.LENGTH_LONG).show()
        Log.e(TAG, "********** onNOtificationRemoved")
        Log.e(
            TAG,
            "ID :" + sbn.id + "\t" + sbn.notification.tickerText + "\t" + sbn.packageName
        )
    }
}