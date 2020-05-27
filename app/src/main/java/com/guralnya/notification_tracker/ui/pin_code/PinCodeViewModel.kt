package com.guralnya.notification_tracker.ui.pin_code

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guralnya.notification_tracker.model.settings.KeystoreManager
import org.joda.time.DateTime

class PinCodeViewModel(private val keystoreManager: KeystoreManager) : ViewModel() {

    var inputPin: StringBuilder = java.lang.StringBuilder()
    var newPinCode: String = ""
    var isCreatingPin = false

    private var pinCodeLiveData = MutableLiveData<String>()

    fun getPinCodeLiveData(): LiveData<String> = pinCodeLiveData

    fun createPinCode() {
        keystoreManager.setPin(newPinCode)
        keystoreManager.setDbPass((newPinCode + DateTime.now().millis).hashCode().toString())
    }

    init {
        pinCodeLiveData.postValue(keystoreManager.getPin())
    }
}