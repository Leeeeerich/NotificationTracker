package com.guralnya.notification_tracker.ui.home_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guralnya.notification_tracker.model.repository.Repository
import com.guralnya.notification_tracker.model.settings.PreferencesManager

class HomeViewModel(
    private val repository: Repository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val trackingStatusLiveData = MutableLiveData<Boolean>()

    fun getNotificationsLiveData(filterMilliseconds: Long) =
        repository.getAllNotify(filterMilliseconds.toString())

    fun getTrackingStatus(): LiveData<Boolean> = trackingStatusLiveData

    fun setTrackingStatus(isTracking: Boolean) {
        preferencesManager.saveTrackingStatus(isTracking) //TODO redo to getting liveData from preferences
        trackingStatusLiveData.postValue(isTracking)
    }
}