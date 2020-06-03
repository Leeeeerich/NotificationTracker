package com.guralnya.notification_tracker.ui.settings

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.guralnya.notification_tracker.model.models.vo.SettingsVo
import com.guralnya.notification_tracker.model.settings.PreferencesManager

class SettingsViewModel(private val preferencesManager: PreferencesManager) : ViewModel() {

    val settingsVo = SettingsVo(ObservableBoolean(preferencesManager.getIsShowRemoved()))

    fun saveChanged() {
        preferencesManager.saveIsShowRemoved(settingsVo.isShowRemoved.get())
    }
}