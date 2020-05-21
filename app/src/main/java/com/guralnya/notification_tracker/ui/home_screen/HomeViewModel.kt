package com.guralnya.notification_tracker.ui.home_screen

import androidx.lifecycle.ViewModel
import com.guralnya.notification_tracker.model.repository.Repository

class HomeViewModel(private val repository: Repository) : ViewModel() {

    fun getNotificationsLiveData() = repository.getAllNotify()
}