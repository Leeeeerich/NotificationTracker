package com.guralnya.notification_tracker.model.services.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.guralnya.notification_tracker.model.services.NotificationTrackerService
import com.guralnya.notification_tracker.model.settings.PreferencesManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RebootReceiver : BroadcastReceiver(), KoinComponent {

    private val preferences: PreferencesManager by inject()

    private val ACTION_BOOT_COMPLETED = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N)
        Intent.ACTION_LOCKED_BOOT_COMPLETED
    else
        Intent.ACTION_BOOT_COMPLETED

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(
            "NotifyBootReceiver",
            "onReceive, context = $context, intent = $intent"
        )
        if (context != null && intent != null) {
            if (                                          //special construction for Samsung devices
                intent.action == ACTION_BOOT_COMPLETED ||
                intent.action == Intent.ACTION_BOOT_COMPLETED ||
                intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED
            ) {
                if (preferences.getTrackingStatus() == true) {
                    context.startService(Intent(context, NotificationTrackerService::class.java))
                }
            }
        }
    }
}