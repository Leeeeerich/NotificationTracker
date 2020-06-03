package com.guralnya.notification_tracker.ui.home_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guralnya.notification_tracker.model.constants.Filtration
import com.guralnya.notification_tracker.model.models.NotifyInfo
import com.guralnya.notification_tracker.model.repository.Repository
import com.guralnya.notification_tracker.model.settings.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: Repository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val trackingStatusLiveData = MutableLiveData<Boolean>()
    var filtration = Filtration.ALL_TIME

    fun getNotificationsLiveData(filterMilliseconds: Long) =
        repository.getAllNotify(filterMilliseconds.toString())

    fun getTrackingStatus(): LiveData<Boolean> = trackingStatusLiveData

    fun setTrackingStatus(isTracking: Boolean) {
        preferencesManager.saveTrackingStatus(isTracking) //TODO redo to getting liveData from preferences
        trackingStatusLiveData.postValue(isTracking)
    }

    fun deleteNotifies(listDeletable: List<NotifyInfo>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNotifies(listDeletable)
        }
    }

    fun init() {
        trackingStatusLiveData.postValue(preferencesManager.getTrackingStatus())
    }
}