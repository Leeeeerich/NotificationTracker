package com.guralnya.notification_tracker.ui

import androidx.lifecycle.ViewModel
import com.guralnya.notification_tracker.model.constants.Filtration

class MainViewModel : ViewModel() {

    var filtration = Filtration.ALL_TIME
}