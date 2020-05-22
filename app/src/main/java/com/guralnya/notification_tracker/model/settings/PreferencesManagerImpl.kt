package com.guralnya.notification_tracker.model.settings

import android.content.Context
import android.content.SharedPreferences

class PreferencesManagerImpl(private val context: Context) :
    PreferencesManager {

    companion object {
        private const val PREFERENCES_NAME = "notification_tracker"
        private const val TRACKING_STATUS = "tracking_status"
        private const val PIN_CODE = "pin_code"
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

    override fun getPinCode(): String? = getPreferences().getString(PIN_CODE, "")

    override fun savePinCode(code: String?) {
        if (code != null) {
            getPreferences().edit().putString(PIN_CODE, code).apply()
        } else {
            remove(PIN_CODE)
        }
    }
}