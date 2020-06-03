package com.guralnya.notification_tracker.model.settings

interface PreferencesManager {

    fun getTrackingStatus(): Boolean?
    fun saveTrackingStatus(isTracking: Boolean?)

    fun getPinCode(): String?
    fun savePinCode(code: String?)

    fun getDbPass(): String?
    fun saveDbPass(code: String?)

    fun getIsShowRemoved(): Boolean
    fun saveIsShowRemoved(isShow: Boolean)
}