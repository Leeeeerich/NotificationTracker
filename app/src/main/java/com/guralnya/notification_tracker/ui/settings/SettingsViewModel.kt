package com.guralnya.notification_tracker.ui.settings

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guralnya.notification_tracker.model.models.IgnorePackage
import com.guralnya.notification_tracker.model.models.vo.IgnoreAppVo
import com.guralnya.notification_tracker.model.models.vo.SettingsVo
import com.guralnya.notification_tracker.model.repository.Repository
import com.guralnya.notification_tracker.model.settings.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesManager: PreferencesManager,
    private val repository: Repository
) : ViewModel() {

    val settingsVo = SettingsVo(ObservableBoolean(preferencesManager.getIsShowRemoved()))
    private val ignorePackagesLiveData = MutableLiveData<List<IgnoreAppVo>>()

    fun getIgnorePackagesLiveData(): LiveData<List<IgnoreAppVo>> = ignorePackagesLiveData

    fun saveChanged(listIgnoreApp: List<IgnorePackage>) {
        preferencesManager.saveIsShowRemoved(settingsVo.isShowRemoved.get())
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAndInsertIgnorePackages(listIgnoreApp)
        }
    }

    fun loadListPackagesWithIgnore() {
        viewModelScope.launch(Dispatchers.IO) {
            ignorePackagesLiveData.postValue(repository.getIgnorePackagesWithIgnore())
        }
    }

}