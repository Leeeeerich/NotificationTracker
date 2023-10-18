package com.guralnya.notification_tracker.model.models

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.ObservableBoolean
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.guralnya.notification_tracker.R
import com.guralnya.notification_tracker.model.converters.DateTimeToLong
import org.joda.time.DateTime

@Entity(tableName = "notifyInfo")
@TypeConverters(DateTimeToLong::class)
data class NotifyInfo(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val notifyId: Int,
    val appPackageName: String,
    val notifyText: String,
    val isAdding: Boolean,
    val dateTimeShow: DateTime
) {

    @Ignore
    var isChecked = ObservableBoolean(false)

    private fun getAppInfo(context: Context): ApplicationInfo? {
        return try {
            context.packageManager.getApplicationInfo(appPackageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    fun getAppIcon(context: Context): Drawable? {
        return try {
            context.packageManager.getApplicationIcon(appPackageName)
        } catch (e: PackageManager.NameNotFoundException) {
            AppCompatResources.getDrawable(context, R.drawable.ic_no_icon)
        }
    }

    fun getAppName(context: Context) =
        getAppInfo(context)?.let { context.packageManager.getApplicationLabel(it) }
            ?: "(Unknown app)"
}