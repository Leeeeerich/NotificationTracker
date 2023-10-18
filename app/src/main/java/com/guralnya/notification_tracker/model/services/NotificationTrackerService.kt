package com.guralnya.notification_tracker.model.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import com.guralnya.notification_tracker.R
import com.guralnya.notification_tracker.model.models.NotifyInfo
import com.guralnya.notification_tracker.model.repository.Repository
import com.guralnya.notification_tracker.model.settings.PreferencesManager
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.koin.android.ext.android.inject

class NotificationTrackerService : NotificationListenerService() {

    private val repository: Repository by inject()
    private val pref: PreferencesManager by inject()
    private val basicChannelId = "basicNotifyChannel"

    companion object {
        private const val ONGOING_NOTIFICATION_ID = 101
        fun startService(context: Context) {
            val intent = Intent(context, NotificationTrackerService::class.java)
            context.startForegroundService(intent)
        }

        fun stopService(context: Context) {
            val intent = Intent(context, NotificationTrackerService::class.java)
            context.stopService(intent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        makeForeground()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName == packageName) return
        if (pref.getTrackingStatus() == true) {
            CoroutineScope(CoroutineName("Outer")).launch(Dispatchers.IO) {
                repository.insertNewNotify(
                    NotifyInfo(
                        id = 0,
                        notifyId = sbn.id,
                        appPackageName = sbn.packageName,
                        notifyText = sbn.notification.tickerText?.toString() ?: "",
                        isAdding = true,
                        dateTimeShow = DateTime(sbn.notification.`when`)
                    )
                )
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        if (sbn.packageName == packageName) return
        if (pref.getTrackingStatus() == true) {
            CoroutineScope(CoroutineName("Outer")).launch(Dispatchers.IO) {
                repository.insertNewNotify(
                    NotifyInfo(
                        id = 0,
                        notifyId = sbn.id,
                        appPackageName = sbn.packageName,
                        notifyText = sbn.notification.tickerText?.toString() ?: "",
                        isAdding = false,
                        dateTimeShow = DateTime(sbn.notification.`when`)
                    )
                )
            }
        }
    }

    private fun makeForeground() {
        createNotifyChannel()
        startForeground(ONGOING_NOTIFICATION_ID, getNotification())
    }

    private fun getNotification(): Notification {
        return NotificationCompat.Builder(this, basicChannelId)
            .setSmallIcon(R.drawable.ic_info)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("")
            .setPriority(NotificationCompat.PRIORITY_MAX).build()
    }

    private fun createNotifyChannel() {
        val name = getString(R.string.app_name)
        val descriptionText = getString(R.string.app_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(basicChannelId, name, importance)
        mChannel.description = descriptionText
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }
}
