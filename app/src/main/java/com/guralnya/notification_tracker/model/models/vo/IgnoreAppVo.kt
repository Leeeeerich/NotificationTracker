package com.guralnya.notification_tracker.model.models.vo

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.databinding.ObservableBoolean
import androidx.room.DatabaseView
import androidx.room.Ignore

@DatabaseView("SELECT DISTINCT appPackageName, packageName FROM notifyInfo LEFT JOIN ignorePackages ON packageName = appPackageName")
data class IgnoreAppVo(
    val appPackageName: String,
    private val packageName: String?
) {
    @Ignore
    var isChecked = ObservableBoolean(appPackageName == packageName)

    private fun getAppInfo(context: Context): ApplicationInfo? {
        return try {
            context.packageManager.getApplicationInfo(appPackageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    fun getAppIcon(context: Context) = context.packageManager.getApplicationIcon(appPackageName)

    fun getAppName(context: Context) =
        getAppInfo(context)?.let { context.packageManager.getApplicationLabel(it) }
            ?: "(Unknown app)"

    fun changeCheckedListener() {
        isChecked.set(!isChecked.get())
    }
}