package com.guralnya.notification_tracker.ui.pin_code

import androidx.lifecycle.ViewModel
import com.guralnya.notification_tracker.model.settings.KeystoreManager

class PinCodeViewModel(private val keystoreManager: KeystoreManager) : ViewModel() {

    var inputPin: StringBuilder = java.lang.StringBuilder()
    var pinCode: String? = keystoreManager.getPassword()
    var newPinCode: String = ""
    var isCreatingPin = false

    fun createPinCode() {
        keystoreManager.setPassword(newPinCode)
    }
}