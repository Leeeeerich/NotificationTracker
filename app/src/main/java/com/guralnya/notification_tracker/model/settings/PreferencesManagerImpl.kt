package com.guralnya.notification_tracker.model.settings

import android.content.Context
import android.content.SharedPreferences

class PreferencesManagerImpl(private val context: Context) :
    PreferencesManager {

    companion object {
        private const val PREFERENCES_NAME = "notification_tracker"
        private const val TRACKING_STATUS = "tracking_status"
        private const val PIN_CODE = "pin_code"
        private const val DB_PASS = "db_pass"
        private const val IS_SHOW_REMOVED = "is_show_removed"
    }

    private fun getPreferences(): SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    private fun remove(name: String) = getPreferences().edit().remove(name).apply()

    override fun getTrackingStatus(): Boolean? = getPreferences().getBoolean(TRACKING_STATUS, false)

    override fun saveTrackingStatus(isTracking: Boolean?) {
        if (isTracking != null) {
            getPreferences().edit().putBoolean(TRACKING_STATUS, isTracking).apply()
        } else {
            remove(TRACKING_STATUS)
        }
    }

    override fun getPinCode(): String? = getPreferences().getString(PIN_CODE, null)

    override fun savePinCode(code: String?) {
        if (code != null) {
            getPreferences().edit().putString(PIN_CODE, code).apply()
        } else {
            remove(PIN_CODE)
        }
    }

    override fun getDbPass(): String? = getPreferences().getString(DB_PASS, null)

    override fun saveDbPass(code: String?) {
        if (code != null) {
            getPreferences().edit().putString(DB_PASS, code).apply()
        } else {
            remove(DB_PASS)
        }
    }

    override fun getIsShowRemoved(): Boolean = getPreferences().getBoolean(IS_SHOW_REMOVED, true)

    override fun saveIsShowRemoved(isShow: Boolean) {
        getPreferences().edit().putBoolean(IS_SHOW_REMOVED, isShow).apply()
    }
}